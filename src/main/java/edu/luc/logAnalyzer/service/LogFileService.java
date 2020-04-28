package edu.luc.logAnalyzer.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.luc.logAnalyzer.data.PropertyRepository;
import edu.luc.logAnalyzer.util.EmailService;

@Service
@Configuration
@EnableScheduling
public class LogFileService {
	
	@Autowired
	PropertyRepository propertyRepository;
	
	@Autowired
	EmailService emailService;
	

	
	public List<String> findApplicationNames() {
		String applications= (propertyRepository.findById("APPLICATION_NAME")).get().getValue();
		List<String> applicationList = new ArrayList<String>();
		applicationList = Arrays.asList(applications.split(","));
		return applicationList;

	}

	
	
	public ArrayList<String> findAllFileNames() {
		
		
		String serverPathList = (propertyRepository.findById("LOG_FILE_PATH")).get().getValue();
		ArrayList<String> fileList = new ArrayList<String>();
		String[] serverNames = serverPathList.split(",");
		for (String serverName : serverNames) 
		{
			File folder = new File(serverName);
			File[] listOfFiles = folder.listFiles();
			for(File file : listOfFiles)
			{
				if (file.isFile()) {

					if(!fileList.contains(file.getName()))
					{
						fileList.add(file.getName());
					}
				}
			}
		}
		Collections.sort(fileList);
		return fileList;
	}
	
	public HashMap<String, ArrayList<String>> search(List<String> logFileNames, String searchString) {
		String serverPathList = (propertyRepository.findById("LOG_FILE_PATH")).get().getValue();
		String[] serverNames = serverPathList.split(",");
		HashMap<String, ArrayList<String>> finalMap = new HashMap<String, ArrayList<String>>();
		String lowerCaseSearchString = searchString.toLowerCase();
		for (String serverName : serverNames) {
			for (String log : logFileNames) {
				ArrayList<String> searchResult = new ArrayList<String>();
				
				try {
					File file = new File(serverName + log);
					boolean exists = file.exists();
					if(exists)
					{

					BufferedReader br = new BufferedReader(new FileReader(file));

					String st;
					while ((st = br.readLine()) != null) {
						String lowerCaseContent = st.toLowerCase();
						
						if (StringUtils.contains(lowerCaseContent, lowerCaseSearchString)) {
							searchResult.add(st);
						}
					}
					
				}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					searchResult.clear();
					searchResult.add("ERROR OCCURED WHILE PROCESSING THIS FILE. PLEASE LOOK INTO THE LOG FOR DETAILS");
				}
				System.out.println("************************** "+log);
				searchResult.stream().forEach(r -> System.out.println(r));
				if(searchResult!=null && searchResult.size()>0)
				{
					String dummy = serverName;
					while(dummy.contains("\\"))
					{
						dummy = dummy.replace("\\", "/");
					}
					finalMap.put(log, searchResult);
				}
			}
		}
		return finalMap;
	}

	
	public void findErrorsOfTheDayTesting() throws Exception
	{
		HashMap<String, Integer> hashMap = new HashMap<String,Integer>();
		
		hashMap.put("TEST-%%%%%-#####-1", 1);
		hashMap.put("TEST-%%%%%-#####-3", 3);
		hashMap.put("TEST-%%%%%-#####-4", 4);
		hashMap.put("TEST-%%%%%-#####-5", 5);
		emailService.sendEmail(hashMap);
	}
	
	
	

	
	@Scheduled(cron = "0 50 09 ? * * ")
	@Transactional
	public void findErrorsOfTheDay() throws Exception
	{
		//servername/application/application/,//servername2/applicationgroup/application/
		String serverPathList = (propertyRepository.findById("LOG_FILE_PATH")).get().getValue();
		//application.log,tomcat.log,app2.log
		String logFilesList = (propertyRepository.findById("LOG_FILE_NAME")).get().getValue();
		
		 HashMap<String, Integer> hashMap = new HashMap<String,Integer>();
		  String[] logFiles = logFilesList.split(",");
		  String[] serverNames = serverPathList.split(",");
		for (String serverName : serverNames) {
			for (String log : logFiles) {
				System.out.println("******************************************" + log);
				try {
					File file = new File(serverName + log);

					BufferedReader br = new BufferedReader(new FileReader(file));

					String st;
					while ((st = br.readLine()) != null) {

						if (StringUtils.contains(st, "ERROR")) {
							// System.out.println(st);
							String[] errorSplit = st.split("ERROR");
							//String key = serverName+log +"-%%%%%-#####-"+ errorSplit[1];
							
							String key = log +"-%%%%%-#####-"+ errorSplit[1];
							if (!hashMap.containsKey(key)) {
								hashMap.put(key, 1);
							} else {
								Integer value = hashMap.get(key);
								value = value + 1;
								hashMap.put(key, value);
							}

						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}
		// System.out.println(hashMap.toString());
		
		HashMap<String, Integer> sortedByKey= hashMap.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey,
				               Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));



		  
		emailService.sendEmail(sortedByKey);
		
		
	}
	
	


}
