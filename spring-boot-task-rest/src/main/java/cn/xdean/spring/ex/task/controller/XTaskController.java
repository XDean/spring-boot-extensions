package cn.xdean.spring.ex.task.controller;

import cn.xdean.spring.ex.task.XTaskService;
import cn.xdean.spring.ex.task.model.XTaskLogEntity;
import cn.xdean.spring.ex.task.model.XTaskRunInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XTaskController {
    @Autowired
    XTaskService taskService;

    @GetMapping("/tasks")
    public List<String> getAll() {
        return taskService.getAll().stream().map(e -> e.id()).collect(Collectors.toList());
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<XTaskInfoDTO> get(@PathVariable("taskId") String taskId,
                                            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        return taskService.find(taskId)
                .map(task -> XTaskInfoDTO.builder()
                        .id(task.id())
                        .cron(task.cron())
                        .runs(taskService.getRecentRunInfo(task, limit).stream()
                                .map(XTaskRunInfoDTO::from)
                                .collect(Collectors.toList()))
                        .build())
                .map(e -> ResponseEntity.ok(e))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/task/{taskId}")
    public ResponseEntity<XTaskRunDTO> run(@PathVariable("taskId") String taskId, Authentication auth) {
        return taskService.find(taskId)
                .map(task -> taskService.run(task, auth.getName()))
                .map(id -> XTaskRunDTO.builder().id(id).build())
                .map(res -> ResponseEntity.ok(res))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/task/{taskId}/{runId}/stop")
    public ResponseEntity<XSimpleMessageDTO> stop(@PathVariable("taskId") String taskId,
                                                  @PathVariable("runId") int runId,
                                                  Authentication auth) {
        return taskService.find(taskId)
                .flatMap(task -> taskService.stop(task, runId, auth.getName()))
                .map(stopped -> {
                    if (stopped) {
                        return ResponseEntity.ok(XSimpleMessageDTO.of("Stop Success"));
                    } else {
                        return ResponseEntity.badRequest().body(XSimpleMessageDTO.of("Task had been stopped"));
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/task/{taskId}/{runId}")
    public ResponseEntity<XTaskRunInfoDTO> getInfo(@PathVariable("taskId") String taskId,
                                                   @PathVariable("runId") int runId,
                                                   @RequestParam(name = "log", required = false) boolean log) {
        return taskService.find(taskId)
                .flatMap(task -> taskService.getInfo(task, runId, log))
                .map(info -> ResponseEntity.ok(XTaskRunInfoDTO.from(info)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/task/{taskId}/{runId}/log")
    public ResponseEntity<String> getLog(@PathVariable("taskId") String taskId,
                                         @PathVariable("runId") int runId) {
        return taskService.find(taskId)
                .flatMap(task -> taskService.getInfo(task, runId, true))
                .map(info -> {
                    SimpleDateFormat f = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                    List<XTaskLogEntity> logs = new ArrayList<>(info.getLogs());
                    if (info.getStatus() == XTaskRunInfo.Status.ERROR && info.getStopTime() != null) {
                        logs.add(XTaskLogEntity.builder()
                                .time(info.getStopTime())
                                .type(XTaskLogEntity.Type.ERROR)
                                .message(info.getStopBy())
                                .build());
                    }
                    return ResponseEntity.ok(logs.stream()
                            .map(e -> String.format("%s [%5s] %s", f.format(e.getTime()), e.getType().toString(), e.getMessage()))
                            .collect(Collectors.joining("\n")));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
