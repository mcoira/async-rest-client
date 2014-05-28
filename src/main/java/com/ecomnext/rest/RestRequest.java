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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-java-ws/src/main/java/play/libs/ws/WSRequest.java
 */
package com.ecomnext.rest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface RestRequest {
    CompletableFuture<RestResponse> execute();

    Map<String, List<String>> getAllHeaders();

    String getMethod();

    List<String> getHeader(String name);

    String getUrl();

    /**
     * @return The body, if and only if it's an in memory body, otherwise returns null.
     */
    byte[] getBody();

    public RestRequest auth(String username, String password, RestAuthScheme scheme);
}
