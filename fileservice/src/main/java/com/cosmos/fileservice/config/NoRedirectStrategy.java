package com.cosmos.fileservice.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.RedirectStrategy;

public class NoRedirectStrategy implements RedirectStrategy {

	@Override
	public void sendRedirect(HttpServletRequest arg0, HttpServletResponse arg1, String arg2) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
