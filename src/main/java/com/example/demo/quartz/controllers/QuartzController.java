package com.example.demo.quartz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.quartz.jobs.BatchExampleJob;
import com.example.demo.quartz.services.SchedulerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/job/")
public class QuartzController {
	@Autowired
	SchedulerService service;

	@GetMapping("run")
	public ResponseEntity<?> runJob() {
		try {
			service.schedule(BatchExampleJob.class);
			return ResponseEntity.ok("OK, Batch has been run");
		} catch (Exception e) {
			log.error("error when try call runJob", e);
			return ResponseEntity.ok("Internal Server Error");
		}
	}

}
