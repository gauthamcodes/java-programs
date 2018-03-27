package com.chat.room.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class IncorrectPasswordException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public IncorrectPasswordException() {
		super("Incorrect password!");
	}
}
