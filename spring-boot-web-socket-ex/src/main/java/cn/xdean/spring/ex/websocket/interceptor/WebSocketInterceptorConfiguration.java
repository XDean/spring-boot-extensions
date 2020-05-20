package cn.xdean.spring.ex.websocket.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

@Configuration
public class WebSocketInterceptorConfiguration {
    @Bean
    @ConditionalOnBean(ConversionService.class)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public PathTemplateInterceptor pathTemplateInterceptor(ConversionService conversionService) {
        return new PathTemplateInterceptor(conversionService);
    }
}
