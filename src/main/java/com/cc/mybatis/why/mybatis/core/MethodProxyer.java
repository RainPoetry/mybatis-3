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

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenchong
 * @create 2021/2/3 8:37 下午
 * @description 方法代理
 */

public class MethodProxyer {

  public static <T> T getMapper(SqlSession sqlSession, Class<T> cls) {
    return sqlSession.getMapper(cls);
  }

  public static <T> T getMapper_1(DefaultSqlSession sqlSession, Class<T> cls) {
    MapperProxyFactory<T> mapperProxyFactory = new MapperProxyFactory<>(cls);
    MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession,
      mapperProxyFactory.getMapperInterface(),
      mapperProxyFactory.getMethodCache());
    Class<T> mapperInterface = mapperProxyFactory.getMapperInterface();
    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
  }

  public static <T> T getMapper_2(DefaultSqlSession sqlSession, Class<T> cls) {
    return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Configuration config = sqlSession.getConfiguration();
        MapperMethod mapperMethod = new MapperMethod(cls, method, config);
        System.out.println(config.getMappedStatementNames());
        return mapperMethod.execute(sqlSession, args);
      }
    });
  }

  public static <T> T getMapper_3(DefaultSqlSession sqlSession, Class<T> cls) {
    return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Configuration config = sqlSession.getConfiguration();
        MapperMethod.SqlCommand command = new MapperMethod.SqlCommand(config, cls, method);
        MapperMethod.MethodSignature methodSignature = new MapperMethod.MethodSignature(config, cls, method);
        String methodName = method.getName();
        // xml 中配置的 id 需要和被调用的方法名称保持一致
//        MappedStatement mappedStatement = config.getMappedStatement(methodName);
        // 根据xml信息构件的  MappedStatement
        String statementId = cls.getName() + "." + methodName;
        MappedStatement mappedStatement = config.getMappedStatement(statementId);
        BoundSql boundSql = mappedStatement.getBoundSql(parameter(args, method));
        Connection connection = sqlSession.getConnection();
        PreparedStatement pstm = connection.prepareStatement(boundSql.getSql());
        return sqlSession.executor.query(mappedStatement, parameter(args, method),
          RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER).get(0);
      }
    });
  }

  // 简易版

  /**
   * <compilerArgument>-parameters</compilerArgument>
   * JDK1.8
   * 才能生效
   */
  private static Map<String, Object> parameter(Object[] args, Method method) {
    Parameter[] parameters = method.getParameters();
    Map<String, Object> maps = new HashMap<>();
    for (int i = 0; i < parameters.length; i++) {
      maps.put(parameters[i].getName(), args[i]);
    }
    System.out.println(maps);
    return maps;
  }


}
