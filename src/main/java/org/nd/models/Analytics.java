package org.nd.models;

import java.util.HashMap;
import java.util.Map;

public class Analytics {
	
	private Integer fullSize;
	private Boolean error;
	private String errorCause;
	
	private Map<String, Integer> arrays = new HashMap<String, Integer>();
	
	private Map<String, Integer> objects = new HashMap<String, Integer>();
	
	
	public Analytics() {
		this.error = false;
	}

	public Integer getFullSize() {
		return fullSize;
	}

	public void setFullSize(Integer fullSize) {
		this.fullSize = fullSize;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}

	public Map<String, Integer> getArrays() {
		return arrays;
	}

	public void setArrays(Map<String, Integer> arrays) {
		this.arrays = arrays;
	}

	public Map<String, Integer> getObjects() {
		return objects;
	}

	public void setObjects(Map<String, Integer> objects) {
		this.objects = objects;
	}
	
	
	
}
