package com.johannesbrodwall.winter.http.server;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.time.ZonedDateTime;

import org.junit.Test;

import com.johannesbrodwall.winter.IOUtil;

public class TomcatHttpServerTest extends WebServerTest {

	@Test
	public void shouldCreateServletClassInstance() throws Exception {
		WebServer server = new TomcatWebServer();
		server.setPort(0);
		server.mapPathToServletClass("/hello", HelloServlet.class);
		server.start();

		URL url = new URL("http", "localhost", server.getActualPort(), "/hello");
		assertThat(IOUtil.toString(url)).isEqualTo("Hello world");
	}

	@Test
	public void shouldCreateServletInstance() throws Exception {
		TomcatWebServer server = new TomcatWebServer();
		server.setPort(0);
		server.getExtensions().addPathToServletInstance("/hello", new HelloServlet("dear test instance"));
		server.start();

		URL url = new URL("http", "localhost", server.getActualPort(), "/hello");
		assertThat(IOUtil.toString(url)).isEqualTo("Hello dear test instance");
	}

	@Test
	public void shouldCreateHttpResponderInstance() throws Exception {
		ZonedDateTime startTime = ZonedDateTime.now();

		WebServer server = new TomcatWebServer();
		server.setPort(0);
		server.mapPathToResponder("/startTime", new StartTimeResponder(startTime));
		server.start();

		URL url = new URL("http", "localhost", server.getActualPort(), "/startTime");
		assertThat(IOUtil.toString(url)).isEqualTo("Started at " + startTime);
	}

}
