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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-java-ws/src/main/java/play/libs/ws/WSRequestHolder.java
 */
package com.ecomnext.rest;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RestRequestHolder {
    String getUsername();

    String getPassword();

    RestAuthScheme getScheme();

    RestSignatureCalculator getCalculator();

    int getTimeout();

    Boolean getFollowRedirects();

    CompletableFuture<RestResponse> get();

    CompletableFuture<RestResponse> patch(String body);

    CompletableFuture<RestResponse> post(String body);

    CompletableFuture<RestResponse> put(String body);

    CompletableFuture<RestResponse> patch(JsonNode body);

    CompletableFuture<RestResponse> post(JsonNode body);

    CompletableFuture<RestResponse> put(JsonNode body);

    CompletableFuture<RestResponse> patch(InputStream body);

    CompletableFuture<RestResponse> post(InputStream body);

    CompletableFuture<RestResponse> put(InputStream body);

    CompletableFuture<RestResponse> post(File body);

    CompletableFuture<RestResponse> put(File body);

    CompletableFuture<RestResponse> delete();

    CompletableFuture<RestResponse> head();

    CompletableFuture<RestResponse> options();

    CompletableFuture<RestResponse> execute(String method);

    /**
     * Execute the request
     */
    CompletableFuture<RestResponse> execute();

    /**
     * Set the method this request should use.
     */
    RestRequestHolder setMethod(String method);

    /**
     * Set the body this request should use
     */
    RestRequestHolder setBody(String body);

    /**
     * Set the body this request should use
     */
    RestRequestHolder setBody(JsonNode body);

    /**
     * Set the body this request should use
     */
    RestRequestHolder setBody(InputStream body);

    /**
     * Set the body this request should use
     */
    RestRequestHolder setBody(File body);

    RestRequestHolder setHeader(String name, String value);

    RestRequestHolder setQueryString(String query);

    RestRequestHolder setQueryParameter(String name, String value);

    RestRequestHolder setAuth(String userInfo);

    RestRequestHolder setAuth(String username, String password);

    RestRequestHolder setAuth(String username, String password, RestAuthScheme scheme);

    RestRequestHolder sign(RestSignatureCalculator calculator);

    RestRequestHolder setFollowRedirects(Boolean followRedirects);

    RestRequestHolder setVirtualHost(String virtualHost);

    RestRequestHolder setTimeout(int timeout);

    RestRequestHolder setContentType(String contentType);

    String getUrl();

    Map<String, Collection<String>> getHeaders();

    Map<String, Collection<String>> getQueryParameters();
}
