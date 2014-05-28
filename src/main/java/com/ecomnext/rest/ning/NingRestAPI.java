/*
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-java-ws/src/main/java/play/libs/ws/ning/NingWSAPI.java
 */
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
        AsyncHttpClientConfig httpClientConfig = new AsyncHttpClientConfig.Builder().build();
        return new NingRestClient(httpClientConfig);
    }

    public void setClient(AsyncHttpClientConfig httpClientConfig) {
        clientHolder.getAndSet(Optional.of(new NingRestClient(httpClientConfig)))
                .ifPresent(NingRestClient::close);
    }

    /**
     * resets the underlying AsyncHttpClient
     */
    public void resetClient() {
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
