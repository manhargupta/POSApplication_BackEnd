package com.nagarro.pos.utilities;

import java.security.SecureRandom;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;

/**
 * @author manhargupta
 *
 *         generate unique token for the user
 */
@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
public class TokenUtil {

	protected static SecureRandom random = new SecureRandom();

	public static synchronized String generateToken(String username) {
		final long longToken = Math.abs(random.nextLong());
		final String random = Long.toString(longToken, 16);
		return (username + ":" + random);
	}
}