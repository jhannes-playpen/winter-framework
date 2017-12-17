package com.johannesbrodwall.hellodocumentdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import com.johannesbrodwall.hellodocumentdb.HelloApplication;
import com.johannesbrodwall.hellodocumentdb.HelloApplicationContext;
import com.johannesbrodwall.winter.config.PropertySource;


public class PersonControllerTest {

	private static Integer serverPort = 9090;

	@BeforeClass
	public static void startServer() throws Exception {
		HelloApplication application = new HelloApplication(0);
		HelloApplicationContext context = new HelloApplicationContext(PropertySource.create(new File("."), "unittest,mongo"));
		application.setContext(context);
		application.start();
		serverPort = application.getActualPort();

	}

	@Test
	public void shouldPostPerson() throws Exception {
		String url = "http://localhost:" + serverPort + "/person";

		String id = postPersonName(url, "Johannes Brodwall");
		String name = getPersonName(url + "/" + id);

		assertThat(name).isEqualTo("Johannes Brodwall");
	}

	private String getPersonName(String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		String response;
		try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			response = input.lines().collect(Collectors.joining("\n"));
		}

		return new JSONObject(response).getString("name");
	}

	private String postPersonName(String url, String name) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setDoOutput(true);

		try (OutputStream output = connection.getOutputStream()) {
			output.write(("name=" + name).getBytes());
		}

		assertThat(connection.getResponseCode()).as(connection.getResponseMessage()).isEqualTo(200);

		String response;
		try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			response = input.lines().collect(Collectors.joining("\n"));
		}

		return new JSONObject(response).getString("id");
	}

}
