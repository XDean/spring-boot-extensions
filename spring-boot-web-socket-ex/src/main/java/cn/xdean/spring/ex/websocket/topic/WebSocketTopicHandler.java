package cn.xdean.spring.ex.websocket.topic;

import cn.xdean.spring.ex.websocket.XWebSocketHandler;
import cn.xdean.spring.ex.websocket.decorator.handler.ExceptionDecoratorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketTopicHandler extends TextWebSocketHandler implements XWebSocketHandler {

    @Autowired WebSocketTopicProperties properties;
    @Autowired ObjectMapper objectMapper;
    @Autowired ExceptionDecoratorFactory exceptionDecoratorFactory;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired @Order List<WebSocketTopic> topics;

    final Map<WebSocketSession, Map<String, WebSocketTopicEventHandler>> handlers = new ConcurrentHashMap<>();

    @Override
    public String path() {
        return properties.getPath();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WebSocketTopicEvent event = objectMapper.readerFor(WebSocketTopicEvent.class).readValue(message.getPayload());
        WebSocketTopicEventHandler handler = handlers.computeIfAbsent(session, s -> new ConcurrentHashMap<>())
                .computeIfAbsent(event.getTopic(), t -> topics.stream()
                        .filter(e -> e.topic().equals(event.getTopic()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("no such topic: " + event.getTopic()))
                        .create(session));
        handler.handleEvent(session, event.getEvent(), new WebSocketPayloadHolder() {
            @Override
            public <T> T getPayload(Class<T> type) throws IOException {
                try {
                    return objectMapper.readerFor(type).readValue(event.getPayload());
                } catch (IOException e) {
                    throw new IOException("Fail to read payload as type: " + type, e);
                }
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        handlers.remove(session);
    }

    @Override
    public WebSocketHandler decorate() {
        return exceptionDecoratorFactory.decorate(this);
    }
}