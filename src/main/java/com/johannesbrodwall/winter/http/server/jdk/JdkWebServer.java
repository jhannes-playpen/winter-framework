package com.johannesbrodwall.winter.http.server.jdk;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.johannesbrodwall.winter.ExceptionUtil;
import com.johannesbrodwall.winter.http.requests.HttpResponder;
import com.johannesbrodwall.winter.http.server.WebServer;
import com.johannesbrodwall.winter.http.server.WebServerExtensions;

@SuppressWarnings("restriction")
public class JdkWebServer implements WebServer {

    private com.sun.net.httpserver.HttpServer server;

    public JdkWebServer() {
        try {
            server = com.sun.net.httpserver.HttpServer.create();
        } catch (IOException e) {
            throw ExceptionUtil.soften(e);
        }
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
        String pathTranslated = sanitizePath(path);
        server.createContext(pathTranslated, exchange -> {
            JdkHttpActionSelector selector = new JdkHttpActionSelector(pathTranslated, exchange);
            responder.handle(selector);
            selector.onNoMatch(a -> a.sendNotFound());
        });
    }

    private String sanitizePath(String path) {
        String pathTranslated = path;
        if (path.endsWith("/*")) {
            pathTranslated = path.substring(0, path.length() - 2);
        }
        if (pathTranslated.isEmpty()) {
            pathTranslated = "/";
        }
        return pathTranslated;
    }

    @Override
    public void await() {
    }

}
