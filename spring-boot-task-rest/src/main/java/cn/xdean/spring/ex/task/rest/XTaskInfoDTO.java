package cn.xdean.spring.ex.task.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class XTaskInfoDTO {
  @JsonProperty("id")
  String id;

  @JsonProperty("cron")
  String cron;

  @JsonProperty("runs")
  List<XTaskRunInfoDTO> runs;
}
