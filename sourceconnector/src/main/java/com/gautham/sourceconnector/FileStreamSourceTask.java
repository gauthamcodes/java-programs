package com.gautham.sourceconnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.gautham.sourceconnector.config.SourceConfig;
import com.gautham.sourceconnector.utils.FileUtils;

public class FileStreamSourceTask extends SourceTask {
	private static final Logger log = LoggerFactory.getLogger(FileStreamSourceTask.class);
	private static final Integer POLL_TIMEOUT = 1000;

	private String fileName;
	private String topicName;
	private String schemaName;
	private Long streamOffset = 0L;
	private Schema schema;
	private List<String> header;

	@Override
	public String version() {
		return new FileStreamSourceConnector().version();
	}

	@Override
	public void start(Map<String, String> props) {
		log.info("starting the task");
		fileName = props.get(SourceConfig.FILE_CONFIG);
		topicName = props.get(SourceConfig.TOPIC_CONFIG);
		schemaName = props.get(SourceConfig.SCHEMA_CONFIG);
		log.info("Task file name = {}, topic name = {}", fileName, topicName);
		if (fileName == null || fileName.isEmpty()) {
			throw new ConnectException("File name not found!");
		} else {
			if (new File(fileName).exists()) {
				try {
					streamOffset = getCurrentOffset(fileName);
					schema = getSchema(FileUtils.getFirstLine(fileName));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (topicName == null) {
			throw new ConnectException("FileStreamSourceTask config missing topic setting");
		}
	}

	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		// check if file exists
		if (new File(fileName).exists()) {
			// get the total lines in the file
			int totalLines = 0;
			try {
				totalLines = FileUtils.countLines(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info("file name {}, total lines {}, stream offset {}", fileName, totalLines, streamOffset);
			if (streamOffset < (totalLines - 1)) {
				log.info("stream offset = {}, total lines = {}", streamOffset, totalLines);
				if (schema != null) {
					Struct struct = null;
					try {
						struct = getStruct(schema, FileUtils.getLineByOffset(fileName, streamOffset + 1));
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (struct != null) {
						ArrayList<SourceRecord> records = new ArrayList<>();
						records.add(new SourceRecord(offsetKey(fileName), offsetValue(streamOffset), topicName, schema,
								struct));
						streamOffset++;
						return records;
					}
				}
			} else {
				FileUtils.delete(fileName);
				log.info("{} got deleted because it's processed!", fileName);
			}
		} else {
			log.info("Waiting for new version of the file {} to be created", fileName);
			synchronized (this) {
				this.wait(POLL_TIMEOUT);
			}
		}
		return null;
	}

	private Struct getStruct(Schema schema, String line) {
		Struct struct = new Struct(schema);
		for (int i = 0; i < header.size(); i++) {
			struct.put(header.get(i), line.split(",")[i]);
		}
		return struct;
	}

	private Schema getSchema(String headerLine) {
		SchemaBuilder keySchemaBuilder = SchemaBuilder.struct().name(schemaName);
		header = new ArrayList<>();
		for (String name : headerLine.split(",")) {
			log.info("column name = {}", name);
			header.add(name);
			keySchemaBuilder.field(name, Schema.STRING_SCHEMA);
		}
		return keySchemaBuilder.build();
	}

	@Override
	public void stop() {
		log.info("Stopping");
		synchronized (this) {
			log.info("Stopped");
			this.notify();
		}
	}

	private Long getCurrentOffset(String filename) throws IOException {
		@SuppressWarnings("resource")
		InputStream stream = new FileInputStream(filename);
		Map<String, Object> offset = context.offsetStorageReader().offset(offsetKey(filename));
		Long lastRecordedOffset = null;
		if (offset != null) {
			lastRecordedOffset = (Long) offset.get("position");
			if (lastRecordedOffset != null) {
				long skipLeft = (Long) lastRecordedOffset;
				while (skipLeft > 0) {
					try {
						long skipped = stream.skip(skipLeft);
						skipLeft -= skipped;
					} catch (IOException e) {
						log.info("Error while trying to seek to previous offset in file: ", e);
						throw new ConnectException(e);
					}
				}
				lastRecordedOffset++;
			}
		}
		stream.close();
		return (lastRecordedOffset != null) ? lastRecordedOffset : 0L;
	}

	private Map<String, String> offsetKey(String filename) {
		return Collections.singletonMap(SourceConfig.FILENAME_FIELD, filename);
	}

	private Map<String, Long> offsetValue(Long pos) {
		return Collections.singletonMap(SourceConfig.POSITION_FIELD, pos);
	}
}