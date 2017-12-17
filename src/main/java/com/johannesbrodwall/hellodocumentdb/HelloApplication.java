package com.johannesbrodwall.hellodocumentdb;

import java.util.Optional;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.johannesbrodwall.hellodocumentdb.person.PersonController;
import com.johannesbrodwall.winter.config.PropertySource;
import com.johannesbrodwall.winter.http.server.TomcatWebServer;
import com.johannesbrodwall.winter.http.server.WebServer;

public class HelloApplication {

	private int httpPort;
	private int actualPort;
	private HelloApplicationContext helloApplicationContext;

	public HelloApplication(int httpPort) {
		this.httpPort = httpPort;
	}

	public static void main(String[] args) throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		int port = Optional.ofNullable(System.getenv("HTTP_PORT")).map(Integer::parseInt).orElse(8080);

		new HelloApplication(port).run(args);
	}

	private void run(String[] args) throws Exception {
		setContext(new HelloApplicationContext(PropertySource.create(System.getenv("PROFILES"))));
		WebServer server = start();
		server.await();
	}

	public void setContext(HelloApplicationContext helloApplicationContext) {
		this.helloApplicationContext = helloApplicationContext;
	}

	public WebServer start() throws Exception {
		WebServer server = new TomcatWebServer();
		server.setPort(httpPort);
		server.getExtensions().setServletAttribute("config", helloApplicationContext);
		server.mapPathToServletClass("/person/*", PersonController.class);
		server.start();
		this.actualPort = server.getActualPort();
		return server;
	}

	public int getActualPort() {
		return actualPort;
	}

}
