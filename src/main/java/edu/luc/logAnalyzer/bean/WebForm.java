package edu.luc.logAnalyzer.bean;

import java.util.List;

public class WebForm {
	
	private String application;
	private List<String> filenames;
	private String search;
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public List<String> getFilenames() {
		return filenames;
	}
	public void setFilenames(List<String> filenames) {
		this.filenames = filenames;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	@Override
	public String toString() {
		return "WebForm [application=" + application + ", filenames=" + filenames + ", search=" + search + "]";
	}
	
	

}
