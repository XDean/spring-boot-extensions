package cn.xdean.spring.ex.task;

import cn.xdean.spring.ex.task.dao.XTaskLogRepository;
import cn.xdean.spring.ex.task.service.XTaskLogCleanupTask;
import cn.xdean.spring.ex.task.service.XTaskManager;
import cn.xdean.spring.ex.task.model.XTaskLogEntity;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xdean.spring.auto.AutoSpringFactories;

@Configuration
@EnableConfigurationProperties
@AutoSpringFactories(EnableAutoConfiguration.class)
@EntityScan(basePackageClasses = XTaskLogEntity.class)
public class XTaskConfiguration {
    @Bean
    @ConfigurationProperties("xdean.task")
    public XTaskProperties xdeanTaskProperties() {
        return new XTaskProperties();
    }

    @Bean
    @ConditionalOnMissingBean(XTaskService.class)
    @ConditionalOnBean(XTaskLogRepository.class)
    public XTaskService xTaskService() {
        return new XTaskManager();
    }

    @Bean
    @ConditionalOnBean(XTaskLogRepository.class)
    public XTaskLogCleanupTask xTaskLogCleanupTask() {
        return new XTaskLogCleanupTask();
    }
}
