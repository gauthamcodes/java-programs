package com.gautham.azure;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Test {

	public static void main(String[] args) {
		File file = new File(
				"C:\\Users\\Gautham.Manivannan\\Personal\\nha-student-activity-part-1-Off-206-1510299942869");
		StringBuilder jsonString = new StringBuilder();
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		JsonParser jp = new JsonParser();
		try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file,
				new GenericDatumReader<GenericRecord>());
				PrintWriter out = new PrintWriter("C:\\Users\\Gautham.Manivannan\\Personal\\test.json");) {
			jsonString.append("[");
			GenericRecord record = null;
			while (dataFileReader.hasNext()) {
				record = dataFileReader.next(record);
				jsonString.append(getJsonString(record) + ",");
			}
			jsonString.deleteCharAt(jsonString.length() - 1);
			jsonString.append("]");
			JsonElement je = jp.parse(jsonString.toString());
			out.write(gson.toJson(je));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getJsonString(GenericRecord record) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JsonEncoder encoder = EncoderFactory.get().jsonEncoder(record.getSchema(), os);
		DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>();
		if (record instanceof SpecificRecord) {
			writer = new SpecificDatumWriter<GenericRecord>();
		}
		writer.setSchema(record.getSchema());
		writer.write(record, encoder);
		encoder.flush();
		String jsonString = new String(os.toByteArray(), Charset.forName("UTF-8"));
		os.close();
		return jsonString;
	}

}
