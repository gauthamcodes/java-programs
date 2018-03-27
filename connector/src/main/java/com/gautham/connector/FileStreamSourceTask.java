package com.gautham.connector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileStreamSourceTask extends SourceTask {
	private static final Logger log = LoggerFactory.getLogger(FileStreamSourceTask.class);
	public static final String FILENAME_FIELD = "filename";
	public static final String POSITION_FIELD = "position";

	private String filename;
	private InputStream stream;
	private String topic = null;

	private Long streamOffset = 0L;
	private Schema schema;
	private List<String> header;

	@Override
	public String version() {
		return new FileStreamSourceConnector().version();
	}

	@Override
	public void start(Map<String, String> props) {
		filename = props.get(FileStreamSourceConnector.FILE_CONFIG);
		try {
			schema = getSchema(getFirstLine(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("Task file name = {}", filename);
		if (filename == null || filename.isEmpty()) {
			stream = System.in;
		}
		topic = props.get(FileStreamSourceConnector.TOPIC_CONFIG);
		if (topic == null)
			throw new ConnectException("FileStreamSourceTask config missing topic setting");
	}

	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		int totalLines = 0;
		try {
			totalLines = countLines(filename);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (streamOffset < (totalLines - 1)) {
			log.info("stream offset = {}, total lines = {}", streamOffset, totalLines);
			Struct struct = null;
			try {
				struct = getStruct(schema, getLineByOffset(filename, streamOffset + 1));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (schema != null && struct != null) {
				ArrayList<SourceRecord> records = new ArrayList<>();
				records.add(new SourceRecord(offsetKey(filename), offsetValue(streamOffset), topic, schema, struct));
				streamOffset++;
				return records;
			}
		}
		return null;
	}

	private Struct getStruct(Schema schema, String line) {
		Struct struct = new Struct(schema);
		for(int i=0;i<header.size();i++) {
			struct.put(header.get(i), line.split(",")[i]);
		}
		return struct;
	}

	private String getLineByOffset(String filename, Long offset) throws IOException {
		return Files.readAllLines(Paths.get(filename)).get(offset.intValue());
	}

	private Schema getSchema(String headerLine) {
		SchemaBuilder keySchemaBuilder = SchemaBuilder.struct().name(filename);
		//keySchemaBuilder.field("id", Schema.INT16_SCHEMA);
		header = new ArrayList<>();
		for (String name : headerLine.split(",")) {
			log.info("column name = {}", name);
			header.add(name);
			keySchemaBuilder.field(name, Schema.STRING_SCHEMA);
		}
		return keySchemaBuilder.build();
	}

	@SuppressWarnings("resource")
	private String getFirstLine(String fileName) throws IOException {
		return new BufferedReader(new FileReader(fileName)).readLine();
	}

	private int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	@Override
	public void stop() {
		log.info("Stopping");
		synchronized (this) {
			try {
				if (stream != null && stream != System.in) {
					stream.close();
					log.trace("Closed input stream");
				}
			} catch (IOException e) {
				log.error("Failed to close FileStreamSourceTask stream: ", e);
			}
			this.notify();
		}
	}

	private Map<String, String> offsetKey(String filename) {
		return Collections.singletonMap(FILENAME_FIELD, filename);
	}

	private Map<String, Long> offsetValue(Long pos) {
		return Collections.singletonMap(POSITION_FIELD, pos);
	}
}