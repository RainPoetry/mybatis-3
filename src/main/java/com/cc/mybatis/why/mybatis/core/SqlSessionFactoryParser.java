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

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author chenchong
 * @create 2021/2/3 8:34 下午
 * @description SqlSession 的构建工厂，全局唯一
 * 解析配置文件，实例化 SqlSessionFactory
 * 解析出来的配置信息全部放在 Configuration 中
 */

public class SqlSessionFactoryParser {

  private static final String RESOURCE = "mybatis-config.xml";
  private static final String DB_CONF = "jdbc.properties";

  public static SqlSessionFactory xml() throws IOException {
    InputStream inputStream = Resources.getResourceAsStream(RESOURCE);
    return new SqlSessionFactoryBuilder().build(inputStream);
  }

  public static SqlSessionFactory xml_cc() {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE);
    XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, null, null);
    // 获取 JDBC 链接信息， Mapper 信息
    Configuration configuration = parser.parse();
    return new DefaultSqlSessionFactory(configuration);
  }


  // java 代码方式实现
  public static SqlSessionFactory java(List<Class<?>> mapperList) throws IOException {
    DataSource dataSource = createUnpooledDataSource(DB_CONF);
    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    Environment environment = new Environment("Production", transactionFactory, dataSource);
    Configuration configuration = new Configuration(environment);
    for (Class cls : mapperList) {
      configuration.addMapper(cls);
    }
    return new SqlSessionFactoryBuilder().build(configuration);
  }

  public static UnpooledDataSource createUnpooledDataSource(String resource) throws IOException {
    Properties props = Resources.getResourceAsProperties(resource);
    UnpooledDataSource ds = new UnpooledDataSource();
    ds.setDriver(props.getProperty("driver"));
    ds.setUrl(props.getProperty("url"));
    ds.setUsername(props.getProperty("username"));
    ds.setPassword(props.getProperty("password"));
    return ds;
  }
}
