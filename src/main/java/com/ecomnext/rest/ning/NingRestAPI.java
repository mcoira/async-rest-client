package com.ecomnext.rest.ning;

import com.ecomnext.rest.RestAPI;
import com.ecomnext.rest.RestClient;
import com.ecomnext.rest.RestRequestHolder;
import com.ning.http.client.AsyncHttpClientConfig;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class NingRestAPI implements RestAPI {
    private final AtomicReference<Optional<NingRestClient>> clientHolder =
            new AtomicReference<>(Optional.<NingRestClient>empty());

    private NingRestClient newClient() {
        AsyncHttpClientConfig httpClientConfig = new AsyncHttpClientConfig.Builder().build();;
        return new NingRestClient(httpClientConfig);
    }

    /**
     * resets the underlying AsyncHttpClient
     */
    protected void resetClient() {
        clientHolder.getAndSet(Optional.empty()).ifPresent(NingRestClient::close);
    }

    @Override
    public synchronized RestClient client() {
        Optional<NingRestClient> clientOption = clientHolder.get();
        if (clientOption.isPresent()) {
            return clientOption.get();
        } else {
            NingRestClient client = newClient();
            clientHolder.set(Optional.of(client));
            return client;
        }
    }

    @Override
    public RestRequestHolder url(String url) {
        return client().url(url);
    }
}
