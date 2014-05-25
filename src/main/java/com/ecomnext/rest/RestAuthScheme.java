package com.ecomnext.rest;

/**
 * Supported authentication schemas.
 */
public enum RestAuthScheme {
    DIGEST,
    BASIC,
    NTLM,
    SPNEGO,
    KERBEROS,
    NONE
}
