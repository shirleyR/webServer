package com.server;

import java.util.*;

public class ServletContext {
	private static Map<String,String>servlet;
	private static Map<String,String> mapping;
	static{
		servlet=new HashMap<String,String>();
		mapping=new HashMap<String,String>();
	}
	public Map<String,String> getServlet(){
		return servlet;
	}
	public void setServlet(Map<String,String> servlet){
		ServletContext.servlet=servlet;
	}
	public Map<String,String> getMapping(){
		return mapping;
	}
	public void setMapping(Map<String,String> mapping){
		ServletContext.mapping=mapping;
	}
}
