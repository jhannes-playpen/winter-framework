package com.johannesbrodwall.winter.http.server;

import javax.servlet.Servlet;

import com.johannesbrodwall.winter.http.requests.HttpResponder;

public interface WebServer {

	void setPort(int port);

	int getActualPort();

	void mapPathToServletClass(String path, Class<? extends Servlet> servletClass);

	void start() throws Exception;

	WebServerExtensions getExtensions();

	void mapPathToResponder(String path, HttpResponder responder);

	void await();

}
