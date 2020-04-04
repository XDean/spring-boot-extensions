package cn.xdean.spring.ex.task;

import cn.xdean.spring.ex.task.model.XTaskRunInfo;

import java.util.List;
import java.util.Optional;

public interface XTaskService {
    List<XTask> getAll();

    Optional<XTask> find(String id);

    int run(XTask task, String who);

    Optional<Boolean> stop(XTask task, int runId, String who);

    List<XTaskRunInfo> getRecentRunInfo(XTask task, int limit);

    Optional<XTaskRunInfo> getInfo(XTask task, int runId, boolean withLog);
}
