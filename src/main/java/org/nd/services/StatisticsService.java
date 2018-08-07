package org.nd.services;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.nd.models.Analytics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class StatisticsService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	private Analytics result;
	
	public Analytics analyze(String json) {
		
		log.debug("analyzing");
		result = new Analytics();
		
		//test json length
		if(json == null || json.isEmpty()) {
			result.setError(true);
			result.setErrorCause("Json is empty : invalid url or empty file");
			return result;
		}
		
		
		//test json validity
		JsonNode rootNode = null;
		try {
			rootNode = jsonMapper.readTree(json);
		} catch (IOException e) {
			result.setError(true);
			result.setErrorCause("Json is invalid");
			return result;
		}
		
		
		try {
			result.setFullSize(jsonMapper.writeValueAsString(rootNode).length());
		} catch (JsonProcessingException e) {}
		
		//traverse
		visit(rootNode, "/", false);
		
	
		return result;
	}
	
	
	
	
	private void visit(JsonNode node, String currentPath, boolean partOfArray) {
		
	
		if(node.isArray()) {
			
			//get array size
			int arraySize = 0;
			try {arraySize = jsonMapper.writeValueAsString(node).length();} catch (JsonProcessingException e) {}
			
			//store in map
			if(result.getArrays().containsKey(currentPath)) {
				Integer oldSize = result.getArrays().get(currentPath);
				result.getArrays().put(currentPath, oldSize + arraySize);
			}else {
				result.getArrays().put(currentPath, arraySize);
			}				
			
			
			ArrayNode fieldArray = (ArrayNode)node;
			Iterator<JsonNode> arrayIterator = fieldArray.elements();
			while (arrayIterator.hasNext()) {
				JsonNode arrayNode = arrayIterator.next();
				visit(arrayNode, currentPath, true);
			}
			
			
			
		}
		
		
		if(node.isObject()) {
			
			if(!partOfArray) {
				//get object size
				int objectSize = 0;
				try {objectSize = jsonMapper.writeValueAsString(node).length();} catch (JsonProcessingException e) {}
				
				//store in map
				if(result.getObjects().containsKey(currentPath)) {
					Integer oldSize = result.getObjects().get(currentPath);
					result.getObjects().put(currentPath, oldSize + objectSize);
				}else {
					result.getObjects().put(currentPath, objectSize);
				}
			}
			
			
			Iterator<Map.Entry<String, JsonNode>> children = node.fields();

			while (children.hasNext()) {

				Map.Entry<String, JsonNode> mapEntry = children.next();
				String fieldName = mapEntry.getKey();
				JsonNode child = mapEntry.getValue();
	
				visit(child, currentPath + fieldName + "/", false);
			}
			
			
		}
		


	}
	

	
}
