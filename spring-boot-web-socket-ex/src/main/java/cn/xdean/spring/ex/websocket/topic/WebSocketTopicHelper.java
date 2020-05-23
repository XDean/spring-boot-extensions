package cn.xdean.spring.ex.websocket.topic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class WebSocketTopicHelper {
    @Autowired
    ObjectMapper objectMapper;

    public void sendEvent(WebSocketSession session, String topic, String event, Object payload) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(objectMapper.writer().writeValueAsString(WebSocketTopicEvent.builder()
                    .topic(topic)
                    .event(event)
                    .payload(objectMapper.convertValue(payload, JsonNode.class))
                    .build())));
        }
    }
}
