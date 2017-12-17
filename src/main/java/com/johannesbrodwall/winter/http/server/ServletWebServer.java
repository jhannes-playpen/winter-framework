package com.johannesbrodwall.winter.http.server;

public interface ServletWebServer extends WebServer {

	@Override
	ServletWebServerExtensions getExtensions();

}
