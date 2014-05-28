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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-java-ws/src/main/java/play/libs/ws/ning/NingWSRequest.java
 */
package com.ecomnext.rest.ning;

import com.ecomnext.rest.RestAuthScheme;
import com.ecomnext.rest.RestRequest;
import com.ecomnext.rest.RestResponse;
import com.ning.http.client.*;
import com.ning.http.client.generators.InputStreamBodyGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NingRestRequest implements RestRequest {
    private final FluentCaseInsensitiveStringsMap headers;
    private final String method;
    private final RequestBuilder builder;
    private final NingRestClient client;
    private final byte[] body;

    public NingRestRequest(NingRestClient client, String method, String url, Map<String, Collection<String>> queryString,
                         Map<String, Collection<String>> headers) {
        this(client, method, url, queryString, new FluentCaseInsensitiveStringsMap(headers), null);
    }

    public NingRestRequest(NingRestClient client, String method, String url, Map<String, Collection<String>> queryString,
                         FluentCaseInsensitiveStringsMap headers) {
        this(client, method, url, queryString, headers, null);
    }

    public NingRestRequest(NingRestClient client, String method, String url, Map<String, Collection<String>> queryString,
                         FluentCaseInsensitiveStringsMap headers, byte[] body) {
        this.client = client;
        this.builder = new RequestBuilder(method);
        this.method = method;
        this.headers = headers;
        this.body = body;
        builder.setUrl(url)
                .setQueryParameters(new FluentStringsMap(queryString))
                .setHeaders(headers);
    }

    /**
     * Return the headers of the request being constructed
     */
    @Override
    public Map<String, List<String>> getAllHeaders() {
        return headers;
    }

    @Override
    public List<String> getHeader(String name) {
        List<String> hdrs = headers.get(name);
        if (hdrs == null) return new ArrayList<>();
        return hdrs;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getUrl() {
        return builder.build().getUrl();
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public RestRequest auth(String username, String password, RestAuthScheme scheme) {
        Realm.AuthScheme authScheme = getAuthScheme(scheme);
        builder.setRealm((new Realm.RealmBuilder())
                .setScheme(authScheme)
                .setPrincipal(username)
                .setPassword(password)
                .setUsePreemptiveAuth(true)
                .build());
        return this;
    }

    @Override
    public CompletableFuture<RestResponse> execute() {
        final CompletableFuture<RestResponse> promise = new CompletableFuture<>();
//        final scala.concurrent.Promise<play.libs.ws.WSResponse> scalaPromise = scala.concurrent.Promise$.MODULE$.<play.libs.ws.WSResponse>apply();
        try {
            AsyncHttpClient asyncHttpClient = client.getUnderlying();
            asyncHttpClient.executeRequest(getBuilder().build(), new AsyncCompletionHandler<Response>() {
                @Override
                public Response onCompleted(Response response) {
                    promise.complete(new NingRestResponse(response));
//                    scalaPromise.success(new NingRestResponse(response));
                    return response;
                }
                @Override
                public void onThrowable(Throwable t) {
//                    scalaPromise.failure(t);
                    promise.completeExceptionally(t);
                }
            });
        } catch (IOException exception) {
//            scalaPromise.failure(exception);
            promise.completeExceptionally(exception);
        }
//        return new F.Promise<play.libs.ws.WSResponse>(scalaPromise.future());
        return promise;
    }

    NingRestRequest setBody(String body) {
        builder.setBody(body);
        return this;
    }

    NingRestRequest setBodyEncoding(String charset) {
        builder.setBodyEncoding(charset);
        return this;
    }

    NingRestRequest setBody(InputStream body) {
        builder.setBody(new InputStreamBodyGenerator(body));
        return this;
    }

    NingRestRequest setPerRequestConfig(PerRequestConfig config) {
        builder.setPerRequestConfig(config);
        return this;
    }

    NingRestRequest setFollowRedirects(Boolean followRedirects) {
        builder.setFollowRedirects(followRedirects);
        return this;
    }

    NingRestRequest setBody(File body) {
        builder.setBody(body);
        return this;
    }

    // intentionally package private.
    NingRestRequest setVirtualHost(String virtualHost) {
        builder.setVirtualHost(virtualHost);
        return this;
    }

    RequestBuilder getBuilder() {
        return builder;
    }


    Realm.AuthScheme getAuthScheme(RestAuthScheme scheme) {
        return Realm.AuthScheme.valueOf(scheme.name());
    }
}
