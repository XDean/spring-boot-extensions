package cn.xdean.spring.ex.task;

import cn.xdean.spring.ex.task.dao.XTaskLogRepository;
import cn.xdean.spring.ex.task.handler.XTaskLogCleanupTask;
import cn.xdean.spring.ex.task.handler.XTaskManager;
import cn.xdean.spring.ex.task.model.XTaskLogEntity;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.RepositoryDefinition;
import xdean.spring.auto.AutoSpringFactories;

@Configuration
@EnableConfigurationProperties
@AutoSpringFactories(EnableAutoConfiguration.class)
@EntityScan(basePackageClasses = XTaskLogEntity.class)
@EnableJpaRepositories(basePackageClasses = XTaskLogRepository.class)
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
