package com.nagarro.pos.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dto.ErrorMessageResponseDto;
import com.nagarro.pos.dto.ProductListDto;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.service.ProductService;
import com.nagarro.pos.validator.Validator;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Controller
@RequestMapping(value = "/products")
public class ProductController {

	final Logger logger = Logger.getLogger(OrderController.class);

	@Autowired
	ProductService productService;

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @return list of all products
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getProducts(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {

		return ResponseEntity.ok().body(new ProductListDto(productService.getProducts()));
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param toSearch
	 * @return list of searched products
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> searchProducts(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam("search") String toSearch) {

		try {
			Validator.validateField(toSearch);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		return ResponseEntity.ok().body(new ProductListDto(productService.searchProducts(toSearch)));
	}
}
