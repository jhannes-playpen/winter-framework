package com.johannesbrodwall.winter.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.johannesbrodwall.winter.ExceptionUtil;
import com.johannesbrodwall.winter.http.requests.HttpResponder;

@SuppressWarnings("restriction")
public class JdkWebServer implements WebServer {

	private com.sun.net.httpserver.HttpServer server;

	public JdkWebServer() throws IOException {
		server = com.sun.net.httpserver.HttpServer.create();
	}

	@Override
	public void setPort(int port) {
		try {
			server.bind(new InetSocketAddress(port), 0);
		} catch (IOException e) {
			throw ExceptionUtil.soften(e);
		}
	}

	@Override
	public int getActualPort() {
		return server.getAddress().getPort();
	}

	@Override
	public void start() throws Exception {
		server.start();
	}

	@Override
	public WebServerExtensions getExtensions() {
		return null;
	}

	@Override
	public void mapPathToResponder(String path, HttpResponder responder) {
		server.createContext(path, new HttpResponderHander(path, responder));
	}

	@Override
	public void await() {
	}

}
