package com.server;


public class SnoopServlet extends Servlet{
	@Override
	public void service(HttpRequest request,HttpResponse response){
		System.out.println("Servlet");
		System.out.println(request.getAction());
	}
}
