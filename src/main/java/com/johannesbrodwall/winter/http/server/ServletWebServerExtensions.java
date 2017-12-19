package com.johannesbrodwall.winter.http.server;

import javax.servlet.Servlet;

public interface ServletWebServerExtensions extends WebServerExtensions {

    void setServletAttribute(String name, Object value);

    void mapPathToServletClass(String path, Class<? extends Servlet> servlet);

}
