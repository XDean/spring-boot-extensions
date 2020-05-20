package cn.xdean.spring.ex.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Collections;
import java.util.List;

@Configuration
@ConditionalOnBean(WebSocketProvider.class)
public class WebSocketProviderConfigurer implements WebSocketConfigurer {

    @Autowired(required = false) List<WebSocketProvider> providers = Collections.emptyList();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        providers.forEach(p -> registry
                .addHandler(p, p.path())
                .setAllowedOrigins(p.allowOrigins().toArray(new String[0]))
                .setHandshakeHandler(p.getHandShakeHandler())
                .addInterceptors(p.getHandshakeInterceptors().toArray(new HandshakeInterceptor[0])));
    }
}