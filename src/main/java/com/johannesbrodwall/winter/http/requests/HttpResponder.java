package com.johannesbrodwall.winter.http.requests;

public interface HttpResponder {

    void handle(HttpActionSelector selector);

}
