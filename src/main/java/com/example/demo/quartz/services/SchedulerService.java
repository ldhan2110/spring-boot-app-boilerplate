package com.example.demo.quartz.services;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.quartz.jobs.BatchExampleJob;
import com.example.demo.quartz.utils.QuartzUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SchedulerService {
	@Autowired
	private Scheduler scheduler;

	@PostConstruct
	public void init() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	@PreDestroy
	public void preDestroy() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	public boolean schedule(Class jobclass) {
		String jobName = "Hello Quartz";
		JobDetail jobDetail = QuartzUtils.builJobDetail(jobclass, jobName);
		Trigger trigger = QuartzUtils.buildTriggerCron(jobDetail, jobName, "0/5 * * * * ?");
		if (trigger == null) {
			log.error("Not triggered");
			return false;
		}

		try {
			boolean jobExists = scheduler.checkExists(new TriggerKey(jobName));
			if (jobExists) {
				scheduler.rescheduleJob(new TriggerKey(jobName), trigger);
				return true;
			}
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException ex) {
			log.error(ex.getMessage(), ex);
			return false;
		}
		return true;
	}
}
