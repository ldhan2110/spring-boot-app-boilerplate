package com.example.demo.report.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class JasperReportService {
	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private DataSource dataSource;

	private JasperReport getReport(String reportName) throws JRException {
		try {
			Resource resource = resourceLoader.getResource("classpath:reports/" + reportName + ".jrxml");
			JasperReport compiledReport = JasperCompileManager.compileReport(resource.getInputStream());
			return compiledReport;
		} catch (Exception e) {
			throw new JRException("[ERROR][JasperReport]: Failed to load or compile report: " + reportName, e);
		}
	}

	public byte[] exportToPdf(String reportName, Map<String, Object> parameters) throws JRException, SQLException {
		try (Connection connection = dataSource.getConnection()) {
			parameters.put("REPORT_CONNECTION", connection);
			JasperReport report = this.getReport(reportName);
			JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
			return JasperExportManager.exportReportToPdf(print);
		}
	}
}
