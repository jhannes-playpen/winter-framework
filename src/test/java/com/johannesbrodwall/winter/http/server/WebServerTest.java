package com.johannesbrodwall.winter.http.server;

import java.io.IOException;
import java.time.ZonedDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.johannesbrodwall.winter.http.requests.HttpActionSelector;
import com.johannesbrodwall.winter.http.requests.HttpResponder;

public class WebServerTest {

    public class StartTimeResponder implements HttpResponder {

        private ZonedDateTime startTime;

        public StartTimeResponder(ZonedDateTime startTime) {
            this.startTime = startTime;
        }

        @Override
        public void handle(HttpActionSelector selector) {
            selector.onGet("/", e -> {
                e.returnString("Started at " + startTime);
            });
        }
    }

    public static class GreeterResponder implements HttpResponder {

        @Override
        public void handle(HttpActionSelector selector) {
            selector.onGet("/greet/{user}", a -> {
                String name = a.pathVariable("user");
                String greeting = a.parameter("greeting");
                a.returnString(greeting + ", " + name);
            });
            selector.onGet("/foo", a -> {
                a.returnObject(new JSONObject().put("foo", "bar"));
            });
            selector.onNoMatch(a -> {
                a.sendNotFound();
            });
        }
    }

    public static class HelloServlet extends HttpServlet {

        private String recipient;

        public HelloServlet() {
            this.recipient = "world";
        }

        public HelloServlet(String recipient) {
            this.recipient = recipient;
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.getWriter().write("Hello " + this.recipient);
        }

    }

}
