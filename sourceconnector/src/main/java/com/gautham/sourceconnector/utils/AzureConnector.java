package com.gautham.sourceconnector.utils;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

public class AzureConnector {
	private static final String CONNECTION_STRING = "DefaultEndpointsProtocol=http;"
			+ "AccountName=kafkaconnectdev;"
			+ "AccountKey=p1PORQflw3j06J9O71dHOL1enftC3t6u2b67yS7to7S7EWNrllaHm8NT5qD/52c3Bilgkc8EFWm/Xk1/E2nx1Q==";

	private static final String CONTAINER_NAME = "activityevents/";

	public static CloudBlobContainer getContainer() throws InvalidKeyException, URISyntaxException, StorageException {
		return CloudStorageAccount.parse(CONNECTION_STRING).createCloudBlobClient()
				.getContainerReference(CONTAINER_NAME);
	}
}
