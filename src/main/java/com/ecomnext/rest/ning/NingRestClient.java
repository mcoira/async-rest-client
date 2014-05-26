package com.ecomnext.rest.ning;

import com.ecomnext.rest.RestClient;
import com.ecomnext.rest.RestRequestHolder;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

/**
 *
 */
public class NingRestClient implements RestClient {

    private AsyncHttpClient asyncHttpClient;

    public NingRestClient(AsyncHttpClientConfig config) {
        this.asyncHttpClient = new AsyncHttpClient(config);
    }

    @Override
    public AsyncHttpClient getUnderlying() {
        return asyncHttpClient;
    }

    @Override
    public RestRequestHolder url(String url) {
        return new NingRestRequestHolder(this, url);
    }

    @Override
    public RestRequestHolder url(String url, String... params) {
        return new NingRestRequestHolder(this, url, params);
    }

    protected void close() {
        this.asyncHttpClient.close();
    }
}
