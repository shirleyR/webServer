package com.server;
import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {
	private List<XMLServlet> servlet;
	private List<XMLMapping> mapping;
	private XMLServlet serv;
	private XMLMapping map;
	private String beginTag;
	private boolean isMap;
	public List<XMLServlet> getServlet(){
		return servlet;
	}
	public void setServlet(List<XMLServlet> servlet){
		this.servlet=servlet;
	}
	@Override
	public void startDocument()throws SAXException{
		servlet=new ArrayList<XMLServlet>();
		mapping=new ArrayList<XMLMapping>();
	}
	
	public List<XMLMapping> getMapping() {
		return mapping;
	}
	public void setMapping(List<XMLMapping> mapping) {
		this.mapping = mapping;
	}
	public XMLServlet getServ() {
		return serv;
	}
	public void setServ(XMLServlet serv) {
		this.serv = serv;
	}
	public boolean isMap() {
		return isMap;
	}
	public void setMap(boolean isMap) {
		this.isMap = isMap;
	}
	@Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if(null!=qName){
            beginTag = qName;
            if(qName.equals("servlet")){
                serv = new XMLServlet();
                isMap = false;
            }else if(qName.equals("servlet-mapping")){
                map = new XMLMapping();
                isMap = true;
            }
        }
        
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        
        if(beginTag != null){
            String info = new String(ch, start, length);
            if(isMap){
                if(beginTag.equals("servlet-name")){
                    map.setServlet_name(info.trim());
                }else if(beginTag.equals("url-pattern")){
                    map.getUrl_pattern().add(info);
                }
            }else{
                if(beginTag.equals("servlet-name")){
                    serv.setServlet_name(info);
                }else if(beginTag.equals("servlet-class")){
                    serv.setServlet_class(info);
                }
            }
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if(null!=qName){
            if(qName.equals("servlet")){
                servlet.add(serv);
            }else if(qName.equals("servlet-mapping")){
                mapping.add(map);
            }
        }
        beginTag = null;
    }

    @Override
    public void endDocument() throws SAXException {
        //ÎÄµµ½áÊø
    }
}
