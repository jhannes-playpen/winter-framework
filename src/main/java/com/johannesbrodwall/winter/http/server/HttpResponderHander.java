package com.johannesbrodwall.winter.http.server;

import java.io.IOException;

import com.johannesbrodwall.winter.http.requests.HttpResponder;
import com.johannesbrodwall.winter.http.server.jdk.JdkHttpActionSelector;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class HttpResponderHander implements HttpHandler {

	private HttpResponder responder;
	private String path;

	public HttpResponderHander(String path, HttpResponder responder) {
		this.path = path;
		this.responder = responder;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		responder.handle(new JdkHttpActionSelector(path, exchange));
	}

}
