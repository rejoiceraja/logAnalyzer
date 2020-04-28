package edu.luc.logAnalyzer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.luc.logAnalyzer.data.PropertyRepository;
import edu.luc.logAnalyzer.domain.ApplicationProperties;

@Service
public class AppPropertyService {
	
	@Autowired
	PropertyRepository propertyRepository;
	
	public ApplicationProperties getProperties(String key)
	{
		Optional<ApplicationProperties> properties = propertyRepository.findById(key);
		return properties.get();
		
	}

}
