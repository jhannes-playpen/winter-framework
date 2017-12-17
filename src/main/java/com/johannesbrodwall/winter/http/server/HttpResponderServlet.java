package com.johannesbrodwall.winter.http.server;

import javax.servlet.ServletException;

import com.johannesbrodwall.winter.http.requests.HttpActionSelector;
import com.johannesbrodwall.winter.http.requests.HttpActionServlet;
import com.johannesbrodwall.winter.http.requests.HttpResponder;

public class HttpResponderServlet extends HttpActionServlet {

	public static final String RESPONDER_NAME = "RESPONDER_NAME";
	private HttpResponder responder;

	@Override
	public void handle(HttpActionSelector selector) {
		responder.handle(selector);
	}

	@Override
	public void init() throws ServletException {
		this.responder = (HttpResponder) getServletContext().getAttribute(getInitParameter(RESPONDER_NAME));
	}

}
