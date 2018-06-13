package com.nagarro.pos.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dto.CustomerSearchDto;
import com.nagarro.pos.dto.ErrorMessageResponseDto;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Customer;
import com.nagarro.pos.service.CustomerService;
import com.nagarro.pos.validator.Validator;

/**
 * @author manhargupta
 *
 *         Customer Controller
 */
@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Controller
@RequestMapping(value = "/customer")
public class CustomerController {
	final Logger logger = Logger.getLogger(CustomerController.class);

	@Autowired
	CustomerService customerService;

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param toSearch
	 * @return List of searched customer
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> searchCustomerList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam(value = "search", required = true) String toSearch) {
		try {
			Validator.validateField(toSearch);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		return ResponseEntity.ok().body(customerService.getCustomers(toSearch));
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param custId
	 * @return Customer Dto
	 * 
	 *         find customer by id
	 */
	@RequestMapping(value = "/{custId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getCustomerById(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("custId") String custId) {
		try {
			Validator.validateField(custId);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		final Customer customer = customerService.getCustomerById(Integer.parseInt(custId));
		return ResponseEntity.ok().body(new CustomerSearchDto(customer.getId(), customer.getFirstName(),
				customer.getLastName(), customer.getEmail(), customer.getMobile()));
	}

}