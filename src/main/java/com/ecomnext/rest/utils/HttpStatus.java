/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecomnext.rest.utils;

/**
 * All status codes defined in RFC1945 (HTTP/1.0), RFC2616 (HTTP/1.1), and
 * RFC2518 (WebDAV) are listed.
 *
 * @see <a href="http://www.iana.org/assignments/http-status-codes">HTTP Status Code Registry</a>
 * @see <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes">List of HTTP status codes - Wikipedia</a>
 */
public interface HttpStatus {

    // --- 1xx Informational ---

    /**
     * {@code 100 Continue} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.1.1">HTTP/1.1</a>
     */
    public static final int CONTINUE = 100;
    /**
     * {@code 101 Switching Protocols} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.1.2">HTTP/1.1</a>
     */
    public static final int SWITCHING_PROTOCOLS = 101;
    /**
     * {@code 102 Processing} (WebDAV - RFC 2518)
     * @see <a href="http://tools.ietf.org/html/rfc2518#section-10.1">WebDAV</a>
     */
    public static final int PROCESSING = 102;


    // --- 2xx Success ---

    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.1">HTTP/1.1</a>
     */
    public static final int OK = 200;
    /**
     * {@code 201 Created} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.2">HTTP/1.1</a>
     */
    public static final int CREATED = 201;
    /**
     * {@code 202 Accepted} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.3">HTTP/1.1</a>
     */
    public static final int ACCEPTED = 202;
    /**
     * {@code 203 Non-Authoritative Information} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.4">HTTP/1.1</a>
     */
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;
    /**
     * {@code 204 No Content} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.5">HTTP/1.1</a>
     */
    public static final int NO_CONTENT = 204;
    /**
     * {@code 205 Reset Content} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.6">HTTP/1.1</a>
     */
    public static final int RESET_CONTENT = 205;
    /**
     * {@code 206 Partial Content} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.2.7">HTTP/1.1</a>
     */
    public static final int PARTIAL_CONTENT = 206;
    /**
     * {@code 207 Multi-Status} (HTTP/1.1 - draft-ietf-http-v11-spec-rev-01?)
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-13">WebDAV</a>
     */
    public static final int MULTI_STATUS = 207;

    // --- 3xx Redirection ---

    /**
     * {@code 300 Multiple Choices} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.1">HTTP/1.1</a>
     */
    public static final int MULTIPLE_CHOICES = 300;
    /**
     * {@code 301 Moved Permanently} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.2">HTTP/1.1</a>
     */
    public static final int MOVED_PERMANENTLY = 301;
    /**
     * {@code 302 Moved Temporarily} which has been deprecated in favor of {@code Found}.
     * @see <a href="http://tools.ietf.org/html/rfc1945#section-9.3">HTTP/1.0</a>
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.3">HTTP/1.1</a>
     */
    public static final int MOVED_TEMPORARILY = 302;
    /**
     * {@code 303 See Other} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.4">HTTP/1.1</a>
     */
    public static final int SEE_OTHER = 303;
    /**
     * {@code 304 Not Modified} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.5">HTTP/1.1</a>
     */
    public static final int NOT_MODIFIED = 304;
    /**
     * {@code 305 Use Proxy} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.6">HTTP/1.1</a>
     */
    public static final int USE_PROXY = 305;
    /**
     * {@code 307 Temporary Redirect} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.3.8">HTTP/1.1</a>
     */
    public static final int TEMPORARY_REDIRECT = 307;

    // --- 4xx Client Error ---

    /**
     * {@code 400 Bad Request} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.1">HTTP/1.1</a>
     */
    public static final int BAD_REQUEST = 400;
    /**
     * {@code 401 Unauthorized} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.2">HTTP/1.1</a>
     */
    public static final int UNAUTHORIZED = 401;
    /**
     * {@code 402 Payment Required} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.3">HTTP/1.1</a>
     */
    public static final int PAYMENT_REQUIRED = 402;
    /**
     * {@code 403 Forbidden} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.4">HTTP/1.1</a>
     */
    public static final int FORBIDDEN = 403;
    /**
     * {@code 404 Not Found} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.5">HTTP/1.1</a>
     */
    public static final int NOT_FOUND = 404;
    /**
     * {@code 405 Method Not Allowed} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.6">HTTP/1.1</a>
     */
    public static final int METHOD_NOT_ALLOWED = 405;
    /**
     * {@code 406 Not Acceptable} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.7">HTTP/1.1</a>
     */
    public static final int NOT_ACCEPTABLE = 406;
    /**
     * {@code 407 Proxy Authentication Required} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.8">HTTP/1.1</a>
     */
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    /**
     * {@code 408 Request Timeout} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.9">HTTP/1.1</a>
     */
    public static final int REQUEST_TIMEOUT = 408;
    /**
     * {@code 409 Conflict} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.10">HTTP/1.1</a>
     */
    public static final int CONFLICT = 409;
    /**
     * {@code 410 Gone} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.11">HTTP/1.1</a>
     */
    public static final int GONE = 410;
    /**
     * {@code 411 Length Required} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.12">HTTP/1.1</a>
     */
    public static final int LENGTH_REQUIRED = 411;
    /**
     * {@code 412 Precondition failed} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.13">HTTP/1.1</a>
     */
    public static final int PRECONDITION_FAILED = 412;
    /**
     * {@code 413 Request Entity Too Large} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.14">HTTP/1.1</a>
     */
    public static final int REQUEST_TOO_LONG = 413;
    /**
     * {@code 414 Request-URI Too Long} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.15">HTTP/1.1</a>
     */
    public static final int REQUEST_URI_TOO_LONG = 414;
    /**
     * {@code 415 Unsupported Media Type} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.16">HTTP/1.1</a>
     */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;
    /**
     * {@code 416 Requested Range Not Satisfiable} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.17">HTTP/1.1</a>
     */
    public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    /**
     * {@code 417 Expectation Failed} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.18">HTTP/1.1</a>
     */
    public static final int EXPECTATION_FAILED = 417;
    /**
     * {@code 419 Insufficient Space on Resource} (WebDAV)
     * @deprecated See <a href="http://tools.ietf.org/rfcdiff?difftype=--hwdiff&url2=draft-ietf-webdav-protocol-06.txt">WebDAV Draft Changes</a>
     */
    public static final int INSUFFICIENT_SPACE_ON_RESOURCE = 419;
    /**
     * {@code 420 Method Failure} (WebDAV)
     * @deprecated See <a href="http://tools.ietf.org/rfcdiff?difftype=--hwdiff&url2=draft-ietf-webdav-protocol-06.txt">WebDAV Draft Changes</a>
     */
    public static final int METHOD_FAILURE = 420;
    /**
     * {@code 422 Unprocessable Entity} (WebDAV - RFC 2518)
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.2">WebDAV</a>
     */
    public static final int UNPROCESSABLE_ENTITY = 422;
    /**
     * {@code 423 Locked} (WebDAV - RFC 2518)
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.3">WebDAV</a>
     */
    public static final int LOCKED = 423;
    /**
     * {@code 424 Failed Dependency} (WebDAV - RFC 2518)
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.4">WebDAV</a>
     */
    public static final int FAILED_DEPENDENCY = 424;

    // --- 5xx Server Error ---

    /**
     * {@code 500 Internal Server Error} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.1">HTTP/1.1</a>
     */
    public static final int INTERNAL_SERVER_ERROR = 500;
    /**
     * {@code 501 Not Implemented} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.2">HTTP/1.1</a>
     */
    public static final int NOT_IMPLEMENTED = 501;
    /**
     * {@code 502 Bad Gateway} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.3">HTTP/1.1</a>
     */
    public static final int BAD_GATEWAY = 502;
    /**
     * {@code 503 Service Unavailable} (HTTP/1.0 - RFC 1945)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.4">HTTP/1.1</a>
     */
    public static final int SERVICE_UNAVAILABLE = 503;
    /**
     * {@code 504 Gateway Timeout} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.5">HTTP/1.1</a>
     */
    public static final int GATEWAY_TIMEOUT = 504;
    /**
     * {@code 505 HTTP Version Not Supported} (HTTP/1.1 - RFC 2616)
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.5.6">HTTP/1.1</a>
     */
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * {@code 507 Insufficient Storage} (WebDAV - RFC 2518)
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.5">WebDAV</a>
     */
    public static final int INSUFFICIENT_STORAGE = 507;

}
