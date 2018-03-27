package com.gautham.filevalidator.utils;

import com.gautham.filevalidator.model.Student;

public class ObjectUtil {
	public static Student toStudentModel(String data) {
		String[] arrayOfData = data.split(",");
		Student student = new Student();
		student.setId(Integer.valueOf(arrayOfData[0]));
		student.setFirstName(arrayOfData[1]);
		student.setLastName(arrayOfData[2]);
		student.setEmail(arrayOfData[3]);
		student.setGender(arrayOfData[4]);
		return student;
	}
}
