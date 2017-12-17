package com.johannesbrodwall.winter.http.server;

import java.net.URISyntaxException;
import java.util.UUID;

import javax.servlet.Servlet;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.johannesbrodwall.winter.ExceptionUtil;
import com.johannesbrodwall.winter.http.requests.HttpResponder;

public class TomcatWebServer implements WebServer {

	public class Extensions implements WebServerExtensions {

		public void addPathToServletInstance(String path, Servlet servlet) {
			String servletName = servlet.getClass().getSimpleName();
			Tomcat.addServlet(context, servletName, servlet);
			context.addServletMappingDecoded(path, servletName);
		}

		 @Override
		public void setServletAttribute(String name, Object object) {
			 context.getServletContext().setAttribute(name, object);
		}

	}

	private Tomcat tomcat = new Tomcat();
	private Context context;

	public TomcatWebServer() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		this.context = tomcat.addContext("",  getBaseContext());
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
	public void setPort(int port) {
		tomcat.setPort(port);
	}

	@Override
	public int getActualPort() {
		return tomcat.getConnector().getLocalPort();
	}

	@Override
	public void mapPathToServletClass(String path, Class<? extends Servlet> servletClass) {
		String servletName = servletClass.getSimpleName();
		Tomcat.addServlet(context, servletName, servletClass.getName());
		context.addServletMappingDecoded(path, servletName);
	}

	@Override
	public void mapPathToResponder(String path, HttpResponder responder) {
		String servletName = responder.getClass().getSimpleName() + "-" + UUID.randomUUID();
		getExtensions().setServletAttribute(servletName, responder);

		Wrapper servlet = Tomcat.addServlet(context, servletName, HttpResponderServlet.class.getName());
		servlet.addInitParameter(HttpResponderServlet.RESPONDER_NAME, servletName);
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
