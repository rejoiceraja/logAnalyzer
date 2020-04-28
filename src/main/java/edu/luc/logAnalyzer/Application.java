package edu.luc.logAnalyzer;


import java.lang.management.ManagementFactory;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;


@Configuration
@EnableAutoConfiguration
@ComponentScan (basePackages="edu")
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebSecurity
@EnableAsync
@EnableScheduling
public class Application extends SpringBootServletInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	private static Class<Application> applicationClass = Application.class;
	
	public static void main(String[] args) {
		logger.info("Application starting: " + ManagementFactory.getRuntimeMXBean().getName());
		SpringApplication.run(Application.class);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
	/**
	 *  Courtesy of https://github.com/spring-projects/spring-boot/issues/1640 and
	 *  http://stackoverflow.com/questions/25957879/filter-order-in-spring-boot.
	 * 	Ensures that our MDC filter occurs after Spring Security's so that username is non-null 
	 * @param securityFilter
	 * @return    
	 */
	@Bean
	public FilterRegistrationBean securityFilterBean(@Qualifier(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME) Filter securityFilter) {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean(securityFilter);
	    registrationBean.setOrder(Integer.MAX_VALUE - 1);
	    registrationBean.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
	    return registrationBean;
	}

	// See above
	@Bean
	public FilterRegistrationBean userMDCBean() {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    UserMDCFilter userFilter = new UserMDCFilter();
	    registrationBean.setFilter(userFilter);
	    registrationBean.setOrder(Integer.MAX_VALUE);
	    return registrationBean;
	}

}
