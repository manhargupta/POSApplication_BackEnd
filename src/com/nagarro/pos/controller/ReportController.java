package com.nagarro.pos.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;
import com.nagarro.pos.exception.CustomException;
import com.nagarro.pos.model.Employee;
import com.nagarro.pos.service.ReportService;
import com.nagarro.pos.validator.Validator;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Controller
@RequestMapping("/reports")
public class ReportController implements ServletContextAware {

	final Logger logger = Logger.getLogger(ReportController.class);

	@Autowired
	ReportService reportService;

	ServletContext ctx;

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @param startDate
	 * @param endDate
	 * @throws IOException
	 * @throws ParseException
	 *             Generate excel report
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public void generateReport(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate) throws IOException, ParseException {

		Date start = null;
		Date end = null;
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=myDoc.docx");
		try {
			Validator.validateField(endDate);
			Validator.validateField(startDate);
		} catch (final CustomException e2) {
			logger.error(e2);
		}
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			start = sdf.parse(startDate);
			end = sdf.parse(endDate);
		} catch (final Exception e2) {
			logger.error(e2);
		}

		final String absolutePath = ctx.getRealPath("WEB-INF\\resources");

		final File file = new File(absolutePath + "\\order.xlsx");
		final InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		try {
			reportService.excelGenerator(start, end, ((Employee) session.getAttribute(Constant.USER)).getId());

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/vnd.ms-excel";
			}

			response.setContentType(mimeType);

		} catch (final CustomException e) {
			logger.error(e);
		}
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		final InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		// Copy bytes from source to destination(outputstream in this example), closes
		// both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());

	}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.ctx = arg0;
	}

}