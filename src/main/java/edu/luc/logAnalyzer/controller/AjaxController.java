package edu.luc.logAnalyzer.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.luc.logAnalyzer.domain.ApplicationProperties;
import edu.luc.logAnalyzer.service.AppPropertyService;

@RestController
public class AjaxController {
	
	@Autowired
	AppPropertyService appPropertyService;
	
	@RequestMapping(value="/getFileList/{application}", method = RequestMethod.GET)

	public Collection<String> getRelatedFiles(@PathVariable String application, HttpServletRequest request, HttpServletResponse response) {
		
		ApplicationProperties value = appPropertyService.getProperties(application.toUpperCase());
		System.out.println("the value is "+value);
		ArrayList<String> fileList = (ArrayList<String>)request.getSession().getAttribute("fileList");
		if(fileList!=null && fileList.size()>0)
		{
			String val = value.getValue();
			List<String> fileLis = new ArrayList<String>();
			fileLis = fileList.stream().filter(name -> name.toLowerCase().contains(val.toLowerCase())).collect(Collectors.toList());
			Collections.sort(fileLis);
			return fileLis;
			
		}
		else
		{
			List<String> fileLis = new ArrayList<String>();
			return fileLis;
		}
		
	}

}
