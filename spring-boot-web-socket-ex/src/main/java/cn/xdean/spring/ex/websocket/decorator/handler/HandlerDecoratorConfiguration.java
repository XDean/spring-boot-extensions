package cn.xdean.spring.ex.websocket.decorator.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerDecoratorConfiguration {
    @Bean
    @ConditionalOnSingleCandidate(ExceptionDecoratorFactory.ExceptionHandler.class)
    public ExceptionDecoratorFactory exceptionDecoratorFactory(ExceptionDecoratorFactory.ExceptionHandler exceptionHandler) {
        return new ExceptionDecoratorFactory(exceptionHandler);
    }
}
