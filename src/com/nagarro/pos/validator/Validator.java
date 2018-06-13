package com.nagarro.pos.validator;

import java.util.Properties;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.constant.OrderStatus;
import com.nagarro.pos.constant.PaymentType;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.utilities.UserProperties;

/*
 * Validator class used for validating the input.
 */
@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
public class Validator {
	static Properties prop = UserProperties.getProperties();

	private Validator() {
	}

	/**
	 * @param field
	 * @throws CustomException
	 * 
	 *             validate field entered by user
	 */
	public static void validateField(String field) throws CustomException {
		if (field == null || field.isEmpty()) {
			throw new CustomException(prop.getProperty("EXCEP_FIELD_EMPTY"));
		}
	}

	/**
	 * @param paymode
	 * @throws CustomException
	 * 
	 *             validate payment mode
	 */
	public static void validatePaymode(String paymode) throws CustomException {
		boolean flag = false;
		for (final PaymentType paymentType : PaymentType.values()) {
			if (paymentType.name().equals(paymode.toUpperCase())) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new CustomException(prop.getProperty("UNKOWN_PAYMODE"));
		}
	}

	/**
	 * @param orderStatus
	 * @throws CustomException
	 * 
	 *             validate order status
	 */
	public static void validateOrderStatus(String orderStatus) throws CustomException {

		boolean flag = false;
		for (final OrderStatus orderStatus2 : OrderStatus.values()) {
			if (orderStatus2.name().equals(orderStatus.toUpperCase())) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new CustomException(prop.getProperty("UNKOWN_ORDERSTATUS"));
		}
	}

	/**
	 * @param field
	 * @throws CustomException
	 * 
	 *             validate field field, should be number
	 */
	public static void validateFieldNumber(String field) throws CustomException {
		float startBal = 0;
		try {
			if (field == null || field.isEmpty()) {
				throw new CustomException(prop.getProperty("EXCEP_FIELD_EMPTY"));
			}
			startBal = Float.parseFloat(field);
			if (startBal < 0) {
				throw new CustomException(prop.getProperty("START_BAL_ERROR"));
			}
		} catch (final NumberFormatException e) {
			throw new CustomException(prop.getProperty("NUMBER_FORMAT_EXCEP"));
		}

	}

}
