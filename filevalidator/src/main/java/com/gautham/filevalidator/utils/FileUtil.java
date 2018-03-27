package com.gautham.filevalidator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.gautham.filevalidator.Config;

public class FileUtil {

	public static String getFileExtension(File file) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}

	public static Boolean isPatternMatch(String filename) {
		return Pattern.compile(Config.FILE_PATTERN, Pattern.CASE_INSENSITIVE).matcher(filename).matches();
	}

	public static List<String> read(String item) throws IOException {
		List<String> data = new ArrayList<>();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(item));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			data.add(line);
		}
		bufferedReader.close();
		return data;
	}
}
