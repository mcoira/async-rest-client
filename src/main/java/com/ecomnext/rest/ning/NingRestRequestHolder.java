package com.ecomnext.rest.ning;

import com.damnhandy.uri.template.MalformedUriTemplateException;
import com.damnhandy.uri.template.UriTemplate;
import com.damnhandy.uri.template.VariableExpansionException;
import com.ecomnext.rest.RestAuthScheme;
import com.ecomnext.rest.RestRequestHolder;
import com.ecomnext.rest.RestResponse;
import com.ecomnext.rest.RestSignatureCalculator;
import com.ecomnext.rest.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.PerRequestConfig;
import com.ning.http.util.AsyncHttpProviderUtils;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class NingRestRequestHolder implements RestRequestHolder {
    
    private final String url;
    private String method = "GET";
    private Object body = null;
    private Map<String, Collection<String>> headers = new HashMap<>();
    private Map<String, Collection<String>> queryParameters = new HashMap<>();

    private String username = null;
    private String password = null;
    private RestAuthScheme scheme = null;
    private RestSignatureCalculator calculator = null;
    private NingRestClient client = null;

    private int timeout = 0;
    private Boolean followRedirects = null;
    private String virtualHost = null;

    public NingRestRequestHolder(NingRestClient client, String url) {
        try {
            this.client = client;
            URL reference = new URL(url);

            this.url = url;

            String userInfo = reference.getUserInfo();
            if (userInfo != null) {
                this.setAuth(userInfo);
            }
            if (reference.getQuery() != null) {
                this.setQueryString(reference.getQuery());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    public NingRestRequestHolder(NingRestClient client, String url, String... params) {
        try {
            this.client = client;

            UriTemplate uriTemplate = UriTemplate.fromTemplate(url);
            if (uriTemplate.getVariables().length != params.length) {
                throw new IllegalArgumentException("The number of variables in the URL and the number of values do not match");
            }

            for (int i = 0; i < params.length; i++) {
                uriTemplate.set(uriTemplate.getVariables()[i], params[i]);
            }
            url = uriTemplate.expand();

            URL reference = new URL(url);
            this.url = url;

            String userInfo = reference.getUserInfo();
            if (userInfo != null) {
                this.setAuth(userInfo);
            }
            if (reference.getQuery() != null) {
                this.setQueryString(reference.getQuery());
            }
        } catch (MalformedURLException | MalformedUriTemplateException | VariableExpansionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a header with the given name, this can be called repeatedly.
     */
    @Override
    public NingRestRequestHolder setHeader(String name, String value) {
        if (headers.containsKey(name)) {
            Collection<String> values = headers.get(name);
            values.add(value);
        } else {
            List<String> values = new ArrayList<>();
            values.add(value);
            headers.put(name, values);
        }
        return this;
    }

    /**
     * Sets a query string.
     */
    @Override
    public RestRequestHolder setQueryString(String query) {
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length > 2) {
                throw new RuntimeException(new MalformedURLException("QueryString parameter should not have more than 2 = per part"));
            } else if (keyValue.length >= 2) {
                this.setQueryParameter(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1 && param.charAt(0) != '=') {
                this.setQueryParameter(keyValue[0], null);
            } else {
                throw new RuntimeException(new MalformedURLException("QueryString part should not start with an = and not be empty"));
            }
        }
        return this;
    }

    /**
     * Sets a query parameter with the given name,this can be called repeatedly.
     */
    @Override
    public RestRequestHolder setQueryParameter(String name, String value) {
        if (queryParameters.containsKey(name)) {
            Collection<String> values = queryParameters.get(name);
            values.add(value);
        } else {
            List<String> values = new ArrayList<>();
            values.add(value);
            queryParameters.put(name, values);
        }
        return this;
    }

    /**
     * Sets the authentication header for the current request using BASIC authentication.
     */
    @Override
    public RestRequestHolder setAuth(String userInfo) {
        this.scheme = RestAuthScheme.BASIC;

        if (userInfo.equals("")) {
            throw new RuntimeException(new MalformedURLException("userInfo should not be empty"));
        }

        int split = userInfo.indexOf(":");

        if (split == 0) { // We only have a password without user
            this.username = "";
            this.password = userInfo.substring(1);
        } else if (split == -1) { // We only have a username without password
            this.username = userInfo;
            this.password = "";
        } else {
            this.username = userInfo.substring(0, split);
            this.password = userInfo.substring(split + 1);
        }

        return this;
    }

    /**
     * Sets the authentication header for the current request using BASIC authentication.
     */
    @Override
    public RestRequestHolder setAuth(String username, String password) {
        this.username = username;
        this.password = password;
        this.scheme = RestAuthScheme.BASIC;
        return this;
    }

    /**
     * Sets the authentication header for the current request.
     */
    @Override
    public RestRequestHolder setAuth(String username, String password, RestAuthScheme scheme) {
        this.username = username;
        this.password = password;
        this.scheme = scheme;
        return this;
    }

    @Override
    public RestRequestHolder sign(RestSignatureCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    /**
     * Sets whether redirects (301, 302) should be followed automatically.
     */
    @Override
    public RestRequestHolder setFollowRedirects(Boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    /**
     * Sets the virtual host.
     */
    @Override
    public RestRequestHolder setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
        return this;
    }

    /**
     * Sets the request timeout in milliseconds.
     */
    @Override
    public RestRequestHolder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Set the content type.  If the request body is a String, and no charset parameter is included, then it will
     * default to UTF-8.
     */
    @Override
    public RestRequestHolder setContentType(String contentType) {
        return setHeader(HttpHeaders.Names.CONTENT_TYPE, contentType);
    }

    @Override
    public RestRequestHolder setMethod(String method) {
        this.method = method;
        return this;
    }

    @Override
    public RestRequestHolder setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public RestRequestHolder setBody(JsonNode body) {
        this.body = body;
        return this;
    }

    @Override
    public RestRequestHolder setBody(InputStream body) {
        this.body = body;
        return this;
    }

    @Override
    public RestRequestHolder setBody(File body) {
        this.body = body;
        return this;
    }

    /**
     * @return the URL of the request.
     */
    @Override
    public String getUrl() {
        return this.url;
    }

    /**
     * @return the headers (a copy to prevent side-effects).
     */
    @Override
    public Map<String, Collection<String>> getHeaders() {
        return new HashMap<>(this.headers);
    }

    /**
     * @return the query parameters (a copy to prevent side-effects).
     */
    @Override
    public Map<String, Collection<String>> getQueryParameters() {
        return new HashMap<>(this.queryParameters);
    }

    /**
     * @return the auth username, null if not an authenticated request.
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * @return the auth password, null if not an authenticated request
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * @return the auth scheme, null if not an authenticated request
     */
    @Override
    public RestAuthScheme getScheme() {
        return this.scheme;
    }

    /**
     * @return the signature calculator (exemple: OAuth), null if none is set.
     */
    @Override
    public RestSignatureCalculator getCalculator() {
        return this.calculator;
    }

    /**
     * @return the auth scheme (null if not an authenticated request)
     */
    @Override
    public int getTimeout() {
        return this.timeout;
    }

    /**
     * @return true if the request is configure to follow redirect, false if it is configure not to, null if nothing is configured and the global client preference should be used instead.
     */
    @Override
    public Boolean getFollowRedirects() {
        return this.followRedirects;
    }

    // Intentionally package public.
    String getVirtualHost() {
        return this.virtualHost;
    }

    /**
     * Perform a GET on the request asynchronously.
     */
    @Override
    public CompletableFuture<RestResponse> get() {
        return execute("GET");
    }

    /**
     * Perform a PATCH on the request asynchronously.
     *
     * @param body represented as String
     */
    @Override
    public CompletableFuture<RestResponse> patch(String body) {
        setMethod("PATCH");
        return executeString(body);
    }

    /**
     * Perform a POST on the request asynchronously.
     *
     * @param body represented as String
     */
    @Override
    public CompletableFuture<RestResponse> post(String body) {
        setMethod("POST");
        return executeString(body);
    }

    /**
     * Perform a PUT on the request asynchronously.
     *
     * @param body represented as String
     */
    @Override
    public CompletableFuture<RestResponse> put(String body) {
        setMethod("PUT");
        return executeString(body);
    }

    /**
     * Perform a PATCH on the request asynchronously.
     *
     * @param body represented as JSON
     */
    @Override
    public CompletableFuture<RestResponse> patch(JsonNode body) {
        setMethod("PATCH");
        return executeJson(body);
    }

    /**
     * Perform a POST on the request asynchronously.
     *
     * @param body represented as JSON
     */
    @Override
    public CompletableFuture<RestResponse> post(JsonNode body) {
        setMethod("POST");
        return executeJson(body);
    }

    /**
     * Perform a PUT on the request asynchronously.
     *
     * @param body represented as JSON
     */
    @Override
    public CompletableFuture<RestResponse> put(JsonNode body) {
        setMethod("PUT");
        return executeJson(body);
    }

    /**
     * Perform a PATCH on the request asynchronously.
     *
     * @param body represented as an InputStream
     */
    @Override
    public CompletableFuture<RestResponse> patch(InputStream body) {
        setMethod("PATCH");
        return executeIS(body);
    }

    /**
     * Perform a POST on the request asynchronously.
     *
     * @param body represented as an InputStream
     */
    @Override
    public CompletableFuture<RestResponse> post(InputStream body) {
        setMethod("POST");
        return executeIS(body);
    }

    /**
     * Perform a PUT on the request asynchronously.
     *
     * @param body represented as an InputStream
     */
    @Override
    public CompletableFuture<RestResponse> put(InputStream body) {
        setMethod("PUT");
        return executeIS(body);
    }

    /**
     * Perform a POST on the request asynchronously.
     *
     * @param body represented as a File
     */
    @Override
    public CompletableFuture<RestResponse> post(File body) {
        setMethod("POST");
        return executeFile(body);
    }

    /**
     * Perform a PUT on the request asynchronously.
     *
     * @param body represented as a File
     */
    @Override
    public CompletableFuture<RestResponse> put(File body) {
        setMethod("PUT");
        return executeFile(body);
    }

    /**
     * Perform a DELETE on the request asynchronously.
     */
    @Override
    public CompletableFuture<RestResponse> delete() {
        return execute("DELETE");
    }

    /**
     * Perform a HEAD on the request asynchronously.
     */
    @Override
    public CompletableFuture<RestResponse> head() {
        return execute("HEAD");
    }

    /**
     * Perform an OPTIONS on the request asynchronously.
     */
    @Override
    public CompletableFuture<RestResponse> options() {
        return execute("OPTIONS");
    }

    /**
     * Execute an arbitrary method on the request asynchronously.
     *
     * @param method The method to execute
     */
    @Override
    public CompletableFuture<RestResponse> execute(String method) {
        setMethod(method);
        return execute();
    }

    @Override
    public CompletableFuture<RestResponse> execute() {
        if (body == null) {
            NingRestRequest req = new NingRestRequest(client, method, url, queryParameters, headers);
            return execute(req);
        } else if (body instanceof String) {
            return executeString((String) body);
        } else if (body instanceof JsonNode) {
            return executeJson((JsonNode) body);
        } else if (body instanceof File) {
            return executeFile((File) body);
        } else if (body instanceof InputStream) {
            return executeIS((InputStream) body);
        } else {
            throw new IllegalStateException("Impossible body: " + body);
        }
    }

    private CompletableFuture<RestResponse> executeString(String body) {
        FluentCaseInsensitiveStringsMap headers = new FluentCaseInsensitiveStringsMap(this.headers);

        // Detect and maybe add charset
        String contentType = headers.getFirstValue(HttpHeaders.Names.CONTENT_TYPE);
        if (contentType == null) {
            contentType = "text/plain";
        }
        String charset = AsyncHttpProviderUtils.parseCharset(contentType);
        if (charset == null) {
            charset = "utf-8";
            headers.replace(HttpHeaders.Names.CONTENT_TYPE, contentType + "; charset=utf-8");
        }

        byte[] bodyBytes;
        try {
            bodyBytes = body.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        NingRestRequest req = new NingRestRequest(client, method, url, queryParameters, headers, bodyBytes)
                .setBody(body)
                .setBodyEncoding(charset);
        return execute(req);
    }

    private CompletableFuture<RestResponse> executeJson(JsonNode body) {
        FluentCaseInsensitiveStringsMap headers = new FluentCaseInsensitiveStringsMap(this.headers);
        headers.replace(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8");
        String bodyStr = Json.stringify(body);
        byte[] bodyBytes;
        try {
            bodyBytes = bodyStr.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        NingRestRequest req = new NingRestRequest(client, method, url, queryParameters, headers, bodyBytes)
                .setBody(bodyStr)
                .setBodyEncoding("utf-8");
        return execute(req);

    }

    private CompletableFuture<RestResponse> executeIS(InputStream body) {
        NingRestRequest req = new NingRestRequest(client, method, url, queryParameters, headers)
                .setBody(body);
        return execute(req);
    }

    private CompletableFuture<RestResponse> executeFile(File body) {
        NingRestRequest req = new NingRestRequest(client, method, url, queryParameters, headers)
                .setBody(body);
        return execute(req);
    }

    private CompletableFuture<RestResponse> execute(NingRestRequest req) {
        if (this.timeout > 0) {// todo change PerRequestConfig
            PerRequestConfig config = new PerRequestConfig();
            config.setRequestTimeoutInMs(this.timeout);
            req.setPerRequestConfig(config);
        }
        if (this.followRedirects != null) {
            req.setFollowRedirects(this.followRedirects);
        }
        if (this.virtualHost != null) {
            req.setVirtualHost(this.virtualHost);
        }
        if (this.username != null && this.password != null && this.scheme != null)
            req.auth(this.username, this.password, this.scheme);
        if (this.calculator != null)
            this.calculator.sign(req);

        return req.execute();
    }
}
