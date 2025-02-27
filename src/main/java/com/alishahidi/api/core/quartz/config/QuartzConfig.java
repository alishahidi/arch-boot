package com.alishahidi.api.core.quartz.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuartzConfig {

//    DataSource dataSource;
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() {
//        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//        factoryBean.setOverwriteExistingJobs(true);
//        factoryBean.setWaitForJobsToCompleteOnShutdown(true);
//        factoryBean.setAutoStartup(true);
//        factoryBean.setQuartzProperties(quartzProperties());
//        return factoryBean;
//    }
//
//
//
//    private Properties quartzProperties() {
//        Properties properties = new Properties();
//        properties.put("org.quartz.scheduler.instanceName", "QuartzCoreScheduler");
//        properties.put("org.quartz.threadPool.threadCount", "5");
//
//        // ✅ Ensure Quartz uses Spring Boot’s DataSource
//
//        // ✅ Use JDBC JobStore
//        properties.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
//        properties.put("org.quartz.jobStore.isClustered", "true");
//        properties.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
//
//        return properties;
//    }
}
