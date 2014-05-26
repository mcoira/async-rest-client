package com.ecomnext.rest;

import com.ning.http.client.AsyncHttpClientConfig;

public interface RestAPI {
    public RestClient client();

    public void setClient(AsyncHttpClientConfig httpClientConfig);

    public RestRequestHolder url(String url);
}
