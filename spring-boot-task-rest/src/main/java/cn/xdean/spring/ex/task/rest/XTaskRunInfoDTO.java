package cn.xdean.spring.ex.task.rest;

import cn.xdean.spring.ex.task.model.XTaskRunInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class XTaskRunInfoDTO {

  @JsonProperty("id")
  int runId;

  @JsonProperty("status")
  XTaskRunStatusDTO status;

  @JsonProperty("start-time")
  long startTime;

  @JsonProperty("user-start-time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  Date userStartTime;

  @JsonProperty("stop-time")
  Long stopTime;

  @JsonProperty("user-stop-time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  Date userStopTime;

  @JsonProperty("start-by")
  String startBy;

  @JsonProperty("stop-by")
  String stopBy;

  @JsonProperty("logs")
  List<XTaskRunLogDTO> logs;

  public static XTaskRunInfoDTO from(XTaskRunInfo e) {
    return XTaskRunInfoDTO.builder()
            .runId(e.getRunId())
            .startTime(e.getStartTime())
            .userStartTime(new Date(e.getStartTime()))
            .stopTime(e.getStopTime())
            .userStopTime(e.getStopTime() == null ? null : new Date(e.getStopTime()))
            .status(XTaskRunStatusDTO.fromValue(e.getStatus()))
            .startBy(e.getStartBy())
            .stopBy(e.getStopBy())
            .logs(e.getLogs().isEmpty() ? null : e.getLogs().stream()
                    .map(l -> XTaskRunLogDTO.from(l))
                    .collect(Collectors.toList()))
            .build();
  }
}
