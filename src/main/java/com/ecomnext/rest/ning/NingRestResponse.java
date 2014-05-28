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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-java-ws/src/main/java/play/libs/ws/ning/NingWSResponse.java
 */
package com.ecomnext.rest.ning;

import com.ecomnext.rest.RestResponse;
import com.ecomnext.rest.utils.Json;
import com.ecomnext.rest.utils.XML;
import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.Response;
import com.ning.http.client.cookie.Cookie;
import com.ning.http.util.AsyncHttpProviderUtils;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;


public class NingRestResponse implements RestResponse {
    private Response ahcResponse;

    public NingRestResponse(Response ahcResponse) {
        this.ahcResponse = ahcResponse;
    }

    @Override
    public Object getUnderlying() {
        return this.ahcResponse;
    }

    /**
     * Get the HTTP status code of the response
     */
    @Override
    public int getStatus() {
        return ahcResponse.getStatusCode();
    }

    /**
     * Get the HTTP status text of the response
     */
    @Override
    public String getStatusText() {
        return ahcResponse.getStatusText();
    }

    /**
     * Get all the HTTP headers of the response as a case-insensitive map
     */
    @Override
    public Map<String, List<String>> getAllHeaders() {
        return ahcResponse.getHeaders();
    }

    /**
     * Get the given HTTP header of the response
     */
    @Override
    public String getHeader(String key) {
        return ahcResponse.getHeader(key);
    }

    /**
     * Get all the cookies.
     */
    @Override
    public List<Cookie> getCookies() {
        return ahcResponse.getCookies();
    }

    /**
     * Get only one cookie, using the cookie name.
     */
    @Override
    public Cookie getCookie(String name) {
        for (Cookie cookie : ahcResponse.getCookies()) {
            // safe -- cookie.getName() will never return null
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Get the response body as a string.  If the charset is not specified, this defaults to ISO-8859-1 for text
     * sub mime types, as per RFC-2616 sec 3.7.1, otherwise it defaults to UTF-8.
     */
    @Override
    public String getBody() {
        try {
            // RFC-2616#3.7.1 states that any text/* mime type should default to ISO-8859-1 charset if not
            // explicitly set, while Plays default encoding is UTF-8.  So, use UTF-8 if charset is not explicitly
            // set and content type is not text/*, otherwise default to ISO-8859-1
            String contentType = ahcResponse.getContentType();
            if (contentType == null) {
                // As defined by RFC-2616#7.2.1
                contentType = "application/octet-stream";
            }
            String charset = AsyncHttpProviderUtils.parseCharset(contentType);

            if (charset != null) {
                return ahcResponse.getResponseBody(charset);
            } else if (contentType.startsWith("text/")) {
                return ahcResponse.getResponseBody(AsyncHttpProviderUtils.DEFAULT_CHARSET);
            } else {
                return ahcResponse.getResponseBody("utf-8");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the response body as a {@link org.w3c.dom.Document DOM document}
     * @return a DOM document
     */
    @Override
    public Document asXml() {
        try {
            return XML.fromInputStream(ahcResponse.getResponseBodyAsStream(), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the response body as a {@link com.fasterxml.jackson.databind.JsonNode}
     * @return the json response
     */
    @Override
    public JsonNode asJson() {
        try {
            // Jackson will automatically detect the correct encoding according to the rules in RFC-4627
            return Json.parse(ahcResponse.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the response body as a stream
     * @return The stream to read the response body from
     */
    @Override
    public InputStream getBodyAsStream() {
        try {
            return ahcResponse.getResponseBodyAsStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the response body as a byte array
     * @return The byte array
     */
    @Override
    public byte[] asByteArray() {
        try {
            return ahcResponse.getResponseBodyAsBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the request {@link java.net.URI}. Note that if the request got redirected, the value of the
     * {@link java.net.URI} will be the last valid redirect url.
     *
     * @return the request {@link java.net.URI}.
     */
    @Override
    public URI getUri() {
        try {
            return ahcResponse.getUri();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
