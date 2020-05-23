package cn.xdean.spring.ex.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Collections;
import java.util.List;

@Configuration
public class XWebSocketConfigurer implements WebSocketConfigurer {

    @Autowired(required = false) List<XWebSocketHandler> providers = Collections.emptyList();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        providers.forEach(p -> registry
                .addHandler(p.decorate(), p.path())
                .setAllowedOrigins(p.allowOrigins().toArray(new String[0]))
                .setHandshakeHandler(p.getHandShakeHandler())
                .addInterceptors(p.getHandshakeInterceptors().toArray(new HandshakeInterceptor[0])));
    }
}
