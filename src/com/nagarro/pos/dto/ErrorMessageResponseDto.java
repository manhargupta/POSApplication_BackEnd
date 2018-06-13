package com.nagarro.pos.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorMessageResponseDto {

	public static ResponseEntity<Object> errorMessage(String eMsg) {
		final ErrorMessage em = new ErrorMessage();
		em.setErrorMessage(eMsg);
		em.setFlag(false);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(em);
	}

}
