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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-java-ws/src/main/java/play/libs/ws/WS.java
 */
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

    /**
     * Close the underlying connections of the current client if exist and replace it with a new
     * one which has the config received.
     */
    public static void configClient(AsyncHttpClientConfig httpClientConfig) {
        restApi.setClient(httpClientConfig);
    }

    /** Close the underlying connections. */
    public static void close() {
        restApi.resetClient();
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
