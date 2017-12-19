package com.johannesbrodwall.winter.http.requests;

public interface HttpAction {

    void sendNotFound();

    String parameter(String name);

    String pathVariable(String name);

    void returnObject(Object jsonObject);

    void returnString(String string);

}