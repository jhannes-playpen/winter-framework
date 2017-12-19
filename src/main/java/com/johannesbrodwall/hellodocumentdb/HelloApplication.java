package com.johannesbrodwall.hellodocumentdb;

import java.util.Optional;

import com.johannesbrodwall.hellodocumentdb.person.PersonController;
import com.johannesbrodwall.winter.config.PropertySource;
import com.johannesbrodwall.winter.http.server.WebServer;
import com.johannesbrodwall.winter.http.server.tomcat.TomcatWebServer;

public class HelloApplication {

    public static void main(String[] args) throws Exception {
        int port = Optional.ofNullable(System.getenv("HTTP_PORT")).map(Integer::parseInt).orElse(8080);
        new HelloApplication(port).run(args);
    }

    private WebServer server;
    private HelloApplicationContext helloApplicationContext;

    public HelloApplication(int httpPort) {
        this.server = new TomcatWebServer(httpPort);
    }

    private void run(String[] args) throws Exception {
        setContext(new HelloApplicationContext(PropertySource.create(System.getenv("PROFILES"))));
        start();
        server.await();
    }

    public void setContext(HelloApplicationContext helloApplicationContext) {
        this.helloApplicationContext = helloApplicationContext;
    }

    public void start() throws Exception {
        server.mapPathToResponder("/person/*", new PersonController(helloApplicationContext));
        server.start();
    }

    public int getActualPort() {
        return server.getActualPort();
    }

}
