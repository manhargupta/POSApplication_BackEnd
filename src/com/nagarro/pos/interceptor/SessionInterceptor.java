package com.nagarro.pos.interceptor;

import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.model.Employee;
import com.nagarro.pos.service.TokenAuth;
import com.nagarro.pos.utilities.UserProperties;

/**
 * @author manhargupta Interceptor, intercept each request to check the token
 *         and if token exist then only pass the request
 */
@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {
	static Properties prop = UserProperties.getProperties();

	@Autowired
	TokenAuth tokenAuth;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getRequestURI().contains("/POSApplication/employees/nosession")) {
			return true;
		} else if (request.getRequestURI().contains("/POSApplication/employees/logout")) {
			return true;
		}

		if (request.getMethod().equalsIgnoreCase("options") || request.getMethod().equalsIgnoreCase("delete")
				|| request.getMethod().equalsIgnoreCase("put")) {
			return true;
		}
		if (request.getRequestURI().equalsIgnoreCase("/POSApplication/employees")
				&& request.getMethod().equalsIgnoreCase("POST")) {
			return true;
		}

		String token = null;
		final Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			response.sendRedirect(Constant.ERROR_PATH);
			return false;
		}
		for (final Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase("x-token")) {
				token = cookie.getValue();
				break;
			}
		}
		if (request.getRequestURI().contains(Constant.ERROR_PATH)) {
			return true;
		} else if (request.getRequestURI().equalsIgnoreCase("/POSApplication/employees")
				&& request.getMethod().equalsIgnoreCase("POST")) {
			return true;
		} else if (token == null) {
			response.sendRedirect(Constant.ERROR_PATH);
			return false;
		} else {
			final Employee employee = tokenAuth.checkAuth(token);
			if (employee == null) {
				response.sendRedirect(Constant.ERROR_PATH);
				return false;
			} else {
				final HttpSession session = request.getSession();
				session.setAttribute(Constant.USER, employee);
				return true;
			}
		}
	}

}
