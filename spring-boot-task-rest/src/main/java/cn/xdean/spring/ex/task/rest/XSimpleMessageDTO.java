package cn.xdean.spring.ex.task.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class XSimpleMessageDTO {
    @JsonProperty("message")
    String message;

    public static XSimpleMessageDTO of(String s) {
        return XSimpleMessageDTO.builder().message(s).build();
    }
}
