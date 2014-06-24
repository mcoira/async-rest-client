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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play-json/src/main/java/play/libs/Json.java
 */
package com.ecomnext.rest.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Helper functions to handle JsonNode values.
 */
public class Json {
    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();
    private static volatile ObjectMapper objectMapper = null;

    // Ensures that there always is *a* object mapper
    private static ObjectMapper mapper() {
        if (objectMapper == null) {
            return defaultObjectMapper;
        } else {
            return objectMapper;
        }
    }

    /**
     * Convert an object to JsonNode.
     *
     * @param data Value to convert in Json.
     */
    public static JsonNode toJson(final Object data) {
        try {
            return mapper().valueToTree(data);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a JsonNode to a Java value
     *
     * @param json Json value to convert.
     * @param clazz Expected Java value type.
     */
    public static <A> A fromJson(JsonNode json, Class<A> clazz) {
        try {
            return mapper().treeToValue(json, clazz);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a JsonNode to a Java value using Generics.
     *
     * In addition to binding to POJOs and "simple" types, there is one additional
     * variant: that of binding to generic (typed) containers. This case requires
     * special handling due to so-called Type Erasure (used by Java to implement
     * generics in somewhat backwards compatible way), which prevents you from
     * using something like Collection<String>.class (which does not compile).
     *
     * @param json Json value to convert.
     * @param valueTypeRef Expected Java value type.
     */
    @SuppressWarnings("unchecked")
    public static <A> A fromJson(JsonNode json, TypeReference<?> valueTypeRef) {
        try {
            return (A) mapper().readValue(json.traverse(), valueTypeRef);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a String to a Java value.
     * @param content String value to convert.
     * @param clazz Expected Java value type.
     */
    public static <A> A fromString(String content, Class<A> clazz) {
        try {
            return mapper().readValue(content, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new empty ObjectNode.
     */
    public static ObjectNode newObject() {
        return mapper().createObjectNode();
    }

    /**
     * Convert a JsonNode to its json string representation.
     */
    public static String stringify(JsonNode json) {
        return json.toString();
    }

    /**
     * Convert an object to its json string representation.
     */
    public static String stringify(Object object) {
        try {
            return mapper().writeValueAsString(object);
        } catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Parse a String representing a json, and return it as a JsonNode.
     */
    public static JsonNode parse(String src) {
        try {
            return mapper().readValue(src, JsonNode.class);
        } catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Parse a InputStream representing a json, and return it as a JsonNode.
     */
    public static JsonNode parse(java.io.InputStream src) {
        try {
            return mapper().readValue(src, JsonNode.class);
        } catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Inject the object mapper to use.
     *
     * This is intended to be used when Play starts up.  By default, Play will inject its own object mapper here,
     * but this mapper can be overridden either by a custom plugin or from Global.onStart.
     */
    public static void setObjectMapper(ObjectMapper mapper) {
        objectMapper = mapper;
    }
}
