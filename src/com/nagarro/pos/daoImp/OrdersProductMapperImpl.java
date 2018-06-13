package com.nagarro.pos.daoImp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dao.OrdersProductMapperDao;
import com.nagarro.pos.model.OrdersProductMapper;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Repository
public class OrdersProductMapperImpl implements OrdersProductMapperDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void removeOrderProductMapper(OrdersProductMapper ordersProductMapper) {
		getCurrentSession().remove(ordersProductMapper);

	}

}