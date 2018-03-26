package com.azureblob.listener.constant;

public enum Constants {
	LOCAL_PATH("local.path", true), 
	AZURE_CONNECTION("azure.connection", true), 
	AZURE_CONTAINER("azure.container", true), 
	AZURE_DIRECTORIES("azure.directories", true), 
	BUFFER_SIZE("buffer.size", false), 
	LAST_MODIFIED("lastmodified.duration", false),
	RECORD_LIMIT("record.limit", false);

	private String key;
	private Boolean mandatory;

	Constants(String key, Boolean mandatory) {
		this.key = key;
		this.mandatory = mandatory;
	}

	public String getKey() {
		return this.key;
	}

	public Boolean isMandatory() {
		return this.mandatory;
	}
}
