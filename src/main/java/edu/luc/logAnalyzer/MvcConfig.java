package edu.luc.logAnalyzer;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;


/**
 * Configuration class for mapping ViewControllers for URIs that don't have an explicit
 * controller mapping
 * 
 * @author rjebamalaidass
 */
@Configuration
@EnableWebMvc
@EnableWebSecurity
public class MvcConfig implements WebMvcConfigurer {
	
	
	  private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
	            "classpath:/META-INF/resources/", "classpath:/resources/",
	            "classpath:/static/"," classpath:/templates/", "classpath:/public/" };

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/**")
	            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	    }
	    

	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/login").setViewName("login");
    
      registry.addViewController("/home").setViewName("home");
      registry.addViewController("/result").setViewName("result");
     
      
   
  }
	


  
    
    
//	
	// Courtesy of http://stackoverflow.com/a/30245409/959368.  Fixes an issue preventing Spring RestControllers
	// from returning plain strings
	@Override
	public void configureMessageConverters(
	        List<HttpMessageConverter<?>> converters) {
	    // put the jackson converter to the front of the list so that application/json content-type strings will be treated as JSON
	    converters.add(new MappingJackson2HttpMessageConverter());
	    // and probably needs a string converter too for text/plain content-type strings to be properly handled
	    converters.add(new StringHttpMessageConverter());
	    converters.add(new ResourceHttpMessageConverter());
	}
	
	
    @Bean
    public FormattingConversionService conversionService() {
        DefaultFormattingConversionService conversionService = 
          new DefaultFormattingConversionService(false);
 
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        registrar.registerFormatters(conversionService);
 
        // other desired formatters
 
        return conversionService;
}
}
