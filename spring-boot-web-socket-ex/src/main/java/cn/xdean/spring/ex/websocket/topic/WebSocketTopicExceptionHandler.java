package cn.xdean.spring.ex.websocket.topic;

import cn.xdean.spring.ex.websocket.decorator.handler.ExceptionDecoratorFactory;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.Function;

public class WebSocketTopicExceptionHandler implements ExceptionDecoratorFactory.ExceptionHandler {

    @Autowired WebSocketTopicHelper helper;

    @Setter
    Function<Exception, String> payloadFunc = e -> e.getMessage();

    @Override
    public void handle(WebSocketSession session, Exception exception) {
        if (session.isOpen()) {
            try {
                helper.sendEvent(session, "error", "throw", payloadFunc.apply(exception));
            } catch (Throwable ex) {
                // ignore
            }
        }
    }
}
