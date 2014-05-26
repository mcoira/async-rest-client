package com.ecomnext.rest;

import com.ecomnext.rest.ning.NingRestAPI;
import com.ning.http.client.AsyncHttpClientConfig;

/**
 * Asynchronous API to to query REST services, as an http client.
 * The value returned is a {@code CompletableFuture<Response>},
 * in order to let you use asynchronous mechanisms.
 */
public class Rest {
    private static final RestAPI restApi = new NingRestAPI();

    public void setClient(AsyncHttpClientConfig httpClientConfig) {
        restApi.setClient(httpClientConfig);
    }

    public static RestClient client() {
        return restApi.client();
    }

    /**
     * Prepare a new request which you can complete chaining methods.
     *
     * @param url the URL to request
     */
    public static RestRequestHolder url(String url) {
        return client().url(url);
    }

    public static RestRequestHolder url(String url, String... params) {
        return client().url(url, params);
    }

}
