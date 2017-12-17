package com.johannesbrodwall.winter.http.server;

import java.net.InetSocketAddress;
import java.util.UUID;

import javax.servlet.Servlet;

import com.johannesbrodwall.winter.http.requests.HttpResponder;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

public class UndertowWebServer implements WebServer {

	public class Extensions implements WebServerExtensions {

		@Override
		public void setServletAttribute(String name, Object value) {
			deploymentInfo.addServletContextAttribute(name, value);
		}

	}

	private Undertow.Builder undertowBuilder = Undertow.builder();
	private Undertow undertow;
	private DeploymentInfo deploymentInfo;

	public UndertowWebServer() {
		deploymentInfo = Servlets.deployment()
			.setContextPath("/")
			.setDeploymentName(getClass().getName())
			.setClassLoader(getClass().getClassLoader());
	}

	@Override
	public void setPort(int port) {
		undertowBuilder.addHttpListener(port, "localhost");
	}

	@Override
	public int getActualPort() {
		InetSocketAddress address = (InetSocketAddress) undertow.getListenerInfo().get(0).getAddress();
		return address.getPort();
	}

	@Override
	public void mapPathToServletClass(String path, Class<? extends Servlet> servletClass) {
		deploymentInfo
			.addServlet(Servlets.servlet(servletClass).addMapping(path));

	}

	@Override
	public void start() throws Exception {
		DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
		manager.deploy();
		undertowBuilder.setHandler(manager.start());

		undertow = undertowBuilder.build();
		undertow.start();
	}

	@Override
	public WebServerExtensions getExtensions() {
		return new Extensions();
	}

	@Override
	public void mapPathToResponder(String path, HttpResponder responder) {
		String servletName = responder.getClass().getSimpleName() + "-" + UUID.randomUUID();
		getExtensions().setServletAttribute(servletName, responder);

		deploymentInfo.addServlet(Servlets.servlet(HttpResponderServlet.class)
				.addMapping(path)
				.addInitParam(HttpResponderServlet.RESPONDER_NAME, servletName));
	}

	@Override
	public void await() {
	}

}
