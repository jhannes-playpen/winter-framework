package com.johannesbrodwall.winter.http.server;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZonedDateTime;

import org.junit.Test;

import com.johannesbrodwall.winter.IOUtil;
import com.johannesbrodwall.winter.http.server.jdk.JdkWebServer;

public class JdkHttpServerTest extends WebServerTest {

    @Test
    public void shouldCreateHttpResponderInstance() throws Exception {
        ZonedDateTime startTime = ZonedDateTime.now();

        WebServer server = new JdkWebServer(0);
        server.mapPathToResponder("/startTime/*", new StartTimeResponder(startTime));
        server.start();

        URL url = new URL("http", "localhost", server.getActualPort(), "/startTime");
        assertThat(IOUtil.toString(url)).isEqualTo("Started at " + startTime);
    }

    @Test
    public void shouldReadParameters() throws Exception {
        WebServer server = new JdkWebServer(0);
        server.mapPathToResponder("/*", new GreeterResponder());
        server.start();

        URL url = new URL("http://localhost:" + server.getActualPort() + "/greet/johannes?greeting=Merry+Christmas");
        assertThat(IOUtil.toString(url)).isEqualTo("Merry Christmas, johannes");

        url = new URL("http://localhost:" + server.getActualPort() + "/foo");
        assertThat(IOUtil.toString(url)).isEqualTo("{\"foo\":\"bar\"}");
    }

    @Test
    public void shouldReturn404ForNotFound() throws Exception {
        WebServer server = new JdkWebServer(0);
        server.mapPathToResponder("/startTime/*", new StartTimeResponder(null));
        server.start();

        URL url = new URL("http", "localhost", server.getActualPort(), "/startTime/nonsense");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        assertThat(connection.getResponseCode()).isEqualTo(404);
    }

}
