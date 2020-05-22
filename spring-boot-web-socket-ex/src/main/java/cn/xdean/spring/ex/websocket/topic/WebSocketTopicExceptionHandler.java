package cn.xdean.spring.ex.websocket.topic;

import cn.xdean.spring.ex.websocket.decorator.handler.ExceptionDecoratorFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.Function;

public class WebSocketTopicExceptionHandler implements ExceptionDecoratorFactory.ExceptionHandler {

    @Autowired
    ObjectMapper objectMapper;

    @Setter
    Function<Exception, String> payloadFunc = e -> e.getMessage();

    @Override
    public void handle(WebSocketSession session, Exception exception) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(objectMapper.writer().writeValueAsString(WebSocketTopicEvent.builder()
                        .topic("error")
                        .event("throw")
                        .payload(objectMapper.convertValue(payloadFunc.apply(exception), JsonNode.class))
                        .build())));
            } catch (Throwable ex) {
                // ignore
            }
        }
    }
}
