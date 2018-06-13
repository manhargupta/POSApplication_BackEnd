package com.nagarro.pos.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dao.EmployeeDao;
import com.nagarro.pos.dao.TokenDao;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Employee;
import com.nagarro.pos.model.Token;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Service
public class TokenService {

	final Logger logger = Logger.getLogger(TokenService.class);

	@Autowired
	TokenDao iTokenDao;

	@Autowired
	EmployeeDao iEmployeeDao;
	public static final long HOUR = 3600 * 1000;

	/**
	 * @param token
	 * @return Employee Object basis on token
	 * @throws CustomException
	 */
	@Transactional
	public Employee getEmployeeByToken(String token) throws CustomException {
		final Token dbToken = iTokenDao.getToken(token);
		if (new Date().compareTo(dbToken.getExpiryDateTime()) > 0) {
			return null;
		}
		return iEmployeeDao.getEmployeeById(dbToken.getEmpId());
	}

	/**
	 * @param token
	 * @param empId
	 * @return boolean
	 * @throws CustomException
	 * 
	 *             save generated token to db
	 */
	@Transactional
	public boolean saveToken(String token, int empId) throws CustomException {

		return iTokenDao.saveToken(new Token(token, empId, new Date(), new Date(new Date().getTime() + 2 * HOUR)));
	}

	/**
	 * @param token
	 * @return boolean
	 * @throws CustomException
	 * 
	 *             delete token from db
	 */
	@Transactional
	public boolean deleteToken(Token token) throws CustomException {
		return iTokenDao.removeToken(token);
	}

}
