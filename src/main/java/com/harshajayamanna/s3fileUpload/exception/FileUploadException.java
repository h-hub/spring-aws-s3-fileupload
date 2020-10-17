package com.harshajayamanna.s3fileUpload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileUploadException extends RuntimeException {
	public FileUploadException(String s) {
	}
}
