package com.johannesbrodwall.winter.http.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.johannesbrodwall.winter.ExceptionUtil;

public class HttpActionSelector {

	private HttpServletRequest req;
	private HttpServletResponse res;
	private boolean isHandled = false;

	public HttpActionSelector(ServletRequest req, ServletResponse res) {
		this.req = (HttpServletRequest) req;
		this.res = (HttpServletResponse) res;
	}

	public void onGet(String path, Consumer<HttpAction> action) {
		HashMap<String, String> pathParameters = new HashMap<String, String>();
		if (req.getMethod().equalsIgnoreCase("GET") && pathMatches(path, pathParameters)) {
			action.accept(new HttpAction(req, res, pathParameters));
			this.isHandled = true;
		}
	}

	public void onPost(String path, Consumer<HttpAction> action) {
		HashMap<String, String> pathParameters = new HashMap<String, String>();
		if (req.getMethod().equalsIgnoreCase("POST") && pathMatches(path, pathParameters)) {
			action.accept(new HttpAction(req, res, pathParameters));
			this.isHandled = true;
		}
	}

	public void onNoMatch(Consumer<HttpAction> action) {
		if (!isHandled) {
			action.accept(new HttpAction(req, res, new HashMap<>()));
			this.isHandled = true;
		}
	}

	private boolean pathMatches(String pathPattern, HashMap<String, String> outputPathParameters) {
		if (req.getPathInfo() == null || req.getPathInfo().isEmpty()) {
			return pathPattern.equals("/");
		}

		String[] actualParts = req.getPathInfo().split("/");
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
