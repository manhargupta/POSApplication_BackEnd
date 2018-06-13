package com.nagarro.pos.daoImp;

import java.util.Properties;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dao.CartDao;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Cart;
import com.nagarro.pos.model.CartProductMapper;
import com.nagarro.pos.utilities.UserProperties;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Repository
public class CartDaoImpl implements CartDao {

	static Properties prop = UserProperties.getProperties();

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public boolean addProductToCart(Cart cart) {
		boolean flag = false;
		try {
			getCurrentSession().save(cart);
			flag = true;
		} catch (final Exception e) {
			flag = false;
		}

		return flag;

	}

	@Override
	public boolean addCartProducttoMapper(CartProductMapper cartProductMapper) {
		boolean flag = false;
		try {
			getCurrentSession().save(cartProductMapper);
			flag = true;
		} catch (final Exception e) {
			flag = false;
		}

		return flag;

	}

	@Override
	public boolean removeCart(Cart cart) {
		getCurrentSession().remove(cart);
		return true;
	}

	@Override
	public Cart getCartById(int custId) {
		return getCurrentSession().get(Cart.class, custId);
	}

	@SuppressWarnings("deprecation")
	@Override
	public CartProductMapper increaseQuantity(int cartId, int pid) throws CustomException {

		CartProductMapper cartProductMapper = null;
		try {
			cartProductMapper = (CartProductMapper) getCurrentSession()
					.createQuery("from CartProductMapper where productId = :pid and cartId = :cartId")
					.setInteger("pid", pid).setInteger("cartId", cartId).getSingleResult();

			if (cartProductMapper == null) {
				throw new CustomException(prop.getProperty("CART_INC_EXCEP"));
			}
			cartProductMapper.setQuantity(cartProductMapper.getQuantity() + 1);
			getCurrentSession().update(cartProductMapper);

		}catch (NoResultException e) {
			throw new CustomException("No product found for query");
		}
		catch (final CustomException e) {
			throw new CustomException(e.getMessage());
		}
		return cartProductMapper;
	}

	@SuppressWarnings("deprecation")
	@Override
	public CartProductMapper decreaseQuantity(int cartId, int pid) throws CustomException {

		CartProductMapper cartProductMapper = null;
		try {
			cartProductMapper = (CartProductMapper) getCurrentSession()
					.createQuery("from CartProductMapper where productId = :pid and cartId = :cartId")
					.setInteger("pid", pid).setInteger("cartId", cartId).getSingleResult();

			if (cartProductMapper == null) {
				throw new CustomException(prop.getProperty("CART_DEC_EXCEP"));
			}

			if (cartProductMapper.getQuantity() == 1) {
				throw new CustomException(prop.getProperty("CART_BELOW_1_EXCEP"));
			}

			cartProductMapper.setQuantity(cartProductMapper.getQuantity() - 1);
			getCurrentSession().update(cartProductMapper);

		} catch (final CustomException e) {
			throw new CustomException(e.getMessage());
		}
		return cartProductMapper;
	}

}