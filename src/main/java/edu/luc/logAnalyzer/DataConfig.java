package edu.luc.logAnalyzer;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for mapping the application datasource (in production)
 * @author cline
 *
 */
@Configuration
@Profile("production")
@EnableTransactionManagement
public class DataConfig {
	
	@Autowired
    private Environment env;
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");

		dataSource.setUrl("jdbc:oracle:thin:@XXXXXXXXX:1521:XXXXXX");
		dataSource.setUsername("XXXXXX");
		dataSource.setPassword("XXXXX");
	
		
		return dataSource;
	}
	

}
