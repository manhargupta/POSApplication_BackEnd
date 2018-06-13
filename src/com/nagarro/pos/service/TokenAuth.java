package com.nagarro.pos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Employee;

@Component
public class TokenAuth {

	@Autowired
	TokenService tokenService;

	/**
	 * @param token
	 * @return Employee object is authorization success
	 * @throws CustomException
	 */
	public Employee checkAuth(String token) throws CustomException {
		return tokenService.getEmployeeByToken(token);
	}

}
