package com.gautham.filevalidator;

public class Config {
	public static final String PROCESSED_FILE_TOPIC = "files-processed";
	public static final String STUDENT_TOPIC = "student-x1";
	public static final String UNKNOWN_TOPIC = "unknown-data";
	
	public static final String FILE_PATTERN = "data.*";
	public static final String FILE_DIRECTORY = "C:\\Users\\Gautham.Manivannan\\Project\\AscendLearning\\tmp\\";
	public static final String FILE_TYPE = "csv";
	
	public static final String BOOTSTRAP_SERVER = "bootstrap.servers";
	public static final String KEY_SERIALIZER = "key.serializer";
	public static final String VALUE_SERIALIZER = "value.serializer";
	public static final String TOPIC_NAME = "topic.name";
}
