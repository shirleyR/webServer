package com.server;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

public class Response {
	private static final String ENTER="\r\n";
	private static final String SPACE=" ";
	private StringBuilder headerInfo;
	private StringBuilder textContent;
	private int contentLength;
	private BufferedWriter bw;
	public Response(){
		headerInfo=new StringBuilder();
		textContent=new StringBuilder();
		contentLength=0;
	}
	
	public Response(OutputStream os){
		this();
		bw=new BufferedWriter(new OutputStreamWriter(os));
	}
	private void createHeader(int code){
		headerInfo.append("HTTP/ 1.1").append(SPACE).append(code).append(SPACE);
		switch(code){
		case 200:
			headerInfo.append("OK").append(ENTER);
			break;
		case 404:
			headerInfo.append("NOT FOUND").append(ENTER);
			break;
		case 500:
			headerInfo.append("SERVER ERROR").append(ENTER);
			break;
		default:
			break;
		}
		headerInfo.append("Server:myServer").append(SPACE)
				  .append("0.0.1v").append(ENTER)
				  .append("Content-Type:text/html;charset=UTF-8").append(ENTER);
				  
	}
	public Response htmlContent(String content){
		textContent.append(content).append(ENTER);
		contentLength+=(content+ENTER).toString().getBytes().length;
		return this;
	}
	
	public void pushToClient(int code){
		createHeader(code);
		try{
			bw.append(headerInfo.toString());
			System.out.println(headerInfo.toString());
			bw.append(textContent.toString());
			System.out.println(textContent.toString());
			bw.flush();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
