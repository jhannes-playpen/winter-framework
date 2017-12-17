package com.johannesbrodwall.winter.http.requests;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johannesbrodwall.winter.ExceptionUtil;


public class HttpAction {

	private static Logger LOG = LoggerFactory.getLogger(HttpAction.class);

	private HttpServletRequest req;
	private HttpServletResponse res;
	private HashMap<String, String> pathParameters;

	public HttpAction(HttpServletRequest req, HttpServletResponse res, HashMap<String, String> pathParameters) {
		this.req = req;
		this.res = res;
		this.pathParameters = pathParameters;
	}

	public void sendNotFound() {
		LOG.info("404 not found {}", getUrl());
		try {
			res.sendError(404, "No corresponding action");
			res.getWriter().append("No action corresponding to " + getUrl());
		} catch (IOException e) {
			LOG.error("Unhandleable exception while handing {}", getUrl(), e);
		}
	}

	private String getUrl() {
		return req.getRequestURL().toString();
	}

	public String parameter(String name) {
		return req.getParameter(name);
	}

	public String pathVariable(String name) {
		if (!pathParameters.containsKey(name)) {
			throw new IllegalArgumentException("Can't find " + name + " in " + pathParameters);
		}
		return pathParameters.get(name);
	}

	public void returnObject(Object jsonObject) {
		res.setContentType("application/json");
		try {
			new JSONObject(jsonObject).write(res.getWriter());
		} catch (JSONException | IOException e) {
			throw ExceptionUtil.soften(e);
		}
	}

	public void returnString(String string) {
		res.setContentType("text/plain");
		try {
			res.getWriter().write(string);;
		} catch (JSONException | IOException e) {
			throw ExceptionUtil.soften(e);
		}
	}


}
