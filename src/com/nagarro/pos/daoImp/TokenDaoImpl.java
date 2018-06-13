package com.nagarro.pos.daoImp;

import java.util.Properties;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dao.TokenDao;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Token;
import com.nagarro.pos.utilities.UserProperties;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Repository
public class TokenDaoImpl implements TokenDao {

	@Autowired
	private SessionFactory sessionFactory;
	static Properties prop = UserProperties.getProperties();

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Token getToken(String token) throws CustomException {
		Token token2 = null;
		final Session session = sessionFactory.getCurrentSession();
		try {
			final Query query = session.createQuery("from Token where token=:token");

			query.setParameter("token", token);
			token2 = (Token) query.getSingleResult();
		} catch (final Exception e) {
			throw new CustomException(prop.getProperty("GETTOKEN_DAO_EXCEP"));
		}
		return token2;
	}

	@Override
	public boolean saveToken(Token token) {
		boolean flag = false;
		try {
			getCurrentSession().save(token);
			flag = true;
		} catch (final Exception e) {
			flag = false;
		}

		return flag;
	}

	@Override
	public boolean removeToken(Token token) {
		boolean flag = false;
		try {
			getCurrentSession().delete(token);
			flag = true;
		} catch (final Exception e) {
			flag = false;
		}

		return flag;
	}

}
