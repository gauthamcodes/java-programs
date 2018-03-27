package com.gautham.connector.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

	private AzureConnector connector = new AzureConnector();
	private static final String defaultFilePath = "/tmp/gautham-files/";
	public String filePath = defaultFilePath;

	public void upload(String fileName)
			throws InvalidKeyException, URISyntaxException, StorageException, FileNotFoundException, IOException {
		File source = new File(filePath + fileName);
		connector.getContainer().getBlockBlobReference(fileName).upload(new FileInputStream(source), source.length());
	}

	public List<ListBlobItem> list() throws InvalidKeyException, URISyntaxException, StorageException {
		return StreamSupport.stream(connector.getContainer().listBlobs().spliterator(), false)
				.filter(item -> Pattern.compile("([^\\s]+(\\.(?i)(csv))$)").matcher(item.getUri().toString()).matches())
				.collect(Collectors.toList());
	}

	public String save(ListBlobItem blobItem) throws URISyntaxException, FileNotFoundException, StorageException {
		String localFilePath = "";
		if (blobItem instanceof CloudBlob) {
			CloudBlob cloudBlob = (CloudBlob) blobItem;
			localFilePath = filePath + cloudBlob.getName();
			cloudBlob.download(new FileOutputStream(localFilePath));
		}
		return localFilePath;
	}

	public void read(String item) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(item));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}
		bufferedReader.close();
	}

	public String getFileName() throws InvalidKeyException, URISyntaxException, StorageException {
		return list().stream().map(item -> {
			try {
				return save(item);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (StorageException e) {
				e.printStackTrace();
			}
			return null;
		}).findFirst().orElse(null);
	}
}
