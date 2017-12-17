package com.johannesbrodwall.hellodocumentdb;

import java.net.InetSocketAddress;
import java.util.Optional;

import javax.servlet.ServletException;

import com.johannesbrodwall.hellodocumentdb.person.PersonController;
import com.johannesbrodwall.winter.config.PropertySource;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

public class HelloApplication {

	private int httpPort;
	private int actualPort;
	private HelloApplicationContext helloApplicationContext;

	public HelloApplication(int httpPort) {
		this.httpPort = httpPort;
	}

	public static void main(String[] args) throws ServletException {
		int port = Optional.ofNullable(System.getenv("HTTP_PORT")).map(Integer::parseInt).orElse(8080);
		new HelloApplication(port).run(args);
	}

	private void run(String[] args) throws ServletException {
		setContext(new HelloApplicationContext(PropertySource.create(System.getenv("PROFILES"))));
		start();
	}

	public void setContext(HelloApplicationContext helloApplicationContext) {
		this.helloApplicationContext = helloApplicationContext;

	}

	public void start() throws ServletException {
		DeploymentManager manager = Servlets.defaultContainer().addDeployment(createServletDeployment());
		manager.deploy();

		Undertow undertow = Undertow.builder()
			.addHttpListener(httpPort, "localhost")
			.setHandler(manager.start())
			.build();

		undertow.start();
		InetSocketAddress address = (InetSocketAddress) undertow.getListenerInfo().get(0).getAddress();
		this.actualPort = address.getPort();
	}

	private DeploymentInfo createServletDeployment() {
		return Servlets.deployment()
			.setDeploymentName(getClass().getName())
			.addServletContextAttribute("config", helloApplicationContext)
			.setContextPath("/")
			.setClassLoader(getClass().getClassLoader())
			.addServlet(Servlets.servlet(PersonController.class).addMapping("/person/*"));
	}

	public int getActualPort() {
		return actualPort;
	}

}
