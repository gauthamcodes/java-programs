package com.azureblob.listener.model;

import java.io.File;
import java.util.Date;

import com.azureblob.listener.config.AzureConfig;
import com.azureblob.listener.utils.AzureUtils;

public class Record {
	private File file;
	private String path;
	private String container;
	private String directory;
	private Long timestamp;
	private Integer size;

	public Record() {

	}
	
	public Record(String path, String container, String directory) {
		this.path = path;
		this.container = container;
		this.directory = directory;
		this.timestamp = System.currentTimeMillis();
	}

	public Record(File file, String path, String container, String directory, Long timestamp) {
		this.file = file;
		this.path = path;
		this.container = container;
		this.directory = directory;
		this.timestamp = timestamp;
	}

	public File getFile() {
		return file;
	}

	public String getPath() {
		return path;
	}

	public String getContainer() {
		return container;
	}

	public String getDirectory() {
		return directory;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Report [file=" + file + ", path=" + path + ", container=" + container + ", directory=" + directory
				+ ", timestamp=" + timestamp + ", size=" + size + "]";
	}

	public String toCSV() {
		return file.getName() + "," + path + "," + container + "," + directory + ","
				+ AzureUtils.dateToString(new Date(), AzureConfig.DATE_FORMAT) + "," + size + "\n";
	}

}
