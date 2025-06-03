package com.example.demo.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchExampleJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("BatchExampleJob start....");
		System.out.println("Do Something extraordinary 2...");
		log.info("BatchExampleJob end....");
	}

}
