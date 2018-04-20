package com.ilib.other.interceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
@Component("encodingLoggingInInterceptor")
public class RestInterceptor extends LoggingInInterceptor{
	/**
	 * Copyright (c) 2011-2014 All Rights Reserved.
	 */

	    private Logger log = Logger.getLogger(getClass());
	    
	    /**
	     * 
	     */
	    public RestInterceptor() {
	        // TODO Auto-generated constructor stub
	        super();
	    }
	    
	    /** 
	     * @see org.apache.cxf.interceptor.LoggingInInterceptor#handleMessage(org.apache.cxf.message.Message)
	     */
	    @Override
	    public void handleMessage(Message message) throws Fault {
	        // TODO Auto-generated method stub
	        
	        String encoding = System.getProperty("file.encoding");
	        encoding = encoding == null || encoding.equals("") ? "UTF-8" : encoding;
	        
	        log.debug("encoding : " + encoding);
	        message.put(Message.ENCODING, encoding);
	        
	        super.handleMessage(message);
	    }

}
