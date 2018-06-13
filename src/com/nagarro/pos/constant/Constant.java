package com.nagarro.pos.constant;

/**
 * constants file
 */
@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
public class Constant {
	private Constant() {
	}

	public static final String USER = "emp";
	public static final String CREATION_DATE = "17/05/2018";
	public static final String AUTHOR = "Manhar Gupta";
	public static final String ERROR_PATH = "/POSApplication/employees/nosession";
	public static final String ERROR_ORDER = "No Orders exist!";

}
