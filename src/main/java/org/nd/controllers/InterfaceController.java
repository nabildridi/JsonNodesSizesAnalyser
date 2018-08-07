package org.nd.controllers;

import java.util.Map;

import org.nd.models.Analytics;
import org.nd.services.HttpService;
import org.nd.services.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class InterfaceController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StatisticsService statisticsService;
	
	@Autowired
	private HttpService httpService;
	
	private ObjectMapper jsonMapper = new ObjectMapper();

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "application";
	}	
	
	@PostMapping("/upload") 
    public String fileEndpoint(Map<String, Object> model, @RequestParam("file") MultipartFile file) {
		
		log.debug("file received");
		
		String response = "";

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            String json  =  new String(bytes);
            
            Analytics result = statisticsService.analyze(json);
            response = jsonMapper.writeValueAsString(result);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

	    
	    model.put("response", response);
	    
	    return "json";

    }
	
	@PostMapping("/text") 
    public String textEndpoint(Map<String, Object> model, @RequestBody String json) {
		
		log.debug("text received");
	    
	    String response = "";
		try {
			Analytics result = statisticsService.analyze(json);
            response = jsonMapper.writeValueAsString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    model.put("response", response);
	    
	    return "json";
    }
	
	
	@PostMapping("/url") 
    public String urlEndpoint(Map<String, Object> model, @RequestBody String url) {
		
		log.debug("url received");
		
		String json = httpService.getUrl(url);

		String response = "";
		try {
			Analytics result = statisticsService.analyze(json);
            response = jsonMapper.writeValueAsString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    model.put("response", response);
	    
	    return "json";
    }
	
}