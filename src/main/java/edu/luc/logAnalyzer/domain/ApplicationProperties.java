package edu.luc.logAnalyzer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LOGFILEANALYZER_PROPERTIES")

public class ApplicationProperties {
	
	@Id
	@Column(name="KEY")
	private String key;
	
	@Column(name="VALUE")
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ApplicationProperties [key=" + key + ", value=" + value + "]";
	}
	
	
	

}
