package com.gautham.avroserde;

import java.io.ByteArrayOutputStream;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;

public class AvroGenericSerDe {
	private final DatumReader<GenericRecord> datumReader;
	private final DatumWriter<GenericRecord> datumWriter;
	private Schema schema;

	public AvroGenericSerDe() {
		schema = null;
		datumReader = null;
		datumWriter = null;
		System.err.println("No File found!");
	}

	public AvroGenericSerDe(String fileName) {
		schema = SchemaParser.getSchema(fileName);
		datumReader = new GenericDatumReader<GenericRecord>(schema);
		datumWriter = new GenericDatumWriter<GenericRecord>(schema);
	}

	public GenericRecord deserialize(byte[] bytes) throws Exception {
		BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
		GenericRecord record = datumReader.read(null, decoder);
		return record;
	}

	public byte[] serialize(GenericRecord record) throws Exception {
		BinaryEncoder encoder = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		encoder = EncoderFactory.get().binaryEncoder(stream, encoder);
		datumWriter.write(record, encoder);
		encoder.flush();
		return stream.toByteArray();
	}
}
