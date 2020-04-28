package edu.luc.logAnalyzer.data;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.luc.logAnalyzer.domain.ApplicationProperties;

public interface PropertyRepository  extends JpaRepository<ApplicationProperties, String> { 

}
