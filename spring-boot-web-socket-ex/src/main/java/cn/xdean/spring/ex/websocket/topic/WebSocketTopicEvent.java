package cn.xdean.spring.ex.websocket.topic;

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
    String topic;

    String event;

    JsonNode payload;
}
