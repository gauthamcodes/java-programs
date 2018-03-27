package com.gautham.sourceconnector.utils;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
	public static boolean isNewValAdded(List<String> newArr, List<String> oldArr) {
		for(String newVal:newArr) {
			if(oldArr.indexOf(newVal) == -1) {
				return true;
			}
		}
		return false;
	}

    public static String fieldsClause(List<String> fields) {
        return fields.stream().collect(Collectors.joining(","));
    }

    public static String placeholderClause(List<String> fields) {
        return fields.stream().map(e -> "?").collect(Collectors.joining(","));
    }

	public static String updateClause(List<String> fields) {
		return fields.stream().map(e -> e + " = ?").collect(Collectors.joining(","));
	}

}
