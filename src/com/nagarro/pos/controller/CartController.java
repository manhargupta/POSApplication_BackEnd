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
import com.nagarro.pos.dto.ErrorMessageResponseDto;
import com.nagarro.pos.dto.cart.AddToCartDto;
import com.nagarro.pos.dto.cart.CartDeleteResponseDto;
import com.nagarro.pos.dto.cart.CartRequestDto;
import com.nagarro.pos.dto.cart.ProductIncDecDto;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Cart;
import com.nagarro.pos.model.CartProductMapper;
import com.nagarro.pos.model.Product;
import com.nagarro.pos.service.CartService;
import com.nagarro.pos.validator.Validator;

/**
 * @author manhargupta
 *
 *         Cart Controller
 */
@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Controller
@RequestMapping("/carts")
public class CartController {
	final Logger logger = Logger.getLogger(CustomerController.class);
	@Autowired
	CartService cartSevice;

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param pid
	 * @param custId
	 * @return DTO Object
	 * 
	 *         This method add a product to employee cart
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> addProductToCart(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam("pid") String pid, @RequestParam("custId") String custId) {
		Product product = null;
		try {
			Validator.validateFieldNumber(custId);
			Validator.validateFieldNumber(pid);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		try {
			product = cartSevice.addProductToCart(Integer.parseInt(pid), Integer.parseInt(custId), 1);
		} catch (final Exception e) {
			logger.error(2);
			return ErrorMessageResponseDto.errorMessage(e.getMessage());
		}

		return ResponseEntity.ok().body(new AddToCartDto(product, true));

	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param pid
	 * @param custId
	 * @return Product Increase Dto
	 * 
	 *         This method increases the product quantity added in the cart
	 */
	@RequestMapping(value = "/customer/{custId}/product/{pid}/inc", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Object> increaseQuantity(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("pid") String pid, @PathVariable("custId") String custId) {
		CartProductMapper cartProductMapper = null;
		final ProductIncDecDto productIncDecDto = new ProductIncDecDto();
		try {
			Validator.validateFieldNumber(custId);
			Validator.validateFieldNumber(pid);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		try {

			cartProductMapper = cartSevice.increaseQuantity(Integer.parseInt(custId), Integer.parseInt(pid));
			productIncDecDto.setCartProductMapper(cartProductMapper);
		} catch (final CustomException e) {
			logger.error(2);
			return ErrorMessageResponseDto.errorMessage(e.getMessage());
		}

		return ResponseEntity.ok().body(productIncDecDto);
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param pid
	 * @param custId
	 * @return Product Increase Dto
	 * 
	 *         This method decreases the product quantity added in the cart
	 */
	@RequestMapping(value = "/customer/{custId}/product/{pid}/dec", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Object> decreaseQuantity(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("pid") String pid, @PathVariable("custId") String custId) {
		CartProductMapper cartProductMapper = null;
		final ProductIncDecDto productIncDecDto = new ProductIncDecDto();

		try {
			Validator.validateFieldNumber(custId);
			Validator.validateFieldNumber(pid);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		try {
			cartProductMapper = cartSevice.decreaseQuantity(Integer.parseInt(custId), Integer.parseInt(pid));
			productIncDecDto.setCartProductMapper(cartProductMapper);
		} catch (final CustomException e) {
			logger.error(2);
			return ErrorMessageResponseDto.errorMessage(e.getMessage());
		}

		return ResponseEntity.ok().body(productIncDecDto);
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param custId
	 * @return Cart Delete DTO
	 * 
	 *         Clears the whole cart of the employee
	 */
	@RequestMapping(value = "/empty/{custId}", method = RequestMethod.DELETE)
	ResponseEntity<Object> emptyCart(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable("custId") String custId) {
		try {
			Validator.validateFieldNumber(custId);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		boolean flag = false;
		CartDeleteResponseDto dto = null;
		try {

			flag = cartSevice.removeCustomerCart(Integer.parseInt(custId));
			if (flag) {
				dto = new CartDeleteResponseDto("Cart Deleted Successfully", true);
			}
		} catch (final CustomException e) {
			logger.error(2);
			return ErrorMessageResponseDto.errorMessage(e.getMessage());
		}
		return ResponseEntity.ok().body(dto);
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param custId
	 * @return Customer Cart Dto
	 * 
	 *         return the cart of the customer
	 */
	@RequestMapping(value = "/customer/{custId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getCustometCart(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("custId") String custId) {
		try {
			Validator.validateFieldNumber(custId);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		Cart cart = null;
		try {
			cart = cartSevice.getCustomerCart(Integer.parseInt(custId));
		} catch (final CustomException e) {
			logger.error(2);
			return ErrorMessageResponseDto.errorMessage(e.getMessage());
		}

		return ResponseEntity.ok().body(new CartRequestDto(cart));
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param pid
	 * @param custId
	 * @return Cart Delete Dto
	 * 
	 *         Delete a product from the cart
	 */
	@RequestMapping(value = "/customer/{custId}/product/{pid}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteProduct(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable("pid") String pid, @PathVariable("custId") String custId) {
		try {
			Validator.validateFieldNumber(custId);
			Validator.validateFieldNumber(pid);
		} catch (final CustomException e2) {
			logger.error(e2);
			return ErrorMessageResponseDto.errorMessage(e2.getMessage());
		}
		boolean flag = false;
		CartDeleteResponseDto dto = null;
		try {
			flag = cartSevice.deleteProductFromCart(Integer.parseInt(custId), Integer.parseInt(pid));
			if (flag) {
				dto = new CartDeleteResponseDto("Product Deleted from Cart", true);
			}
		} catch (final Exception e) {
			logger.error(2);
			return ErrorMessageResponseDto.errorMessage(e.getMessage());
		}
		return ResponseEntity.ok().body(dto);

	}

}