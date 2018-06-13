package com.nagarro.pos.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.dto.CashDrawerDto;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.CashDrawer;
import com.nagarro.pos.model.Employee;
import com.nagarro.pos.service.EmployeeService;
import com.nagarro.pos.utilities.UserProperties;

/**
 * @author manhargupta
 *
 */
@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Controller
@RequestMapping(value = "/cashdrawer")
public class CashDrawerController {

	final Logger logger = Logger.getLogger(CashDrawerController.class);
	static Properties prop = UserProperties.getProperties();

	@Autowired
	EmployeeService employeeService;

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @return Employee Cash Drawer DTO
	 * @throws ParseException
	 * @throws CustomException
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getCashDrawer(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ParseException, CustomException {
		final Employee emp = employeeService.getEmployeeById(((Employee) session.getAttribute("emp")).getId());
		final Map<String, List<CashDrawer>> cashDrawerMap = new HashMap<>();
		final List<CashDrawer> cashDrawerList = emp.getCashDrawer();
		for (final CashDrawer cashDrawer : cashDrawerList) {
			final String date = cashDrawer.getDate().toString().split(" ")[0];
			if (cashDrawerMap.containsKey(date)) {
				cashDrawerMap.get(date).add(cashDrawer);
			} else {
				cashDrawerMap.put(date, new ArrayList<CashDrawer>());
				cashDrawerMap.get(date).add(cashDrawer);
			}
		}
		final TreeMap<String, List<CashDrawer>> sortedCashDrawer = new TreeMap<>();
		sortedCashDrawer.putAll(cashDrawerMap);
		return ResponseEntity.ok().body(new CashDrawerDto(sortedCashDrawer, true));
	}

}
