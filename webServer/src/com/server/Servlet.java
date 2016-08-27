package com.server;

public class Servlet {
	public void service(HttpRequest request,HttpResponse response){
		/*
		String username=request.getParameter("user");
		response.htmlContent("<html><head></head><body>This <br>");
		response.htmlContent(" Welcome "+username+"</body></html>");*/
		String method=request.getMethod();
		if(method.equals("post")){
			
		}else{
			
		}
	}
	public void doGet(HttpRequest request,HttpResponse response){
		
	}
	public void doPost(HttpRequest request,HttpResponse response){
		
	}
}
