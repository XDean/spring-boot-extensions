package cn.xdean.spring.ex.task.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class XTaskRunDTO {
  @JsonProperty("id")
  int id;
}
