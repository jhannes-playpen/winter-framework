package com.johannesbrodwall.winter.http.requests;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpActionServlet extends GenericServlet {

    private static Logger LOG = LoggerFactory.getLogger(HttpActionServlet.class);

    public abstract void handle(HttpActionSelector selector);

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpActionSelector selector = new ServletHttpActionSelector(req, res);
        try {
            LOG.debug("Processing HTTP {} request {}", ((HttpServletRequest) req).getMethod(), ((HttpServletRequest) req).getRequestURL());
            handle(selector);
            selector.onNoMatch(e -> {
                e.sendNotFound();
            });
        } catch (RuntimeException e) {
            ((HttpServletResponse) res).sendError(500, e.toString());
            LOG.error("Error during processing of {}", ((HttpServletRequest) req).getRequestURL(), e);
        }
    }

}