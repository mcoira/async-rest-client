package com.ecomnext.rest;

public interface RestAPI {
    public RestClient client();

    public RestRequestHolder url(String url);
}
