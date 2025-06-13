package com.example.demo.publicApi.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.entities.UserInfo;
import com.example.demo.auth.services.AuthService;
import com.example.demo.kafka.service.KafkaProducerService;
import com.example.demo.report.service.JasperReportService;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicApiController {
	@Lazy
	@Autowired
	private KafkaProducerService service;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private JasperReportService report;

	@PostMapping("hello")
	public ResponseEntity<HashMap<String, String>> login(@RequestBody HashMap<String, String> request) {
		HashMap<String, String> response = new HashMap<String, String>();
		service.sendMessage("hello", "Hello World");
		response.put("data", "ok");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/sample", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateSampleReport() throws JRException, SQLException {
		Map<String, Object> params = new HashMap<>();
		params.put("title", "Sample Jasper Report");
		byte[] pdf = report.exportToPdf("sample", params);
		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=sample_report.pdf").body(pdf);
	}
	
	@GetMapping("users")
	public ResponseEntity<UserInfo> getUserInfoList() throws Exception {
		return ResponseEntity.ok().body(authService.getUserInfoList());
	}
}
