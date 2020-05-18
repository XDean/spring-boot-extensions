package cn.xdean.spring.ex.websocket.topic;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketTopic {
    String topic();

    WebSocketTopicEventHandler create(WebSocketSession session);
}
