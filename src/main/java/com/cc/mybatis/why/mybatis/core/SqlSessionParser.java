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

import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

/**
 * @author chenchong
 * @create 2021/2/3 8:35 下午
 * @description  获取 SqlSession
 *
 *  SqlSession 线程不安全
 */

public class SqlSessionParser {

  public static SqlSession session(SqlSessionFactory sqlSessionFactory) {
    return sqlSessionFactory.openSession();
  }


  public static DefaultSqlSession session_cc(SqlSessionFactory sqlSessionFactory) throws Exception {
    // 在解析 mybatis 配置文件的时候
    // 读取 environments 中 default 配置的 environment 的 id 对应的 environment
    // 没有则会报错
    Configuration configuration = sqlSessionFactory.getConfiguration();
    Environment environment = configuration.getEnvironment();
    // TransactionFactory transactionFactory = environment.getTransactionFactory();
    JdbcTransactionFactory transactionFactory = (JdbcTransactionFactory) configuration.getTypeAliasRegistry()
      .resolveAlias("JDBC").getDeclaredConstructor().newInstance();
    // 事务隔离级别
    TransactionIsolationLevel level = null;
    // 自动提交
    boolean autoCommit = true;
    // 默认是 simple
    ExecutorType execType = configuration.getDefaultExecutorType();
    Transaction transaction = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
//    final Executor executor = configuration.newExecutor(tx, execType);
    SimpleExecutor executor = new SimpleExecutor(configuration, transaction);
    return new DefaultSqlSession(configuration, executor, autoCommit);
  }
}
