package com.johannesbrodwall.hellodocumentdb;

import java.util.Optional;

import com.johannesbrodwall.hellodocumentdb.person.PersonController;
import com.johannesbrodwall.winter.config.PropertySource;
import com.johannesbrodwall.winter.http.server.ServletWebServer;
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
		ServletWebServer server = new TomcatWebServer();
		server.setPort(httpPort);
		//server.mapPathToResponder("/person/*", new PersonController(helloApplicationContext));
		server.getExtensions().setServletAttribute("config", helloApplicationContext);
		server.getExtensions().mapPathToServletClass("/person/*", PersonController.class);
		server.start();
		this.actualPort = server.getActualPort();
		return server;
	}

	public int getActualPort() {
		return actualPort;
	}

}
