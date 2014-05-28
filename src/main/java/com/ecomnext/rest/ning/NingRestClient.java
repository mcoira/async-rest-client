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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-java-ws/src/main/java/play/libs/ws/ning/NingWSClient.java
 */
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
