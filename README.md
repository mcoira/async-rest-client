# Asynchronous REST client 

Asynchronous REST client is an HTTP and WebSocket client library for Java 8 focused on:
* Non blocking HTTP calls with asynchronous and composable responses processing
* URL expressions with transparent replacement and escaping
* Transparent usage of JSON and XML formats
* Low number of dependencies

We have been users of other client libraries such as [Apache HTTPClient](http://hc.apache.org/httpcomponents-client-ga/) or [async-http-client](https://github.com/AsyncHttpClient/async-http-client). Both of them are great but their approach is too low level. In order to gain usability you need to use wrappers like [Apache Fluent](http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fluent.html), [Spring RestTemplate](http://docs.spring.io/spring/docs/4.0.5.RELEASE/spring-framework-reference/htmlsingle/#rest-resttemplate), or [Play WS API](https://github.com/playframework/playframework/tree/master/framework/src/play-java-ws). 

We were regular users of [Play WS](https://github.com/playframework/playframework/tree/master/framework/src/play-java-ws) because it solves most of our requirements but for some Java projects it is a problem to include too many dependencies (Play Framework, Akka, Scala) and may be uncomfortable to use Scala Promises. So we started from Play WS code to build a more suitable client for us.

## Getting started

### Maven

To use the latest version of this library you need to add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>com.ecomnext</groupId>
  <artifactId>async-rest-client</artifactId>
  <version>1.0</version>
</dependency>
```

**Important note: this project is not hosted in Maven Central yet.**

### Making a request

To build an HTTP request, you start with Rest.url() to specify the URL.

```java
RestRequestHolder urlHolder = Rest.url("http://example.com");
```

When you call REST services it is very common to include variables values into the URL. In order to support that you can include variable names enclosed between curly brackets in the URL and provide the actual values as parameter of Rest.url().

```java
RestRequestHolder reqHolder = Rest.url("http://example.com/{userid}", userId);
```

More complex URL are possible using the URI template processor [Handy URI Templates](https://github.com/damnhandy/Handy-URI-Templates) which implements [RFC6570](http://tools.ietf.org/html/rfc6570).

The RestRequestHolder can be used to specify HTTP options such as headers, timeouts, or query parameters. Every call on the object will return a reference to the object so it is possible to chain multiple calls together.

```java
RestRequestHolder complexHolder = reqHolder
        .setHeader("headerKey", "headerValue")
        .setTimeout(1000)
        .setQueryParameter("paramKey", "paramValue");
```

To perform the actual call to the HTTP service you have to call the method corresponding to the HTTP method you want to use. This will send the request with the configuration of the holder and it will return a ```CompletableFuture<RestResponse>``` where the RestResponse contains the data returned from the server.

```java
CompletableFuture<RestResponse> futureResponse = complexHolder.get();
```

### Request with authentication

There are three different authentication systems supported, BASIC, DIGEST, and NTLM. By default it will be used the [HTTP Basic Authentication](http://en.wikipedia.org/wiki/Basic_access_authentication).

It is common to find yourself in one of this two different scenarios. You may need to use the same user/pass to authenticate all your requests or you may have different user/pass for different requests.

To authenticate a single request you can specify your user and password in the RestRequestHolder. 

```java
RestRequestHolder reqHolder = Rest.url("http://example.com").setAuth("user", "password");
```

Preemptive authentication is enabled by default. It means that the client will send the basic authentication response even before the server gives an unauthorized response in certain situations, thus reducing the overhead of making the connection.

To use the same authentication values for every request it is necessary to modify the underlying AsyncHttpClient:

```java
Builder builder = new AsyncHttpClientConfig.Builder();
Realm realm = new Realm.RealmBuilder()
                       .setPrincipal(user)
                       .setPassword(admin)
                       .setUsePreemptiveAuth(true)
                       .setScheme(AuthScheme.BASIC)
                       .build();
builder.setRealm(realm).build();

Rest.configClient(builder.build());
```
  
### Request with follow redirects

If an HTTP call results in a 302 or a 301 redirect, you can automatically follow the redirect without having to make another call.

```java
Rest.url("http://example.com").setFollowRedirects(true).get();
```

### Request with query parameters

The simplest way to add query parameters in a request is to use ```setQueryParameter()``` in a ```RestRequestHolder```.

```java
Rest.url("http://example.com").setQueryParameter("paramKey", "paramValue").get();
```

### Request with additional headers

You can add any HTTP header to your request using the ```RestRequestHolder```.

```java
Rest.url("http://example.com").setHeader("headerKey", "headerValue").get();
```

FYI: the ```Content-Type``` header is set automatically either for JSON and XML but, if you are sending plain text in a particular format, you may want to define the content type explicitly.

```java
Rest.url("http://example.com").setHeader("Content-Type", "application/json").post(jsonString);
// OR
Rest.url("http://example.com").setContentType("application/json").post(jsonString);
```

### Request with time out

If your code is sensible to the response time, you can use setTimeout to set a value in milliseconds.

```java
Rest.url("http://example.com").setTimeout(1000).get();
```

### Submitting form data

When you use post with a String, contentType is automatically set to "text/plain". If you want to submit form data you need to encode your data and set the header.

```java
Rest.url("http://example.com").setContentType("application/x-www-form-urlencoded")
           .post("key1=value1&key2=value2");
```

### Submitting JSON data

The library used to work with JSON is [Jackson](http://wiki.fasterxml.com/JacksonHome), but there is a utility class to simplify it's usage, ```com.ecomnext.rest.utils.Json```. It offers methods to easily read and write POJOs.

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.ecomnext.rest.utils.Json;
JsonNode json = Json.newObject()
                    .put("key1", "value1")
                    .put("key2", "value2");
Rest.url("http://example.com").post(json);
```

## Processing the Response

### Processing a response as JSON

You can process the response as a JsonNode by calling ```response.asJson()```.

```java
CompletableFuture<RestResponse> futureResponse = Rest.url("http://example.com").get();
JsonNode jsonNode = futureResponse.get().asJson();
```

### Processing a response as XML

Similarly, you can process the response as XML by calling ```response.asXml()```.

```java
CompletableFuture<RestResponse> futureResponse = Rest.url("http://example.com").get();
Document document = futureResponse.get().asXml();
```

### Processing large responses

The library allows you to download large files getting the response body as an InputStream so you can process the data without loading the entire content into memory at once.

```java
InputStream inputStream = null;
OutputStream outputStream = null;
try {
  CompletableFuture<RestResponse> futureResponse = Rest.url("http://example.com").get();
  inputStream = futureResponse.get().getBodyAsStream();

  // write the inputStream to a File
  final File file = new File("/tmp/response.txt");
  outputStream = new FileOutputStream(file);

  int read = 0;
  byte[] buffer = new byte[1024];

  while ((read = inputStream.read(buffer)) != -1) {
    outputStream.write(buffer, 0, read);
  }

  return file;
} catch (IOException e) {
    throw e;
} finally {
    if (inputStream != null) {inputStream.close();}
    if (outputStream != null) {outputStream.close();}
}

```

## Using RestClient

The ```RestClient``` class is a wrapper around the underlying [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client). It gives you the additional functionality making easier to work with futures, formats, and much more.

The class ```Rest``` gives you access to a default client.

```java
RestClient client = Rest.client();
```

In some cases you will need to define multiple clients with different profiles, or using a mock for testing purposes. You can define a Rest client directly from code and use it for making requests.

```java
com.ning.http.client.AsyncHttpClientConfig customConfig =
    new com.ning.http.client.AsyncHttpClientConfig.Builder()
        .setProxyServer(new com.ning.http.client.ProxyServer("127.0.0.1", 38080))
        .setCompressionEnabled(true)
        .build();
WSClient customClient = new com.ecomnext.rest.ning.NingRestClient(customConfig);

CompletableFuture<RestResponse> response = customClient.url("http://example.com/feed").get();
```

NOTE: if you instantiate a NingRestClient object, you must shutdown using client.close() when processing has completed. This will release the underlying ThreadPoolExecutor used by AsyncHttpClient. Failure to close the client may result in out of memory exceptions (especially if you are reloading an application frequently in development mode).

You can also get access to the underlying AsyncHttpClient.

```java
com.ning.http.client.AsyncHttpClient underlyingClient =  Rest.client().getUnderlying();
```

## RestClient limitations

This library does not support multi part form upload directly neither streaming body upload. You can do both of them using the underlying client.


## License

This software is licensed under the Apache 2 license, quoted below.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.