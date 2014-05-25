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
