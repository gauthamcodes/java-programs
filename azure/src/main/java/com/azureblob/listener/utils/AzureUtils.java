package com.azureblob.listener.utils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AzureUtils {
	public static Properties loadProperties() {
		Properties properties = new Properties();
		try (InputStreamReader in = new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"),
				"UTF-8")) {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static void appendLineToFile(String content, String fileName) {
		try (FileWriter fw = new FileWriter(fileName, true)) {
			fw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeJsonToFile(String jsonStr, String fileName) {
		try (FileWriter fw = new FileWriter(fileName)) {
			fw.write(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> readLinesFromCsv(String fileName) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
			br.readLine();
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return lines;
	}

	public static String dateToString(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static Date stringToDate(String dateStr, String format) {
		Date date = null;
		DateFormat df = new SimpleDateFormat(format);
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date durationToDate(String duration) {
		if (duration != "") {
			Calendar cal = Calendar.getInstance();
			String[] array = duration.split(" ");
			for (String item : array) {
				if (item.contains("d")) {
					cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf("-" + item.replace("d", "")));
				} else if (item.contains("h")) {
					cal.add(Calendar.HOUR_OF_DAY, Integer.valueOf("-" + item.replace("h", "")));
				} else if (item.contains("m")) {
					cal.add(Calendar.MONTH, Integer.valueOf("-" + item.replace("m", "")));
				} else {
					continue;
				}
			}
			return new Date(cal.getTimeInMillis());
		} else {
			return null;
		}
	}

	public static Boolean deleteFile(Path path) {
		Boolean isDone = false;
		try {
			isDone = Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isDone;
	}
}
