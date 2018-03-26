package com.azureblob.listener;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azureblob.listener.config.AzureConfig;
import com.azureblob.listener.constant.Constants;
import com.azureblob.listener.model.Record;
import com.azureblob.listener.task.DeserializerTask;
import com.azureblob.listener.task.DownloaderTask;
import com.azureblob.listener.utils.AzureUtils;

public class AzureInitializer {
	private final static Logger log = LoggerFactory.getLogger(AzureInitializer.class);
	private Long timestamp;
	private List<Record> recordBuffer;
	private AzureConfig config;

	public AzureInitializer() {
		this.timestamp = System.currentTimeMillis();
		this.recordBuffer = new ArrayList<>();
		this.config = new AzureConfig(validateProperties(AzureUtils.loadProperties()));
	}

	public static void main(String[] args) {
		log.info("Azure blob storage avro record deserializer");
		log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		AzureInitializer initializer = new AzureInitializer();
		initializer.validateDirectories();
		initializer.start();
	}

	private void start() {
		Thread azureBlobTask = new Thread(new DownloaderTask(recordBuffer, config), "Downloader");
		Thread deserializerTask = new Thread(new DeserializerTask(recordBuffer, config), "Deserializer");
		azureBlobTask.start();
		deserializerTask.start();
		try {
			Runtime.getRuntime().addShutdownHook(new OnComplete());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Record> getRecordBuffer() {
		return recordBuffer;
	}

	public void setRecordBuffer(List<Record> recordBuffer) {
		this.recordBuffer = recordBuffer;
	}

	private Properties validateProperties(Properties properties) {
		for (Constants constant : Constants.values()) {
			if (constant.isMandatory() && !properties.containsKey(constant.getKey())) {
				throw new InvalidParameterException(
						MessageFormat.format("Missing mandatory property value for key - {0}!", constant.getKey()));
			}
		}
		return properties;
	}

	private void validateDirectories() {
		log.info("Verifying the directory");
		if (new File(config.getLocal()).exists()) {
			File tmp = new File(config.getTemp());
			if (tmp.exists() && tmp.isDirectory()) {
				for (File file : tmp.listFiles()) {
					file.delete();
				}
			} else {
				if (tmp.mkdirs()) {
					log.info("Temp folder got created successfully!");
				}
			}
			File json = new File(config.getJson());
			if (!json.exists()) {
				if (json.mkdirs()) {
					log.info("Json folder got created successfully!");
				}
			}
		} else {
			if (new File(config.getLocal()).mkdirs() && new File(config.getTemp()).mkdirs()
					&& new File(config.getJson()).mkdirs()) {
				log.info("Root, Temp and Json folder got created successfully!");
			}
			try {
				File csvFile = new File(config.getCsv());
				csvFile.createNewFile();
				AzureUtils.appendLineToFile(AzureConfig.getCsvHeader(), csvFile.getAbsolutePath());
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

	public class OnComplete extends Thread {
		public void run() {
			Long seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - timestamp);
			log.info("Total time taken to complete the process {}min {}sec ", seconds / 60, seconds % 60);
			log.info("Total records processed {}", config.getTotal());
		}
	}
}
