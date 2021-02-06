/**
 * Copyright 2009-2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cc.mybatis.why.mybatis.core;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

/***
 * @author chenchong
 * @create 2021/2/3 9:18 下午
 * @description 在解析 Mapper 配置文件的时候，将解析出来的节点信息，封装成 MappedStatement
 * <p>
 *  然后在执行具体的 SQL 时，将 MappedStatement 传递给具体的 Executor 进行执行
 *
 *  1. 先解析 mapper 文件，将 id 和 MappedStatement 写入到 Configuration 中的 mappedStatements 中
 *
 *  2. 根据 mapper 文件的 namespace ，获取 namespace 对应的 接口，然后通过 Configuration.addMapper() 的方式，
 *        将 <类+方法名，MappedStatement>  注册到 Configuration 中
 *
 *    MappedStatement 通过 id 或者 类名+方法名 获取
 *
 *
 *    MappedStatement 是在 Configuration 实例化的时候就创建好的
 */

public class MappedStatementParser {

  public static void insertDeal(Configuration configuration, XNode context, Class parameterTypeClass) {
    String sql = "<select id=\"getStudent\" resultType=\"com.cc.mybatis.why.entity.Student\">\n" +
      "    select *\n" +
      "    from student\n" +
      "    where id = #{id}\n" +
      "  </select>";

    // 根据 xml 节点名称来动态获取的
    SqlCommandType sqlCommandType = SqlCommandType.SELECT;
    LanguageDriver langDriver = new XMLLanguageDriver();
    KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;

    // SQL 存放点
    SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);
    // 没有配置的话，默认值
    StatementType statementType = StatementType.PREPARED;
    MappedStatement mappedStatement = new MappedStatement
      .Builder(configuration, "nodeId", sqlSource, sqlCommandType)
      .build();
  }

  // 根据 xml 配置读取 sql 信息
  private static SqlSource fromXml(Configuration configuration) {
    String sql = "    select *\n" +
      "    from student\n" +
      "    where id = #{id}";
    String sql2 = "    select *\n" +
      "    from student\n" +
      "    where id = ${id}";
    // 带有 ${}
    TextSqlNode textSqlNode = new TextSqlNode(sql2);
    // 不带有 ${}
    StaticTextSqlNode staticTextSqlNode = new StaticTextSqlNode(sql);

    // 会将 #{} 转换为 ? ，然后变成 StaticSqlSource
    DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(configuration, textSqlNode);

    RawSqlSource rawSqlSource = new RawSqlSource(configuration, staticTextSqlNode, null);


    return null;
  }

  // 从 注解读取 sql 信息
  // 和 XML 是一样的逻辑
  private static SqlSource fromAnnotation() {
    return null;
  }
}
