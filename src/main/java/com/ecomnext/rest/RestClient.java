package com.ecomnext.rest;

import com.ning.http.client.AsyncHttpClient;

public interface RestClient {
    public AsyncHttpClient getUnderlying();

    RestRequestHolder url(String url);
}
