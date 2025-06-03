package com.example.demo.quartz.config;

import javax.sql.DataSource;

import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfig {

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	SpringBeanJobFactory springBeanJobFactory() {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, DataSource dataSource) throws Exception {
		try {
			SchedulerFactoryBean factory = new SchedulerFactoryBean();
			factory.setConfigLocation(new ClassPathResource("/quartz.properties"));
			factory.setOverwriteExistingJobs(true);
			factory.setJobFactory(jobFactory);
			factory.setDataSource(dataSource);
			factory.setWaitForJobsToCompleteOnShutdown(true);
			factory.afterPropertiesSet();
			return factory;
		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();
			throw new RuntimeException("Error initializing Quartz Scheduler", e);
		}
	}
}
