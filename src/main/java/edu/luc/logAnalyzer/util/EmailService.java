package edu.luc.logAnalyzer.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import edu.luc.logAnalyzer.data.PropertyRepository;


@Service
public class EmailService {

	@Autowired
	PropertyRepository propertyRepository;
	
	@Autowired
    private JavaMailSender javaMailSender;

	
	public void sendEmail(HashMap<String,Integer> errorList) throws Exception
	{
		
        SimpleMailMessage msg = new SimpleMailMessage();
		//MimeMessage msg = javaMailSender.createMimeMessage();
	    //MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        //rjebamalaidass@luc.edu,mathew@luc.edu,ashley@luc.edu,cheryl@luc.edu
        String emailList = (propertyRepository.findById("EMAIL_ADDRESS")).get().getValue();
        String[] emails = emailList.split(",");
       
    	msg.setTo(emails);
      
    	msg.setFrom("WebTeam@luc.edu");
    	msg.setSubject("Daily Error Report for Web Team");
        String body = generateEmailBody(errorList );
        System.out.println("*********************************************************************");
        System.out.println(body);
        System.out.println("*********************************************************************");
        msg.setText(body);
       
       
    	
        msg.setSentDate(new Date());
    	
    	  	
        
    	
        javaMailSender.send(msg);
		
	}
	
	
	
	
	public String generateEmailBody(HashMap<String,Integer> errorList)
	{
		StringBuffer body = new StringBuffer();
		//body.append("<html><head></head><body>");
		//body.append("<table><th>Log File Path </th><th>Log File Name</th><th>Error Count</th>");
		Set<String> keySet = errorList.keySet();
		
		for (String key : keySet) {
			try
			{
				String[] keySplit = key.split("-%%%%%-#####-");
				//System.out.println(keySplit.toString());
				
				//body.append("<tr><td>"+keySplit[0]+"</td><td>"+keySplit[1]+"</td><td>"+errorList.get(key)+"</td></tr>");
			//	body.append("<tr><td>"+keySplit[0]+"</td><td>"+errorList.get(key)+"</td></tr>");
				//keySplit[1] = keySplit[1].replace(";", "");
				body.append("\n"+keySplit[0]+"\t\t"+keySplit[1]+"\t\t"+errorList.get(key));
				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		//body.append("</table></body></html>");
		  
		
		return body.toString();

	}
	
	
	

}
