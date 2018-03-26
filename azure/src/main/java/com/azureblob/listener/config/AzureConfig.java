package com.azureblob.listener.config;

import java.io.File;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.azureblob.listener.constant.Constants;
import com.azureblob.listener.utils.AzureUtils;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;

public class AzureConfig {
	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public static final String JSON_EXT = ".json";

	private static final String TEMP_FOLDER = "tmp\\";
	private static final String DOWNLOADED_REPORT = "downloaded-report.csv";
	private static final String CSV_HEADER = "File Name,Path,Container,Directory,Date,No. of Records\n";
	private static final String BUFFER_SIZE = "10";
	private static final String JSON_FOLDER = "json\\";

	private Integer bufferSize;
	private Integer total;
	private Integer limit;
	private Date lastModified;
	private List<String> downloaded;
	private List<String> directories;
	private String connection;
	private String container;
	private String local;
	private String temp;
	private String json;
	private String csv;
	private Boolean isComplete;

	public AzureConfig(Properties properties) {
		this.total = 0;
		this.bufferSize = Integer.valueOf(properties.getProperty(Constants.BUFFER_SIZE.getKey(), BUFFER_SIZE));
		this.limit = Integer.valueOf(properties.getProperty(Constants.RECORD_LIMIT.getKey(), "0"));
		if (this.limit != 0 && this.limit < bufferSize) {
			throw new IllegalArgumentException("Limit value can't be lesser than buffer size");
		}
		this.lastModified = AzureUtils.durationToDate(properties.getProperty(Constants.LAST_MODIFIED.getKey(), ""));
		this.downloaded = getDownloadedFromFile(properties);
		this.directories = Arrays.asList(properties.getProperty(Constants.AZURE_DIRECTORIES.getKey()).split(","));
		this.connection = properties.getProperty(Constants.AZURE_CONNECTION.getKey());
		this.container = properties.getProperty(Constants.AZURE_CONTAINER.getKey());
		this.local = properties.getProperty(Constants.LOCAL_PATH.getKey());
		this.temp = local + TEMP_FOLDER;
		this.json = local + JSON_FOLDER;
		this.csv = local + DOWNLOADED_REPORT;
		this.isComplete = false;
	}

	private List<String> getDownloadedFromFile(Properties properties) {
		String localPath = properties.getProperty(Constants.LOCAL_PATH.getKey());
		File file = new File(localPath + AzureConfig.DOWNLOADED_REPORT);
		if (file.exists() && file.isFile()) {
			return AzureUtils.readLinesFromCsv(file.getAbsolutePath()).stream().map(item -> item.split(",")[0])
					.collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

	public Integer getBufferSize() {
		return bufferSize;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getLimit() {
		return limit;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public List<String> getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(List<String> downloaded) {
		this.downloaded = downloaded;
	}

	public String getConnection() {
		return connection;
	}

	public String getContainer() {
		return container;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public List<String> getDirectories() {
		if (directories.size() > 0)
			return directories;
		else
			return Arrays.asList(new String[] { "" });
	}

	public void setDirectories(List<String> directories) {
		this.directories = directories;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getLocal() {
		return local;
	}

	public String getJson() {
		return json;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getCsv() {
		return csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}	

	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public CloudBlobDirectory getDirectory(String directoryName) {
		try {
			return CloudStorageAccount.parse(connection).createCloudBlobClient().getContainerReference(container)
					.getDirectoryReference(directoryName);
		} catch (InvalidKeyException | URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void incrementTotal() {
		this.total++;
	}

	public static String getCsvHeader() {
		return CSV_HEADER;
	}

}
