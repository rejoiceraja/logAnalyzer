package edu.luc.logAnalyzer;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import edu.luc.logAnalyzer.service.UserService;


@Component
public class UserMDCFilter implements Filter {

	
	//Autowired
	//UserService userService;
	private UserService userService = new UserService();

	Logger logger = LoggerFactory.getLogger(UserMDCFilter.class);
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {


		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String uri = request.getRequestURI();


		String uvid = userService.getCurrentUserUvid();
		System.out.println("the UVID recevied in the USERMDCFILTER is "+uvid);

		if (uvid != null) {
			MDC.put("user", uvid);
		
			request.getSession().setAttribute("userName", uvid.toLowerCase());
		}
		
		try {
			chain.doFilter(req, res);
		} catch(IllegalStateException ise) {
			// This seems to only happen in WebLogic, and not sure what the consequences are...
			logger.error("Caught IllegalStateException in AuthFilter; problem with authorization?");
		} 
//		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {}

	public void destroy() {}
}
