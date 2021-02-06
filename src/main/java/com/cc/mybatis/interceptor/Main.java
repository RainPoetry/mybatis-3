package com.cc.mybatis.interceptor;

import com.cc.mybatis.common.DataSources;
import com.cc.mybatis.why.mybatis.core.SqlSessionFactoryParser;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;

/**
 * @author chenchong
 * @create 2021/2/5 8:05 下午
 * @description 拦截器
 * <p>
 * sql 注入
 */

public class Main {

  public static void main(String[] args) {

  }


  public static Executor get(Configuration configuration) throws IOException {
    DataSource dataSource = DataSources.createUnpooledDataSource(null);
    JdbcTransaction transaction = new JdbcTransaction(dataSource,
      TransactionIsolationLevel.NONE, false);
    SimpleExecutor executor = new SimpleExecutor(configuration, transaction);
    return executor;
  }


}
