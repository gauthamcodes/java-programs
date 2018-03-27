package com.gautham.filevalidator.model;

import java.util.Date;

public class ProcessedFiles {
	private Integer id;
	private String fileName;
	private Date processedOn;
	private Boolean isValid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getProcessedOn() {
		return processedOn;
	}

	public void setProcessedOn(Date processedOn) {
		this.processedOn = processedOn;
	}

}
