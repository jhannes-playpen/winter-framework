package com.johannesbrodwall.winter.http.requests;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.johannesbrodwall.winter.ExceptionUtil;

public class ServletHttpActionSelector implements HttpActionSelector {

	private HttpServletRequest req;
	private HttpServletResponse res;
	private boolean isHandled = false;

	public ServletHttpActionSelector(ServletRequest req, ServletResponse res) {
		this.req = (HttpServletRequest) req;
		this.res = (HttpServletResponse) res;
	}

	@Override
	public void onGet(String path, Consumer<HttpAction> action) {
		HashMap<String, String> pathParameters = new HashMap<String, String>();
		if (req.getMethod().equalsIgnoreCase("GET") && pathMatches(path, pathParameters)) {
			handle(action, pathParameters);
		}
	}


	@Override
	public void onPost(String path, Consumer<HttpAction> action) {
		HashMap<String, String> pathParameters = new HashMap<String, String>();
		if (req.getMethod().equalsIgnoreCase("POST") && pathMatches(path, pathParameters)) {
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
		action.accept(new ServletHttpAction(req, res, pathParameters));
		this.isHandled = true;
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
