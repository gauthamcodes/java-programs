package com.gautham.sourceconnector.utils;

import java.io.IOException;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class HttpUtils {
	private final static MediaType SCHEMA_CONTENT = MediaType.parse("application/vnd.schemaregistry.v1+json");
	private final static String SCHEMA_URL = "http://10.200.128.58:8081/";

	public static void fullConfig(String subjectName) throws IOException {
		final OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().put(RequestBody.create(SCHEMA_CONTENT, "{\"compatibility\": \"NONE\"}"))
				.url(SCHEMA_URL + "config/" + subjectName).build();
		System.out.println(client.newCall(request).execute().body().string());
	}
}
