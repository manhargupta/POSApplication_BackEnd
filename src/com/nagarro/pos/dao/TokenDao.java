package com.nagarro.pos.dao;

import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Token;

public interface TokenDao {

	Token getToken(String token) throws CustomException;

	boolean saveToken(Token token) throws CustomException;

	boolean removeToken(Token token) throws CustomException;

}
