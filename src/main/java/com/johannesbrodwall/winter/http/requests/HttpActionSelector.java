package com.johannesbrodwall.winter.http.requests;

import java.util.function.Consumer;

public interface HttpActionSelector {

    void onGet(String path, Consumer<HttpAction> action);

    void onPost(String path, Consumer<HttpAction> action);

    void onNoMatch(Consumer<HttpAction> action);

}