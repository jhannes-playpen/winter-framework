package com.johannesbrodwall.winter.http.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.johannesbrodwall.winter.ExceptionUtil;
import com.johannesbrodwall.winter.http.requests.HttpAction;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class JdkHttpAction implements HttpAction {

	private HttpExchange exchange;
	private HashMap<String, String> pathParameters;

	public JdkHttpAction(HttpExchange exchange, HashMap<String, String> pathParameters) {
		this.exchange = exchange;
		this.pathParameters = pathParameters;
	}

	@Override
	public void sendNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public String parameter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String pathVariable(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnObject(Object jsonObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void returnString(String string) {
		exchange.getResponseHeaders().set("Content-Type", "text/plain");
		try {
			exchange.sendResponseHeaders(0, string.getBytes().length);
			exchange.getResponseBody().write(string.getBytes(StandardCharsets.UTF_8));
			exchange.getResponseBody().close();
		} catch (IOException e) {
			throw ExceptionUtil.soften(e);
		}
	}

}
