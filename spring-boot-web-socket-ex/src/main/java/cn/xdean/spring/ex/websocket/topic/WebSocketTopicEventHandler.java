package cn.xdean.spring.ex.websocket.topic;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketTopicEventHandler {
    void handleEvent(WebSocketSession session, String event, WebSocketPayloadHolder holder);
}
