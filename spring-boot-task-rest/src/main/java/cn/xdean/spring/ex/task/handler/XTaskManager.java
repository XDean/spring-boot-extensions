package cn.xdean.spring.ex.task.handler;

import cn.xdean.spring.ex.task.XTask;
import cn.xdean.spring.ex.task.XTaskLogger;
import cn.xdean.spring.ex.task.XTaskService;
import cn.xdean.spring.ex.task.dao.XTaskLogRepository;
import cn.xdean.spring.ex.task.model.XTaskLogEntity;
import cn.xdean.spring.ex.task.model.XTaskRunInfo;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import io.reactivex.subjects.UnicastSubject;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class XTaskManager implements SchedulingConfigurer, XTaskService, DisposableBean {

    @Autowired(required = false)
    List<XTask> tasks = Collections.emptyList();
    @Autowired
    XTaskLogRepository logRepository;

    @Autowired
    TaskScheduler scheduler;

    private final List<TaskRunImpl> runs = new ArrayList<>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        tasks.forEach(t -> {
            if (!t.cron().isEmpty()) {
                taskRegistrar.addCronTask(() -> {
                    TaskRunImpl r = new TaskRunImpl(t, "_system");
                    r.run();
                }, t.cron());
            }
        });
    }

    @Override
    public void destroy() {
        runs.forEach(r -> r.stop("_system"));
    }

    @Override
    public List<XTask> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    @Override
    public Optional<XTask> find(String id) {
        return tasks.stream().filter(t -> t.id().equals(id)).findAny();
    }

    @Override
    public int run(XTask task, String who) {
        TaskRunImpl r = new TaskRunImpl(task, who);
        scheduler.schedule(r::run, new Date());
        return r.runId;
    }

    @Override
    public Optional<Boolean> stop(XTask task, int runId, String who) {
        Optional<Boolean> memory = runs.stream()
                .filter(e -> e.getTaskId().equals(task.id()))
                .filter(e -> e.getRunId() == runId)
                .findAny()
                .map(r -> r.stop(who));
        if (memory.isPresent()) {
            return memory;
        } else if (logRepository.existsByTaskIdAndRunId(task.id(), runId)) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<XTaskRunInfo> getRecentRunInfo(XTask task, int limit) {
        return logRepository.findAllByTaskIdAndTypeOrderByIdAsc(
                task.id(), XTaskLogEntity.Type.START, limit > 0 ? PageRequest.of(0, limit) : Pageable.unpaged())
                .stream()
                .map(e -> getInfo(task, e.getRunId(), false))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<XTaskRunInfo> getInfo(XTask task, int runId, boolean withLog) {
        List<XTaskLogEntity> sysLogs = logRepository.findAllByTaskIdAndRunIdAndTypeInOrderByIdAsc(task.id(), runId, XTaskLogEntity.Type.systemTypes());
        int sysSize = sysLogs.size();
        XTaskLogEntity startLog;
        XTaskLogEntity endLog;
        if (sysSize == 0) {
            return Optional.empty();
        } else if (sysSize == 1) {
            startLog = sysLogs.get(0);
            endLog = null;
        } else if (sysSize == 2) {
            startLog = sysLogs.stream()
                    .filter(e -> e.getType() == XTaskLogEntity.Type.START)
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("A started task must have type=START log"));
            endLog = sysLogs.stream()
                    .filter(e -> e.getType() != XTaskLogEntity.Type.START)
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("A ended task must have type!=START log"));
        } else {
            throw new IllegalStateException("Any run must have at most 2 system logs");
        }
        return Optional.of(XTaskRunInfo.builder()
                .taskId(task.id())
                .runId(runId)
                .startTime(startLog.getTime())
                .stopTime(endLog == null ? null : endLog.getTime())
                .startBy(startLog.getMessage())
                .stopBy(endLog == null ? null : endLog.getMessage())
                .status(endLog == null ? XTaskRunInfo.Status.RUNNING : XTaskRunInfo.Status.from(endLog.getType()))
                .logs(withLog ?
                        logRepository.findAllByTaskIdAndRunIdAndTypeInOrderByIdAsc(task.id(), runId, XTaskLogEntity.Type.userTypes()) :
                        Collections.emptyList())
                .build());
    }

    @Value
    private class TaskRunImpl implements XTaskLogger {
        int runId;
        String startTrigger;
        XTask task;
        Subject<XTaskLogEntity> subject = UnicastSubject.create();
        String taskId;
        Disposable disposable;
        @NonFinal
        AtomicBoolean stopped = new AtomicBoolean(false);

        TaskRunImpl(XTask task, String startTrigger) {
            this.task = task;
            this.taskId = task.id();
            this.runId = newTaskRun(taskId, startTrigger);
            this.startTrigger = startTrigger;
            runs.add(this);
            this.disposable = subject
                    .doFinally(() -> {
                        runs.remove(this);
                    })
                    .doOnError(e -> stopped.set(true))
                    .onErrorReturn(e -> newRow(XTaskLogEntity.Type.DONE_ERROR, getStackTrace(e)))
                    .buffer(1, TimeUnit.SECONDS, 10)
                    .subscribe(rows -> {
                        logRepository.saveAll(rows);
                    }, error -> {
                        logRepository.save(newRow(XTaskLogEntity.Type.ERROR,
                                "Server internal error happened: " + getStackTrace(error)));
                    }, () -> {
                        if (stopped.compareAndSet(false, true)) {
                            logRepository.save(newRow(XTaskLogEntity.Type.DONE, ""));
                        }
                    });
        }

        @Override
        public void error(String message) {
            subject.onNext(newRow(XTaskLogEntity.Type.ERROR, message));
        }

        @Override
        public void warn(String message) {
            subject.onNext(newRow(XTaskLogEntity.Type.WARN, message));
        }

        @Override
        public void info(String message) {
            subject.onNext(newRow(XTaskLogEntity.Type.INFO, message));
        }

        int newTaskRun(String taskId, String trigger) {
            int id = logRepository.findFirstByTaskIdOrderByRunIdDesc(taskId).map(i -> i.getRunId() + 1).orElse(0);
            XTaskLogEntity e = XTaskLogEntity.builder()
                    .taskId(taskId)
                    .runId(id)
                    .type(XTaskLogEntity.Type.START)
                    .time(System.currentTimeMillis())
                    .message(trigger)
                    .build();
            logRepository.save(e);
            return id;
        }

        void run() {
            try {
                this.task.run(this);
                done();
            } catch (Exception e) {
                errorStop(e);
            }
        }

        boolean stop(String who) {
            if (stopped.compareAndSet(false, true)) {
                this.disposable.dispose();
                logRepository.save(newRow(XTaskLogEntity.Type.STOP, who));
                return true;
            } else {
                return false;
            }
        }

        void done() {
            if (!subject.hasComplete() && !subject.hasThrowable()) {
                subject.onComplete();
            }
        }

        void errorStop(Exception e) {
            if (!subject.hasComplete() && !subject.hasThrowable()) {
                subject.onError(e);
            }
        }

        XTaskLogEntity newRow(XTaskLogEntity.Type type, String message) {
            return XTaskLogEntity.builder()
                    .taskId(taskId)
                    .runId(runId)
                    .time(System.currentTimeMillis())
                    .type(type)
                    .message(message)
                    .build();
        }
    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
