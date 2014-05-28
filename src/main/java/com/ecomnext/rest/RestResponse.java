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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-java-ws/src/main/java/play/libs/ws/WSResponse.java
 */
package com.ecomnext.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.cookie.Cookie;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface RestResponse {
    Map<String, List<String>> getAllHeaders();

    Object getUnderlying();

    int getStatus();

    String getStatusText();

    String getHeader(String key);

    List<Cookie> getCookies();

    Cookie getCookie(String name);

    String getBody();

    Document asXml();

    JsonNode asJson();

    InputStream getBodyAsStream();

    byte[] asByteArray();

    URI getUri();
}
