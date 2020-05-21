package cn.xdean.spring.ex.websocket.decorator.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

public class ExceptionDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    public interface ExceptionHandler {
        void handle(WebSocketSession session, Exception e);
    }

    final ExceptionHandler exceptionHandler;

    public ExceptionDecoratorFactory(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new ExceptionDecorator(handler);
    }

    private class ExceptionDecorator extends WebSocketHandlerDecorator {
        public ExceptionDecorator(WebSocketHandler delegate) {
            super(delegate);
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            try {
                super.afterConnectionEstablished(session);
            } catch (Exception e) {
                exceptionHandler.handle(session, e);
            }
        }

        @Override
        public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
            try {
                super.handleMessage(session, message);
            } catch (Exception e) {
                exceptionHandler.handle(session, e);
            }
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            try {
                super.handleTransportError(session, exception);
            } catch (Exception e) {
                exceptionHandler.handle(session, e);
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
            try {
                super.afterConnectionClosed(session, closeStatus);
            } catch (Exception e) {
                exceptionHandler.handle(session, e);
            }
        }
    }
}
