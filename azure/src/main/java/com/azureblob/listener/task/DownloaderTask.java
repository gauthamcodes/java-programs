package com.azureblob.listener.task;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azureblob.listener.config.AzureConfig;
import com.azureblob.listener.model.Record;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class DownloaderTask implements Runnable {

	private final static Logger log = LoggerFactory.getLogger(DownloaderTask.class);

	private List<Record> records;
	private AzureConfig config;

	public DownloaderTask(List<Record> records, AzureConfig config) {
		this.records = records;
		this.config = config;
	}

	@Override
	public void run() {
		log.info("Downloader task started!");
		config.getDirectories().stream().forEach(item -> {
			try {
				getBlobs(item);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	private void getBlobs(String directoryName) throws InterruptedException {
		synchronized (records) {
			CloudBlobDirectory cloudDirectory = config.getDirectory(directoryName);
			if (cloudDirectory != null) {
				try {
					for (ListBlobItem blobItem : cloudDirectory.listBlobs()) {
						if (blobItem.getClass().getSimpleName().equals("CloudBlockBlob")) {
							CloudBlob cloudBlob = (CloudBlob) blobItem;
							if (!config.getDownloaded().contains(cloudBlob.getName())
									&& (config.getLastModified() == null || config.getLastModified()
											.compareTo(cloudBlob.getProperties().getLastModified()) < 0)) {
								Record record = new Record(cloudBlob.getUri().toString(),
										cloudDirectory.getContainer().getName(), cloudDirectory.getPrefix());
								String fileNameWithPath = config.getTemp() + getChild(cloudBlob.getName());
								cloudBlob.downloadToFile(fileNameWithPath);
								record.setFile(new File(fileNameWithPath));
								records.add(record);
								config.incrementTotal();
								records.notifyAll();
							}
						} else {
							String path = blobItem.getUri().getRawPath();
							getBlobs(path.replace("/" + blobItem.getContainer().getName(), ""));
						}
						if (config.getLimit() != 0 && config.getTotal() == config.getLimit()) {
							break;
						}
						if (records.size() == config.getBufferSize()) {
							records.wait();
						}
					}
				} catch (IOException | StorageException | URISyntaxException e) {
					e.printStackTrace();
				} finally {
					config.setIsComplete(true);
					while (!records.isEmpty()) {
						records.wait();
					}
				}
			}
		}
	}

	private String getChild(String name) {
		String[] array = name.split("/");
		return array[array.length - 1];
	}
}
