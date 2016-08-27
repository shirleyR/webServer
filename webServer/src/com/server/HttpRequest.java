package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
	private static final String ENTER="\r\n";
	private BufferedReader request;
	private String requestHeader;
	private String method;
	private String action;
	private Map<String,List<String>> parameter;
	public HttpRequest(){
		requestHeader="";
		method="";
		action="";
		parameter=new HashMap<String,List<String>>();
	}
	public HttpRequest (InputStream inputStream){
		this();
		request=new BufferedReader(new InputStreamReader(inputStream));
		// receive header
		try{
			String temp;
			while(!(temp=request.readLine()).equals("")){
				requestHeader+=(temp+ENTER);
			}
			System.out.println(requestHeader);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		parseRequestHeader();
	}
	public void parseRequestHeader(){
		String parameterString="";
		String firstLine=requestHeader.substring(0,requestHeader.indexOf(ENTER));
		
		int splitPointOne=firstLine.indexOf("/");
		method=firstLine.substring(0, splitPointOne).trim();
		int splitPointTwo=firstLine.indexOf("HTTP/");
		String actionTemp=firstLine.substring(splitPointOne,splitPointTwo);
		if(method.equalsIgnoreCase("post")){
			this.action=actionTemp;
		}else if(method.equalsIgnoreCase("get")){
			if(actionTemp.contains("?")){
				parameterString=actionTemp.substring((actionTemp.indexOf("?")+1)).trim();
				this.action=actionTemp.substring(0, actionTemp.indexOf("?"));
			}else{
				this.action=actionTemp;
			}
			// 
			parseParameterString(parameterString);
		}
				
	}
	private void parseParameterString(String parameterString){
		if("".equals(parameterString)){
			return;
		}else{
			String[]parameterKeyValues=parameterString.split("&");
			for(int i=0;i<parameterKeyValues.length;i++){
				String[] KeyValues=parameterKeyValues[i].split("=");
				if(KeyValues.length==1){
					KeyValues=Arrays.copyOf(KeyValues, 2);
					KeyValues[1]=null;
				}
				String key=KeyValues[0].trim();
				String values=null==KeyValues[1]?null:decode(KeyValues[1].trim(),"UTF-8");
				if(!parameter.containsKey(key)){
					parameter.put(key, new ArrayList<String>());
				}
				List<String> value=parameter.get(key);
				value.add(values);
			}
		}
	}
	public String decode(String string,String encoding){
		try{
			return URLDecoder.decode(string,encoding);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
	public String[] getParameterValues(String name){
		List<String> values=parameter.get(name);
		if(values==null){
			return null;
		}else
			return values.toArray(new String[0]);
	}
	public String getParameter(String name){
		String[] value=getParameterValues(name);
		if(value==null){
			return null;
		}
		return value[0];
	}
	public String getAction(){
		return action;
	}
	public void setAction(String action){
		this.action=action;
	}
	public String getMethod(){
		return method;
	}
	public void setMethod(String method){
		this.method=method;
	}

}
