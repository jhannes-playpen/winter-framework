package com.johannesbrodwall.winter.http.server.jdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.JSONObject;

import com.johannesbrodwall.winter.ExceptionUtil;
import com.johannesbrodwall.winter.http.requests.HttpAction;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class JdkHttpAction implements HttpAction {

    private HttpExchange exchange;
    private HashMap<String, String> pathParameters;

    public JdkHttpAction(HttpExchange exchange, HashMap<String, String> pathParameters) {
        this.exchange = exchange;
        this.pathParameters = pathParameters;
    }

    @Override
    public void sendNotFound() {
        try {
            String message = "404 Not Found - " + exchange.getRequestURI().getPath();
            exchange.sendResponseHeaders(404, message.getBytes().length);
            exchange.getResponseBody().write(message.getBytes());
            exchange.getResponseBody().close();
        } catch (IOException e) {
            throw ExceptionUtil.soften(e);
        }
    }

    @Override
    public String parameter(String name) {
        String query = exchange.getRequestURI().getQuery();
        String[] parts = query.split("&"); // TODO! Handle &amp; &gt; &lt;
        for (String string : parts) {
            int pos = string.indexOf("=");
            // TODO: URLDecode
            if (pos == -1) {
                String paramName = urlDecode(string);
                if (paramName.equals(name)) {
                    return "";
                }
            } else {
                String paramName = urlDecode(string.substring(0, pos));
                if (paramName.equals(name)) {
                    return urlDecode(string.substring(pos + 1));
                }
            }
        }
        return null;
    }

    @Override
    public String pathVariable(String name) {
        return pathParameters.get(name);
    }

    @Override
    public void returnObject(Object object) {
        JSONObject jsonObject = object instanceof JSONObject ? (JSONObject) object : new JSONObject(object);

        String string = jsonObject.toString();
        sendContent("application/json", string.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void returnString(String string) {
        sendContent("text/plain", string.getBytes(StandardCharsets.UTF_8));
    }

    private String urlDecode(String string) {
        try {
            return URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Should never happen - URLDecoder guarantees UTF-8 support");
        }
    }

    private void sendContent(String contentType, byte[] bytes) {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        try {
            exchange.sendResponseHeaders(0, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
        } catch (IOException e) {
            throw ExceptionUtil.soften(e);
        }
    }

}
