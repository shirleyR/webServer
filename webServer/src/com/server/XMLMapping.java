package com.server;

import java.util.*;
public class XMLMapping {
	private String servlet_name;
	private List<String> url_pattern;
	public XMLMapping(){
		url_pattern=new ArrayList<String>();
	}
	public String getServlet_name() {
		return servlet_name;
	}
	public void setServlet_name(String servlet_name) {
		this.servlet_name = servlet_name;
	}
	public List<String> getUrl_pattern() {
		return url_pattern;
	}
	public void setUrl_pattern(List<String> url_pattern) {
		this.url_pattern = url_pattern;
	}
	
}
