package cn.xdean.spring.ex.task.service;

import cn.xdean.spring.ex.task.XTask;
import cn.xdean.spring.ex.task.XTaskLogger;
import cn.xdean.spring.ex.task.XTaskProperties;
import cn.xdean.spring.ex.task.dao.XTaskLogRepository;
import cn.xdean.spring.ex.task.model.XTaskLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XTaskLogCleanupTask implements XTask {

    @Autowired
    XTaskLogRepository logRepository;

    @Autowired
    XTaskProperties properties;

    @Override
    public String id() {
        return "xtask-cleanup";
    }

    @Override
    public String cron() {
        return properties.getCleanup().getCron();
    }

    @Override
    @Transactional
    public void run(XTaskLogger logger) {
        Run run = new Run(logger);
        run.cleanupByTime();
        run.cleanupByCount();
    }

    private class Run {
        XTaskLogger logger;

        Run(XTaskLogger logger) {
            this.logger = logger;
        }

        private void cleanupByTime() {
            Duration maxDuration = properties.getCleanup().getMaxDuration();
            if (maxDuration == null) {
                return;
            }
            logger.info("Start Cleanup Task Log By Time");
            long liveMillis = maxDuration.getSeconds() * 1000;
            logger.info("Max Duration: " + maxDuration);
            List<XTaskLogEntity> startLogs = logRepository.findAllByTimeBeforeAndTypeIs(System.currentTimeMillis() - liveMillis, XTaskLogEntity.Type.START);
            logger.info(String.format("Find %d Expired", startLogs.size()));
            for (int i = 0; i < startLogs.size(); i++) {
                XTaskLogEntity startLog = startLogs.get(i);
                logger.info(String.format("Delete Logs (%d/%d): %s#%d", i + 1, startLogs.size(), startLog.getTaskId(), startLog.getRunId()));
                logRepository.deleteAllByTaskIdAndRunId(startLog.getTaskId(), startLog.getRunId());
            }
            logger.info("Cleanup By Time Done.");
        }

        private void cleanupByCount() {
            logger.info("Start Cleanup Task Log By Count");
            int max = properties.getCleanup().getMaxCountPerTask();
            logger.info("Max Count: " + max);
            Map<String, List<XTaskLogEntity>> startLogs = logRepository.findAllByTypeIs(XTaskLogEntity.Type.START)
                    .stream()
                    .collect(Collectors.groupingBy(e -> e.getTaskId()));
            startLogs.values().removeIf(e -> e.size() <= max);
            logger.info(String.format("Find %s Tasks Need Cleanup", startLogs.size()));
            int index = 0;
            for (Map.Entry<String, List<XTaskLogEntity>> entry : startLogs.entrySet()) {
                String taskId = entry.getKey();
                List<XTaskLogEntity> logs = entry.getValue();
                logger.info(String.format("Delete Logs (%d/%d): %s (%d Runs)", index + 1, startLogs.size(), taskId, logs.size() - max));
                logs.stream()
                        .sorted(Comparator.comparing(e -> e.getTime()))
                        .limit(logs.size() - max)
                        .forEach(e -> {
                            logRepository.deleteAllByTaskIdAndRunId(e.getTaskId(), e.getRunId());
                        });
            }
            logger.info("Cleanup By Count Done.");
        }
    }
}
