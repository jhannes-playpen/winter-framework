package com.johannesbrodwall.winter.http.server;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.johannesbrodwall.winter.ExceptionUtil;


public class HttpAction {

	private HttpServletRequest req;
	private HttpServletResponse res;
	private HashMap<String, String> pathParameters;

	public HttpAction(HttpServletRequest req, HttpServletResponse res, HashMap<String, String> pathParameters) {
		this.req = req;
		this.res = res;
		this.pathParameters = pathParameters;
	}

	public void sendNotFound() {

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


}
