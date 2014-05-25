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
