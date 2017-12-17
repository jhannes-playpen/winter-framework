package com.johannesbrodwall.winter.http.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.function.Consumer;

import com.johannesbrodwall.winter.ExceptionUtil;
import com.johannesbrodwall.winter.http.requests.HttpAction;
import com.johannesbrodwall.winter.http.requests.HttpActionSelector;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class JdkHttpActionSelector implements HttpActionSelector {

	private HttpExchange exchange;
	private boolean isHandled;
	private String path;

	public JdkHttpActionSelector(String path, HttpExchange exchange) {
		this.path = path;
		this.exchange = exchange;
	}

	@Override
	public void onGet(String path, Consumer<HttpAction> action) {
		HashMap<String, String> pathParameters = new HashMap<String, String>();
		if (exchange.getRequestMethod().equalsIgnoreCase("GET") && pathMatches(path, pathParameters)) {
			handle(action, pathParameters);
		}
	}

	@Override
	public void onPost(String path, Consumer<HttpAction> action) {
		HashMap<String, String> pathParameters = new HashMap<String, String>();
		if (exchange.getRequestMethod().equalsIgnoreCase("POST") && pathMatches(path, pathParameters)) {
			handle(action, pathParameters);
		}
	}

	@Override
	public void onNoMatch(Consumer<HttpAction> action) {
		if (!isHandled) {
			handle(action, new HashMap<>());
		}
	}

	private void handle(Consumer<HttpAction> action, HashMap<String, String> pathParameters) {
		action.accept(new JdkHttpAction(exchange, pathParameters));
		this.isHandled = true;
	}

	private boolean pathMatches(String pathPattern, HashMap<String, String> outputPathParameters) {
		String pathInfo = exchange.getRequestURI().toString();
		if (!pathInfo.startsWith(path)) {
			return false;
		}
		pathInfo = pathInfo.substring(path.length());

		if (pathInfo == null || pathInfo.isEmpty()) {
			return pathPattern.equals("/");
		}

		String[] actualParts = pathInfo.split("/");
		String[] patternParts = pathPattern.split("/");

		if (actualParts.length != patternParts.length) {
			return false;
		}

		for (int i = 0; i < patternParts.length; i++) {
			if (patternParts[i].startsWith("{") && patternParts[i].endsWith("}")) {
				outputPathParameters.put(patternParts[i].substring(1, patternParts[i].length()-1),
						decode(actualParts[i]));
			} else if (!patternParts[i].equals(actualParts[i])) {
				return false;
			}
		}

		return true;
	}

	private String decode(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtil.soften(e);
		}
	}
}
