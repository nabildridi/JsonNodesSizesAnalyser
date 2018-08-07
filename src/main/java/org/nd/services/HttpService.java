package org.nd.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public String getUrl(String url) {
		
		String json = null;
		
		log.debug("Trying Get  :" + url);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.getForEntity(url, String.class);
			if(response.getStatusCode() == HttpStatus.OK) {
				json = response.getBody();
			}
		} catch (RestClientException e) {}
		
		
		if(json == null) {
			try {
				log.debug("Get failed, trying Post");
				response  = restTemplate.postForEntity(url, null, String.class);
				if(response.getStatusCode() == HttpStatus.OK) {
					json = response.getBody();
				}
			} catch (RestClientException e) {}			
		}


		return json;
	}
	
}
