package com.johannesbrodwall.winter.http.requests;

import javax.servlet.ServletException;

public class ServletHttpResponderAdapter extends HttpActionServlet {

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
