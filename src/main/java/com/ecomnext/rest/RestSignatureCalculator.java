package com.ecomnext.rest;

/**
 * Sign a Rest call.
 */
public interface RestSignatureCalculator {
    /**
     * Sign a request
     */
    public void sign(RestRequest request);
}
