package cn.xdean.spring.ex.task;

import cn.xdean.spring.ex.task.dao.XTaskLogRepository;
import cn.xdean.spring.ex.task.handler.XTaskLogCleanupTask;
import cn.xdean.spring.ex.task.handler.XTaskManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xdean.spring.auto.AutoSpringFactories;

@Configuration
@EnableConfigurationProperties
@AutoSpringFactories(EnableAutoConfiguration.class)
@Import(XTaskLogRepository.class)
public class XTaskConfiguration {
    @Bean
    @ConfigurationProperties("xdean.task")
    public XTaskProperties xdeanTaskProperties() {
        return new XTaskProperties();
    }

    @Bean
    @ConditionalOnMissingBean(XTaskService.class)
    public XTaskService xTaskService() {
        return new XTaskManager();
    }

    @Bean
    public XTaskLogCleanupTask xTaskLogCleanupTask() {
        return new XTaskLogCleanupTask();
    }
}
