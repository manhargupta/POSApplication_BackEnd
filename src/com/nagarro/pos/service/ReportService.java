package com.nagarro.pos.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dao.EmployeeDao;
import com.nagarro.pos.dao.OrderDao;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Employee;
import com.nagarro.pos.model.Orders;
import com.nagarro.pos.model.OrdersProductMapper;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Service
public class ReportService implements ServletContextAware {

	@Autowired
	EmployeeDao iEmployee;

	@Autowired
	OrderDao iorder;

	final Logger logger = Logger.getLogger(ReportService.class);

	ServletContext ctx;

	/**
	 * @param start
	 * @param end
	 * @param empId
	 * @throws CustomException
	 * @throws IOException
	 * @throws ParseException
	 */
	@Transactional
	public void excelGenerator(Date start, Date end, int empId) throws CustomException, IOException, ParseException {

		List<Orders> allOrders = new ArrayList<>();
		Employee employee = null;
		if (empId == -1) {
			allOrders = iorder.getAllOrders();
		} else {
			allOrders = iEmployee.getEmployeeById(empId).getOrder();
			employee = iEmployee.getEmployeeById(empId);
		}

		FileOutputStream fileOut = null;
		final String absolutePath = ctx.getRealPath("WEB-INF\\resources");

		final String filename = absolutePath + "\\order.xlsx";
		try (FileOutputStream file = new FileOutputStream(new File(filename))) {

			final XSSFWorkbook hwb = new XSSFWorkbook();
			final XSSFSheet sheet = hwb.createSheet("Payment & Sales Summary");
			final Row rowhead = sheet.createRow((short) 0);
			rowhead.createCell((short) 0).setCellValue("Employee ID");
			rowhead.createCell((short) 1).setCellValue("Employee Name");
			rowhead.createCell((short) 2).setCellValue("Email");
			rowhead.createCell((short) 3).setCellValue("Mobile");
			final Row rowheadDetail = sheet.createRow((short) 1);
			rowheadDetail.createCell((short) 0).setCellValue(employee.getId());
			rowheadDetail.createCell((short) 1)
					.setCellValue(employee.getFirstName().concat(" " + employee.getLastName()));
			rowheadDetail.createCell((short) 2).setCellValue(employee.getEmail());
			rowheadDetail.createCell((short) 3).setCellValue(employee.getMobile());

			final Row orderHead = sheet.createRow((short) 3);
			orderHead.createCell((short) 0).setCellValue("Order ID");
			orderHead.createCell((short) 1).setCellValue("Customer Name");
			orderHead.createCell((short) 2).setCellValue("Order Date");
			orderHead.createCell((short) 3).setCellValue("Order Status");
			orderHead.createCell((short) 4).setCellValue("Payment Type");
			orderHead.createCell((short) 5).setCellValue("Total Order Payment");

			int i = 4;
			long employeeAllOrdersTotal = 0;

			for (final Orders order : allOrders) {
				int orderSum = 0;
				final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				final String orderDate = dateFormat.format(order.getOrderDate());
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final Date dateOfOrder = sdf.parse(orderDate);
				for (final OrdersProductMapper ordersProductMapper : order.getOrdersProductMappers()) {
					orderSum += (ordersProductMapper.getProduct().getPrice() * ordersProductMapper.getQuantity());
				}
				if (dateOfOrder.compareTo(end) <= 0 && dateOfOrder.compareTo(start) >= 0) {
					final Row row = sheet.createRow(i++);
					row.createCell(0).setCellValue(order.getOrderId());
					row.createCell(1).setCellValue(
							order.getCustomer().getFirstName().concat(" " + order.getCustomer().getLastName()));
					row.createCell(2).setCellValue(order.getOrderDate().toGMTString());
					row.createCell(3).setCellValue(order.getOrderStatus().toString());
					row.createCell(4).setCellValue(order.getPaymentType().toString());
					row.createCell(5).setCellValue(orderSum);
					employeeAllOrdersTotal += orderSum;
				}
			}
			final Row row = sheet.createRow(++i);
			row.createCell(4).setCellValue("Orders Grand Total");
			row.createCell(5).setCellValue(employeeAllOrdersTotal);
			fileOut = new FileOutputStream(filename);
			hwb.write(file);
			fileOut.close();
		}
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.ctx = arg0;

	}
}