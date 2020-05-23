package cn.xdean.spring.ex.websocket.topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketTopicEvent {
    @JsonProperty(value = "topic", required = true)
    String topic;

    @JsonProperty(value = "event", required = true)
    String event;

    @JsonProperty(value = "payload", required = false)
    JsonNode payload;
}
