package com.johannesbrodwall.winter.http.server;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.time.ZonedDateTime;

import org.junit.Test;

import com.johannesbrodwall.winter.IOUtil;
import com.johannesbrodwall.winter.http.server.jdk.JdkWebServer;

public class JdkHttpServerTest extends WebServerTest {

	@Test
	public void shouldCreateHttpResponderInstance() throws Exception {
		ZonedDateTime startTime = ZonedDateTime.now();

		WebServer server = new JdkWebServer();
		server.setPort(0);
		server.mapPathToResponder("/startTime", new StartTimeResponder(startTime));
		server.start();

		URL url = new URL("http", "localhost", server.getActualPort(), "/startTime");
		assertThat(IOUtil.toString(url)).isEqualTo("Started at " + startTime);
	}

}
