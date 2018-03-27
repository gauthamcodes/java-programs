package com.gautham.sourceconnector.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class BlobUtils {

	private static final String DEFAULT_FILE_PATH = "C:\\Users\\Gautham.Manivannan\\Project\\AscendLearning\\";
	private static final String FILE_PATTERN = "([^\\s]+(\\.(?i)(csv))$)";

	public static void upload(String fileName, String path)
			throws InvalidKeyException, URISyntaxException, StorageException, FileNotFoundException, IOException {
		File source = new File(path + fileName);
		AzureConnector.getContainer().getBlockBlobReference(fileName).upload(new FileInputStream(source),
				source.length());
	}

	public static List<String> move(String filePath) throws InvalidKeyException, URISyntaxException, StorageException {
		String finalPath = (filePath == null) ? DEFAULT_FILE_PATH : filePath;
		return list().stream().map(item -> {
			try {
				return save(item, finalPath);
			} catch (FileNotFoundException | URISyntaxException | StorageException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

	public static void delete() throws InvalidKeyException, URISyntaxException, StorageException {
		list().forEach(item -> {
			if (item instanceof CloudBlob) {
				CloudBlob cloudBlob = (CloudBlob) item;
				try {
					cloudBlob.delete();
				} catch (StorageException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static List<ListBlobItem> list() throws InvalidKeyException, URISyntaxException, StorageException {
		
		return StreamSupport.stream(AzureConnector.getContainer().listBlobs().spliterator(), false)
				.filter(item -> Pattern.compile(FILE_PATTERN).matcher(item.getUri().toString()).matches())
				.collect(Collectors.toList());
	}

	public static String save(ListBlobItem blobItem, String filePath)
			throws URISyntaxException, FileNotFoundException, StorageException {
		String localFilePath = "";
		if (blobItem instanceof CloudBlob) {
			CloudBlob cloudBlob = (CloudBlob) blobItem;
			localFilePath = filePath + cloudBlob.getName();
			cloudBlob.download(new FileOutputStream(localFilePath));
		}
		return localFilePath;
	}
}
