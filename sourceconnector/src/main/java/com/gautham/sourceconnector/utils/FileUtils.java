package com.gautham.sourceconnector.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

	public static String getLineByOffset(String filename, Long offset) throws IOException {
		return Files.readAllLines(Paths.get(filename)).get(offset.intValue());
	}

	public static Integer countLines(String filename) throws IOException {
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

	@SuppressWarnings("resource")
	public static String getFirstLine(String fileName) throws IOException {
		return new BufferedReader(new FileReader(fileName)).readLine();
	}

	public static void delete(String filename) {
		new File(filename).delete();
	}
}
