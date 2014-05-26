package com.ecomnext.rest;

import com.damnhandy.uri.template.UriTemplate;
import com.ning.http.client.AsyncHttpClient;

public interface RestClient {
    public AsyncHttpClient getUnderlying();

    RestRequestHolder url(String url);

    RestRequestHolder url(String url, String... params);
}
