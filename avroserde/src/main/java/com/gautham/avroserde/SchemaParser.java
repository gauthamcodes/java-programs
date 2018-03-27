package com.gautham.avroserde;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;

public class SchemaParser {
	public static Schema getSchema(String fileName) {
		Schema schema = null;
		try {
			schema = new Schema.Parser().parse(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return schema;
	}
}
