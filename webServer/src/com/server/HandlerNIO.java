package com.server;

import java.io.*;
import java.net.*;
public class HandlerNIO implements Runnable {
	private Socket client;
	private HttpRequest request;
	private HttpResponse response;
	private Queue myQueue;
	private int code=200;
	public HandlerNIO(Queue q){
		myQueue=q;
	}
	public void run(){
		while(true){
			MyClient clients=(MyClient)myQueue.remove(); 
			try{
				System.out.println("Start to request");
				request=new HttpRequest(clients.clientInputStream);
				response=new HttpResponse(clients.key);
		
				String action=request.getAction();
				Servlet servlet=WebApp.getServlet(action);
				if(servlet==null){
					this.code=404;
					response.sendError(code," error");
					return;
				}
				System.out.println("Re!!!!!!!!!!!!!");
				servlet.service(request, response);
				if (clients.notifyRequestDone())
					break;
			}catch(Exception e){
				e.printStackTrace();
				code=500;
				
			}
			try{
				response.sendError(code,"");
				clients.key.cancel();
				clients.key.selector().wakeup();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}

}
