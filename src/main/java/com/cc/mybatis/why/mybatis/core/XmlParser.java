/**
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.cc.mybatis.why.mybatis.core;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.PropertyParser;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author chenchong
 * @create 2021/2/4 10:29 上午
 * @description  XML 解析
 *  解析数据被封装成 XNode
 */

public class XmlParser {

  private static final String resource = "mapper/studentMapper.xml";

  public static void main(String[] args) throws Exception {
    InputStream inputStream = Resources.getResourceAsStream(resource);
    Document document = createDocument(new InputSource(inputStream));
    parseNode(document);
  }

  private static void parseNode(Document document) throws XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    Node node = (Node) xpath.evaluate("/mapper/select", document, XPathConstants.NODE);
    // xml 节点名称
    String name = node.getNodeName();
    // xml 属性配置
    Properties attribute = parseAttributes(node);
    // xml 节点的内容
    String body = parseBody(node);
    System.out.println(body);
  }

  private static String parseBody(Node node) {
    String data = getBodyData(node);
    if (data == null) {
      NodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node child = children.item(i);
        data = getBodyData(child);
        if (data != null) {
          break;
        }
      }
    }
    return data;
  }

  private static String getBodyData(Node child) {
    //  mybatis-config.xml 中的  <properties resource="jdbc.properties"/>
    Properties variables = new Properties();
    if (child.getNodeType() == Node.CDATA_SECTION_NODE
      || child.getNodeType() == Node.TEXT_NODE) {
      String data = ((CharacterData) child).getData();
      data = PropertyParser.parse(data, variables);
      return data;
    }
    return null;
  }
  private static Properties parseAttributes(Node n) {
    //  mybatis-config.xml 中的  <properties resource="jdbc.properties"/>
    Properties variables = new Properties();
    Properties attributes = new Properties();
    NamedNodeMap attributeNodes = n.getAttributes();
    if (attributeNodes != null) {
      for (int i = 0; i < attributeNodes.getLength(); i++) {
        Node attribute = attributeNodes.item(i);
        String value = PropertyParser.parse(attribute.getNodeValue(), variables);
        attributes.put(attribute.getNodeName(), value);
      }
    }
    return attributes;
  }

  private static Document createDocument(InputSource inputSource) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//    factory.setValidating(validation);

    factory.setNamespaceAware(false);
    factory.setIgnoringComments(true);
    factory.setIgnoringElementContentWhitespace(false);
    factory.setCoalescing(false);
    factory.setExpandEntityReferences(true);

    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setEntityResolver(new XMLMapperEntityResolver());
    builder.setErrorHandler(new ErrorHandler() {
      @Override
      public void error(SAXParseException exception) throws SAXException {
        throw exception;
      }

      @Override
      public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
      }

      @Override
      public void warning(SAXParseException exception) throws SAXException {
        // NOP
      }
    });
    return builder.parse(inputSource);
  }
}
