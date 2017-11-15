package com.johannesbrodwall.winter.http.server;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public abstract class HttpActionServlet extends GenericServlet {

	public HttpActionServlet() {
		super();
	}

	public abstract void handle(HttpActionSelector selector);

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		HttpActionSelector selector = new HttpActionSelector(req, res);
		try {
			handle(selector);
			selector.onNoMatch(e -> {
				e.sendNotFound();
			});
		} catch (RuntimeException e) {
			((HttpServletResponse)res).sendError(500, e.toString());
			e.printStackTrace();
		}
	}

}