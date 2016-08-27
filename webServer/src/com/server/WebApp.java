package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class WebApp {
	private static ServletContext context;
	static{
		context=new ServletContext();
		Map<String,String> mapping=context.getMapping();
		Map<String, String>servlet=context.getServlet();
		
		SAXParserFactory factory=SAXParserFactory.newInstance();
		try{

			SAXParser parser=factory.newSAXParser();
			XMLHandler handler=new XMLHandler();
			InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("com/server/WEB-INF/web.xml");
			parser.parse(is, handler);
					
			List<XMLServlet> serv=handler.getServlet();
			
			for(XMLServlet xmlServlet:serv){
				String name=xmlServlet.getServlet_name();
				servlet.put(name, xmlServlet.getServlet_class());
			}
			List<XMLMapping> map=handler.getMapping();
			
			for(XMLMapping maps:map){
				List<String> actions=maps.getUrl_pattern();
				for(String action:actions){
					mapping.put(action, maps.getServlet_name());
				}
			}
			System.out.println(context.getMapping().size());
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}
	}
	public static Servlet getServlet(String action){
		System.out.println(action+context.getMapping().get("/snoop"));
		if("".equals(action)||action==null){
			return null;
		}
		String servlet_name=context.getMapping().get(action.trim());
		String classPath=context.getServlet().get(servlet_name);
		Servlet servlet=null;
		if(classPath!=null){
			Class<?> clazz=null;
			try{
				clazz=Class.forName(classPath);
				servlet=(Servlet)clazz.newInstance();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}catch(InstantiationException e){
				e.printStackTrace();
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}
		}
		return servlet;	
	}
}
