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
 * @see original file at https://github.com/playframework/playframework/blob/master/framework/src/play/src/main/java/play/libs/XML.java
 */
package com.ecomnext.rest.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * XML utilities.
 */
public class XML {
    /**
     * Parse an XML string as DOM.
     */
    public static Document fromString(String xml) {
        try {
            return fromInputStream(
                    new ByteArrayInputStream(xml.getBytes("utf-8")),
                    "utf-8"
            );
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse an InputStream as DOM.
     */
    public static Document fromInputStream(InputStream in, String encoding) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource is = new InputSource(in);
            is.setEncoding(encoding);

            return builder.parse(is);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
