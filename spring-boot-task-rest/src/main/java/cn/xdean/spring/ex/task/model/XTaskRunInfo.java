package cn.xdean.spring.ex.task.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.List;

@Value
@Builder
public class XTaskRunInfo {
    public enum Status {
        RUNNING, STOP, DONE, ERROR;

        public static Status from(XTaskLogEntity.Type type) {
            switch (type) {
                case DONE:
                    return Status.DONE;
                case DONE_ERROR:
                    return Status.ERROR;
                case STOP:
                    return Status.STOP;
                default:
                    return Status.RUNNING;
            }
        }
    }

    String taskId;

    int runId;

    long startTime;

    @Nullable
    Long stopTime;

    Status status;

    String startBy;

    @Nullable
    String stopBy;

    List<XTaskLogEntity> logs;
}
