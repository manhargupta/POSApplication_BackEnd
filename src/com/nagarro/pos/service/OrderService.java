package com.nagarro.pos.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.constant.OrderStatus;
import com.nagarro.pos.constant.PaymentType;
import com.nagarro.pos.dao.CartDao;
import com.nagarro.pos.dao.CartProductMapperDao;
import com.nagarro.pos.dao.EmployeeDao;
import com.nagarro.pos.dao.OrderDao;
import com.nagarro.pos.dao.OrdersProductMapperDao;
import com.nagarro.pos.dto.Order_TotalPrice;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.CartProductMapper;
import com.nagarro.pos.model.CashDrawer;
import com.nagarro.pos.model.Customer;
import com.nagarro.pos.model.Employee;
import com.nagarro.pos.model.Orders;
import com.nagarro.pos.model.OrdersProductMapper;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Service
public class OrderService {

	@Autowired
	CustomerService customerService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	CartService cartService;

	@Autowired
	OrderDao iOrder;

	@Autowired
	CartDao iCart;

	@Autowired
	CartProductMapperDao iCartProductMapper;

	@Autowired
	OrdersProductMapperDao iOrdersProductMapper;

	@Autowired
	EmployeeDao iEmployee;

	final Logger logger = Logger.getLogger(OrderService.class);

	/**
	 * @param paymentType
	 * @param orderStatus
	 * @param custId
	 * @param empId
	 * @param orderId
	 * @return Order Object
	 * @throws CustomException
	 * 
	 *             save/place customer order and generate order id
	 */
	@Transactional(rollbackFor = Exception.class)
	public Orders saveOrder(PaymentType paymentType, OrderStatus orderStatus, int custId, int empId, String orderId)
			throws CustomException {
		float cartTotalPrice = 0;
		final Customer customer = customerService.getCustomerById(custId);
		if (customer == null) {
			throw new CustomException("Customer does not exist!");
		}
		final Employee employee = employeeService.getEmployeeById(empId);
		Orders orders = null;
		if (!orderId.equals("")) {
			orders = getSavedOrderById(orderId, empId);
			if (orders == null) {
				throw new CustomException("Order id does not exist!");
			}
		} else {
			orders = new Orders();
			orders.setCustomer(customer);
			orders.setEmployee(employee);
		}
		orders.setCreated(new Date());
		orders.setUpdated(new Date());
		orders.setOrderDate(new Date());
		orders.setPaymentType(paymentType);
		orders.setOrderStatus(orderStatus);
		if (customer.getCart() == null) {
			throw new CustomException("Cart is Empty!");
		}
		for (final CartProductMapper cartProductMapper : customer.getCart().getCartProductMapper()) {
			if (orderStatus.equals(OrderStatus.COMPLETE)) {
				final int updatedStock = cartProductMapper.getProduct().getStock() - cartProductMapper.getQuantity();
				if (updatedStock >= 0) {
					cartProductMapper.getProduct().setStock(updatedStock);
				} else {
					throw new CustomException(
							"stock is not avaible for product : " + cartProductMapper.getProduct().getName());
				}
			}
			final OrdersProductMapper newOrdersProductMapper = new OrdersProductMapper(orders,
					cartProductMapper.getProduct(), cartProductMapper.getQuantity());
			orders.getOrdersProductMappers().add(newOrdersProductMapper);
			cartProductMapper.getProduct().getOrdersProductMappers().add(newOrdersProductMapper);
			iOrder.addOrdersProductMappers(newOrdersProductMapper);
			if (orderStatus.equals(OrderStatus.COMPLETE)) {
				cartTotalPrice += cartProductMapper.getProduct().getPrice() * cartProductMapper.getQuantity();
			}
		}
		if (orderId.equals("")) {
			final Orders createdOrder = iOrder.addOrder(orders);
			employee.getOrder().add(createdOrder);
			customer.getOrder().add(createdOrder);
		}
		cartService.removeCustomerCart(custId);
		final CashDrawer cashDrawer = employee.getCashDrawer().get(employee.getCashDrawer().size() - 1);
		if (orderStatus.equals(OrderStatus.COMPLETE)) {
			cashDrawer.setEndBal(cashDrawer.getEndBal() + cartTotalPrice);
		}
		return orders;
	}

	/**
	 * @param empId
	 * @return Map of saved order
	 * @throws CustomException
	 * 
	 *             search all saved order of employee
	 */
	@Transactional
	public Map<String, List<Order_TotalPrice>> getSavedOrder(int empId) throws CustomException {
		final Employee currEmp = iEmployee.getEmployeeById(empId);
		if (currEmp.getOrder().isEmpty()) {
			throw new CustomException(Constant.ERROR_ORDER);
		}
		final Map<String, List<Order_TotalPrice>> sortedPendingOrders = new TreeMap<>(Collections.reverseOrder());
		final List<Orders> allOrders = currEmp.getOrder();
		for (final Orders currOrder : allOrders) {
			if (currOrder.getOrderStatus().equals(OrderStatus.PENDING)
					&& !currOrder.getOrdersProductMappers().isEmpty()) {
				final String date = currOrder.getOrderDate().toString().split(" ")[0];
				if (sortedPendingOrders.containsKey(date)) {
					sortedPendingOrders.get(date).add(new Order_TotalPrice(currOrder, getOrderTotalAmount(currOrder)));
				} else {
					sortedPendingOrders.put(date, new ArrayList<Order_TotalPrice>());
					sortedPendingOrders.get(date).add(new Order_TotalPrice(currOrder, getOrderTotalAmount(currOrder)));
				}
			}
		}
		return sortedPendingOrders;

	}

	/**
	 * @param empId
	 * @return Map of completed order list
	 * @throws CustomException
	 * 
	 *             search all complete order of employee
	 */
	@Transactional
	public Map<String, List<Order_TotalPrice>> getCompleteOrder(int empId) throws CustomException {
		final Employee currEmp = iEmployee.getEmployeeById(empId);
		if (currEmp.getOrder().isEmpty()) {
			throw new CustomException(Constant.ERROR_ORDER);
		}
		final Map<String, List<Order_TotalPrice>> sortedCompleteOrders = new TreeMap<>(Collections.reverseOrder());
		final List<Orders> allOrders = currEmp.getOrder();
		for (final Orders currOrder : allOrders) {
			if (currOrder.getOrderStatus().equals(OrderStatus.COMPLETE)) {
				final String date = currOrder.getOrderDate().toString().split(" ")[0];
				if (sortedCompleteOrders.containsKey(date)) {
					sortedCompleteOrders.get(date).add(new Order_TotalPrice(currOrder, getOrderTotalAmount(currOrder)));
				} else {
					sortedCompleteOrders.put(date, new ArrayList<Order_TotalPrice>());
					sortedCompleteOrders.get(date).add(new Order_TotalPrice(currOrder, getOrderTotalAmount(currOrder)));
				}
			}
		}
		return sortedCompleteOrders;
	}

	/**
	 * @param order
	 * @return order total amount
	 */
	public float getOrderTotalAmount(Orders order) {
		float total = 0;
		for (final OrdersProductMapper ordersProductMapper : order.getOrdersProductMappers()) {
			total += (ordersProductMapper.getProduct().getPrice() * ordersProductMapper.getQuantity());
		}
		return total + 10;

	}

	/**
	 * @param orderId
	 * @param empId
	 * @return saved order object
	 * @throws CustomException
	 */
	@Transactional
	public Orders getSavedOrderById(String orderId, int empId) throws CustomException {
		final Employee currEmp = iEmployee.getEmployeeById(empId);
		Orders order = null;
		if (currEmp.getOrder() == null) {
			throw new CustomException(Constant.ERROR_ORDER);
		}
		final List<Orders> allOrders = currEmp.getOrder();
		for (final Orders currOrder : allOrders) {
			if (currOrder.getOrderStatus().equals(OrderStatus.PENDING)
					&& currOrder.getOrderId().equalsIgnoreCase(orderId)) {
				order = currOrder;
				break;
			}
		}
		return order;
	}

	/**
	 * @param empId
	 * @return Map of all orders of Employee
	 * @throws CustomException
	 */
	@Transactional
	public Map<String, List<Order_TotalPrice>> getAllOrders(int empId) throws CustomException {

		final Employee currEmp = iEmployee.getEmployeeById(empId);
		if (currEmp.getOrder().isEmpty()) {
			throw new CustomException(Constant.ERROR_ORDER);
		}
		final Map<String, List<Order_TotalPrice>> sortedOrders = new TreeMap<>(Collections.reverseOrder());
		final List<Orders> ordersList = currEmp.getOrder();
		for (final Orders currOrder : ordersList) {
			if (!currOrder.getOrdersProductMappers().isEmpty()) {
				final String date = currOrder.getOrderDate().toString().split(" ")[0];
				if (sortedOrders.containsKey(date)) {
					sortedOrders.get(date).add(new Order_TotalPrice(currOrder, getOrderTotalAmount(currOrder)));
				} else {
					sortedOrders.put(date, new ArrayList<Order_TotalPrice>());
					sortedOrders.get(date).add(new Order_TotalPrice(currOrder, getOrderTotalAmount(currOrder)));
				}
			}
		}

		return sortedOrders;
	}

	@Transactional
	public Orders reloadSavedOrder(String orderId, int empId) throws CustomException {
		final Orders savedOrder = getSavedOrderById(orderId, empId);
		if (savedOrder == null) {
			throw new CustomException(Constant.ERROR_ORDER);
		}
		for (final OrdersProductMapper ordersProductMapper : savedOrder.getOrdersProductMappers()) {
			cartService.addProductToCart(ordersProductMapper.getProduct().getId(), savedOrder.getCustomer().getId(),
					ordersProductMapper.getQuantity());
			iOrdersProductMapper.removeOrderProductMapper(ordersProductMapper);

		}
		return savedOrder;

	}

}
