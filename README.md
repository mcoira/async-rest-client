## Asynchronous REST client 

Asynchronous HTTP and WebSocket client library for Java 8 based on the code of [Play WS API](https://github.com/playframework/playframework/tree/master/framework/src/play-java-ws) which is a wrapper around [async-http-client](https://github.com/AsyncHttpClient/async-http-client).

Play WS API adds support for monadic futures which let us combine them and avoid the callback hell. It also adds support for some content type formats such as JSON and XML, and it sets transparently the Content-Type header. 
   
In order to avoid Scala, Akka, and Play framework dependencies, we have modified the Play WS API project to make a pure Java project with only two dependencies, async-http-client and Jackson. We also have replace Play promises with [CompletableFutures](http://java.dzone.com/articles/java-8-definitive-guide). 

### License

This software is licensed under the Apache 2 license, quoted below.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.