package com.cc.mybatis.common;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * @author chenchong
 * @create 2021/2/5 8:13 下午
 * @description
 */

public class DataSources {

  private static final String DB_CONF = "jdbc.properties";

  public static UnpooledDataSource createUnpooledDataSource(String resource) throws IOException {
    Properties props = Resources.getResourceAsProperties(Optional.ofNullable(resource).orElse(DB_CONF));
    UnpooledDataSource ds = new UnpooledDataSource();
    ds.setDriver(props.getProperty("driver"));
    ds.setUrl(props.getProperty("url"));
    ds.setUsername(props.getProperty("username"));
    ds.setPassword(props.getProperty("password"));
    return ds;
  }
}
