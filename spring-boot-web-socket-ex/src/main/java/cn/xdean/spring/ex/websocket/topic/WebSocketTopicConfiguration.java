package cn.xdean.spring.ex.websocket.topic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketTopicConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "xdean.websocket.topic")
    public WebSocketTopicProperties webSocketTopicProperties() {
        return new WebSocketTopicProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(WebSocketTopic.class)
    public WebSocketTopicHandler webSocketTopicHandler() {
        return new WebSocketTopicHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
