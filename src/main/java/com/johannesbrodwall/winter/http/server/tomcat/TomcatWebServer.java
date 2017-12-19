package com.johannesbrodwall.winter.http.server.tomcat;

import java.net.URISyntaxException;
import java.util.UUID;

import javax.servlet.Servlet;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.johannesbrodwall.winter.ExceptionUtil;
import com.johannesbrodwall.winter.http.requests.HttpResponder;
import com.johannesbrodwall.winter.http.requests.ServletHttpResponderAdapter;
import com.johannesbrodwall.winter.http.server.ServletWebServer;
import com.johannesbrodwall.winter.http.server.ServletWebServerExtensions;

public class TomcatWebServer implements ServletWebServer {

    public class Extensions implements ServletWebServerExtensions {

        public void addPathToServletInstance(String path, Servlet servlet) {
            String servletName = servlet.getClass().getSimpleName();
            Tomcat.addServlet(context, servletName, servlet);
            context.addServletMappingDecoded(path, servletName);
        }

        @Override
        public void setServletAttribute(String name, Object object) {
            context.getServletContext().setAttribute(name, object);
        }

        @Override
        public void mapPathToServletClass(String path, Class<? extends Servlet> servletClass) {
            String servletName = servletClass.getSimpleName();
            Tomcat.addServlet(context, servletName, servletClass.getName());
            context.addServletMappingDecoded(path, servletName);
        }

    }

    private Tomcat tomcat;
    private Context context;

    public TomcatWebServer(int port) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        this.tomcat = new Tomcat();
        tomcat.setPort(port);
        this.context = tomcat.addContext("", getBaseContext());
    }

    private String getBaseContext() {
        // TODO: Ensure that the context path is sensible
        try {
            return getClass().getResource("/webapp/").toURI().getPath();
        } catch (URISyntaxException e) {
            throw ExceptionUtil.soften(e);
        }
    }

    @Override
    public int getActualPort() {
        return tomcat.getConnector().getLocalPort();
    }

    @Override
    public void mapPathToResponder(String path, HttpResponder responder) {
        String servletName = responder.getClass().getSimpleName() + "-" + UUID.randomUUID();
        getExtensions().setServletAttribute(servletName, responder);

        Wrapper servlet = Tomcat.addServlet(context, servletName, ServletHttpResponderAdapter.class.getName());
        servlet.addInitParameter(ServletHttpResponderAdapter.RESPONDER_NAME, servletName);
        context.addServletMappingDecoded(path, servletName);
    }

    @Override
    public void start() throws Exception {
        tomcat.start();
    }

    @Override
    public void await() {
        tomcat.getServer().await();
    }

    @Override
    public Extensions getExtensions() {
        return new Extensions();
    }

}
