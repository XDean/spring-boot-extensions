package cn.xdean.spring.ex.websocket.topic;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class WebSocketTopicEvent {
    String topic;

    String event;

    JsonNode payload;
}
