package edu.luc.logAnalyzer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import edu.luc.logAnalyzer.LDAPConfig;
import edu.luc.logAnalyzer.bean.WebForm;
import edu.luc.logAnalyzer.domain.ApplicationProperties;
import edu.luc.logAnalyzer.service.AppPropertyService;
import edu.luc.logAnalyzer.service.LogFileService;
import edu.luc.logAnalyzer.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	LDAPConfig ldapConfig;
	
	@Autowired
	LogFileService logFileService;
	
	@Autowired
	AppPropertyService appPropertyService;
	
	@Autowired
	UserService userService;
	
	
	
	@GetMapping(value="/")
	public void initialPage(HttpServletRequest request, Model model, HttpSession session, SessionStatus status) throws Exception
	{	
		homePage(request, model, session, status);
	}
	
	@GetMapping(value="home")
	public String homePage(HttpServletRequest request, Model model, HttpSession session, SessionStatus status) throws Exception
	{
		
		 String errorMessage = "";
		try
		{
		
			
		
			List<String> applications = (List<String>)session.getAttribute("applications");
			if(applications==null)
			{
				applications = logFileService.findApplicationNames();
				session.setAttribute("applications", applications);
			}
		
			
			WebForm form = new WebForm();
			model.addAttribute("webform", form);
			//logFileService.findErrorsOfTheDay();
			//logFileService.findErrorsOfTheDayTesting();
			
			
			return "home";
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			model.addAttribute("message", "Error Login into the application");
			return "login";
		}
		
	}
	
	@GetMapping(value="runJob")
	public String runJob(HttpServletRequest request, Model model, HttpSession session, SessionStatus status) throws Exception
	{
		try
		{
			System.out.println("in the thome page");
			logFileService.findErrorsOfTheDay();
			//logFileService.findErrorsOfTheDayTesting();
			model.addAttribute("message", "Process Ran Successfully");
			return "home";
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return homePage(request, model, session, status);
		}
	}
	
	//@GetMapping(value="search")
	
	
	@PostMapping(value="search")
	public String search(HttpServletRequest request, Model model, HttpSession session, SessionStatus status, @ModelAttribute("webform") WebForm webform) throws Exception
	{
		try
		{
			System.out.println("in the search post  page" +webform.toString());
			//logFileService.findErrorsOfTheDay();
		//logFileService.findErrorsOfTheDayTesting();
		
//			List<String> logFileNames = new ArrayList<String>();
//			logFileNames.add("CATALINA.log");
//			logFileNames.add("oip.log");
//			String searchString = "ERROR";
			HashMap<String, ArrayList<String>> finalResult = logFileService.search(webform.getFilenames(), webform.getSearch());
			webform.setApplication("0");
			
			model.addAttribute("webform", webform);
			model.addAttribute("finalResult", finalResult);
			return "home";
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return homePage(request, model, session, status);
		}
	}
	
	
	


}
