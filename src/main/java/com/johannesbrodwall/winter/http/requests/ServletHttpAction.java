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

public class ServletHttpAction implements HttpAction {

    private static Logger LOG = LoggerFactory.getLogger(ServletHttpAction.class);

    private HttpServletRequest req;
    private HttpServletResponse res;
    private HashMap<String, String> pathParameters;

    public ServletHttpAction(HttpServletRequest req, HttpServletResponse res, HashMap<String, String> pathParameters) {
        this.req = req;
        this.res = res;
        this.pathParameters = pathParameters;
    }

    @Override
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

    @Override
    public String parameter(String name) {
        return req.getParameter(name);
    }

    @Override
    public String pathVariable(String name) {
        if (!pathParameters.containsKey(name)) {
            throw new IllegalArgumentException("Can't find " + name + " in " + pathParameters);
        }
        return pathParameters.get(name);
    }

    @Override
    public void returnObject(Object object) {
        JSONObject jsonObject = object instanceof JSONObject ? (JSONObject) object : new JSONObject(object);

        res.setContentType("application/json");
        try {
            jsonObject.write(res.getWriter());
        } catch (JSONException | IOException e) {
            throw ExceptionUtil.soften(e);
        }
    }

    @Override
    public void returnString(String string) {
        res.setContentType("text/plain");
        try {
            res.getWriter().write(string);
        } catch (IOException e) {
            throw ExceptionUtil.soften(e);
        }
    }

}
