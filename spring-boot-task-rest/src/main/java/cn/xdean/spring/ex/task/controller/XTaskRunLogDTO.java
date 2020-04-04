package cn.xdean.spring.ex.task.controller;

import cn.xdean.spring.ex.task.model.XTaskLogEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class XTaskRunLogDTO {
    @JsonProperty("time")
    long time;

    @JsonProperty("type")
    XTaskLogTypeDTO type;

    @JsonProperty("message")
    String message;

    public static XTaskRunLogDTO from(XTaskLogEntity e) {
        return XTaskRunLogDTO.builder()
                .time(e.getTime())
                .type(XTaskLogTypeDTO.fromValue(e.getType()))
                .message(e.getMessage())
                .build();
    }
}
