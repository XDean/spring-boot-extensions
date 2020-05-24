package cn.xdean.spring.ex.websocket;

import cn.xdean.spring.ex.websocket.decorator.handler.HandlerDecoratorConfiguration;
import cn.xdean.spring.ex.websocket.interceptor.WebSocketInterceptorConfiguration;
import cn.xdean.spring.ex.websocket.topic.WebSocketTopicConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import xdean.spring.auto.AutoSpringFactories;

@Configuration
@EnableWebSocket
@AutoSpringFactories(EnableAutoConfiguration.class)
@Import({
        XWebSocketConfigurer.class,
        WebSocketInterceptorConfiguration.class,
        WebSocketTopicConfiguration.class,
        HandlerDecoratorConfiguration.class,
})
public class XWebSocketAutoConfiguration {


}
