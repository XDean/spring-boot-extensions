package cn.xdean.spring.ex.websocket;

import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Collections;
import java.util.List;

public interface XWebSocketHandler extends WebSocketHandler {
    String path();

    default List<String> allowOrigins() {
        return Collections.singletonList("*");
    }

    @Nullable
    default HandshakeHandler getHandShakeHandler() {
        return null;
    }

    default List<HandshakeInterceptor> getHandshakeInterceptors() {
        return Collections.emptyList();
    }
}
