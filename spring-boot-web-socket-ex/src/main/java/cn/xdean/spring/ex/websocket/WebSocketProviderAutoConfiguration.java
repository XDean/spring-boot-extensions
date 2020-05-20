package cn.xdean.spring.ex.websocket;

import cn.xdean.spring.ex.websocket.interceptor.WebSocketInterceptorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xdean.spring.auto.AutoSpringFactories;

@Configuration
@AutoSpringFactories(EnableAutoConfiguration.class)
@Import({
        WebSocketProviderConfigurer.class,
        WebSocketInterceptorConfiguration.class
})
public class WebSocketProviderAutoConfiguration {


}
