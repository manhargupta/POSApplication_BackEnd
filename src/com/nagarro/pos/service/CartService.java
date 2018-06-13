package com.nagarro.pos.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dao.CartDao;
import com.nagarro.pos.dao.CartProductMapperDao;
import com.nagarro.pos.dao.CustomerDao;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Cart;
import com.nagarro.pos.model.CartProductMapper;
import com.nagarro.pos.model.Customer;
import com.nagarro.pos.model.Product;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Service
public class CartService {

	final Logger logger = Logger.getLogger(CartService.class);

	@Autowired
	CartDao iCart;

	@Autowired
	ProductService productService;

	@Autowired
	CustomerDao iCustomer;

	@Autowired
	CartProductMapperDao iCartProductMapper;

	/**
	 * @param pid
	 * @param custId
	 * @param quantity
	 * @return Product Object
	 * @throws CustomException
	 * 
	 *             add product to employee cart
	 */
	@Transactional(rollbackFor = Exception.class)
	public Product addProductToCart(int pid, int custId, int quantity) throws CustomException {

		Product product = null;
		try {
			product = productService.getProductById(pid);
			if (product == null) {
				throw new CustomException("Product does not exist!");
			}
			final Customer customer = iCustomer.getCustomerById(custId);
			if (customer == null) {
				throw new CustomException("Customer does not exist!");
			}
			Cart newCartData = customer.getCart();
			if (newCartData == null) {
				newCartData = new Cart();
			}
			final CartProductMapper existingProduct = newCartData.getCartProductMapper().stream()
					.filter(p -> p.getProduct().getId() == pid).findAny().orElse(null);
			if (existingProduct != null) {
				existingProduct.setQuantity(existingProduct.getQuantity() + 1);
				newCartData.setUpdated(new Date());
			} else {

				final CartProductMapper cartProductMapper = new CartProductMapper();

				newCartData.setCustomer(customer);

				newCartData.getCartProductMapper().add(cartProductMapper);
				product.getCartProductMapper().add(cartProductMapper);

				cartProductMapper.setCart(newCartData);
				cartProductMapper.setProduct(product);
				cartProductMapper.setQuantity(quantity);

				customer.setCart(newCartData);

				customer.setCreated(new Date());
				customer.setUpdated(new Date());
				newCartData.setCreated(new Date());
				newCartData.setUpdated(new Date());

				iCart.addProductToCart(newCartData);
				iCart.addCartProducttoMapper(cartProductMapper);
			}

		} catch (final Exception e) {
			logger.error(e);
			throw new CustomException(e.getMessage());
		}
		return product;
	}

	/**
	 * @param cart
	 * @return boolean
	 * @throws Exception
	 * 
	 *             remove customer cart
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean removeCustomerCart(Cart cart) throws Exception {

		return iCart.removeCart(cart);

	}

	/**
	 * @param custId
	 * @return boolean
	 * @throws CustomException
	 * 
	 *             remove customer cart by id
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean removeCustomerCart(int custId) throws CustomException {
		final Customer customer = iCustomer.getCustomerById(custId);

		if (customer.getCart() == null) {
			throw new CustomException("No cart Exist for this customer");
		}

		final Cart cart = customer.getCart();
		cart.setCustomer(null);
		customer.setCart(null);
		for (final CartProductMapper cartProductMapper : cart.getCartProductMapper()) {
			iCartProductMapper.removeCartProductMapper(cartProductMapper);
		}

		return iCart.removeCart(cart);
	}

	/**
	 * @param custId
	 * @param pid
	 * @return Cart Product Mapper Object
	 * @throws CustomException
	 * 
	 *             inc product quantity
	 */
	@Transactional
	public CartProductMapper increaseQuantity(int custId, int pid) throws CustomException {

		CartProductMapper cartProductMapper = null;
		try {

			final Customer customer = iCustomer.getCustomerById(custId);
			final int customerCartId = customer.getCart().getId();
			cartProductMapper = iCart.increaseQuantity(customerCartId, pid);
		} catch (final CustomException e) {
			logger.error(e);
			e.printStackTrace();
			throw new CustomException(e.getMessage());
		}

		return cartProductMapper;
	}

	/**
	 * @param custId
	 * @param pid
	 * @return Cart Product mapper
	 * @throws CustomException
	 * 
	 *             dec product quantity
	 */
	@Transactional
	public CartProductMapper decreaseQuantity(int custId, int pid) throws CustomException {

		CartProductMapper cartProductMapper = null;
		try {
			final Customer customer = iCustomer.getCustomerById(custId);
			final int customerCartId = customer.getCart().getId();
			cartProductMapper = iCart.decreaseQuantity(customerCartId, pid);
		} catch (final CustomException e) {
			logger.error(e);
			e.printStackTrace();
			throw new CustomException(e.getMessage());
		}
		return cartProductMapper;
	}

	/**
	 * @param custId
	 * @return Cart Object
	 * @throws CustomException
	 * 
	 *             get customer Cart
	 */
	@Transactional
	public Cart getCustomerCart(int custId) throws CustomException {
		Cart cart = null;
		try {
			final Customer customer = iCustomer.getCustomerById(custId);
			cart = customer.getCart();
			if (cart == null) {
				throw new CustomException("No Cart Exist for this Customer");
			}
		} catch (final CustomException e) {
			logger.error(e);
			throw new CustomException(e.getMessage());
		}

		return cart;

	}

	/**
	 * @param custId
	 * @param pid
	 * @return boolean
	 * @throws CustomException
	 * 
	 *             delete product from employee cart
	 */
	@Transactional
	public boolean deleteProductFromCart(int custId, int pid) throws CustomException {
		Cart cart = null;
		try {
			final Customer customer = iCustomer.getCustomerById(custId);
			cart = customer.getCart();
			if (cart == null) {
				throw new CustomException("No Cart Exist for this Customer");
			}

			final CartProductMapper prod = cart.getCartProductMapper().stream()
					.filter(p -> p.getProduct().getId() == pid).findAny().orElse(null);

			if (prod == null) {
				throw new CustomException("Product does not exist in the cart");
			}

			iCartProductMapper.removeCartProductMapper(prod);

		} catch (final CustomException e) {
			logger.error(e);
			throw new CustomException(e.getMessage());
		}

		return true;
	}
}
