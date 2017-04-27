package com.proj.configuration;

import javax.servlet.FilterRegistration;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitilizer extends AbstractAnnotationConfigDispatcherServletInitializer {
	 @Override
	    protected Class<?>[] getRootConfigClasses() {
	        return new Class[] { AppConfig.class };
	    }

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
	
	

}
