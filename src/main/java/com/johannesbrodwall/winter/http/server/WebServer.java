package com.johannesbrodwall.winter.http.server;

import com.johannesbrodwall.winter.http.requests.HttpResponder;

public interface WebServer {

    int getActualPort();

    void start() throws Exception;

    WebServerExtensions getExtensions();

    void mapPathToResponder(String path, HttpResponder responder);

    void await();

}
