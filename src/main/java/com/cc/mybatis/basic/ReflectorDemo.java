package com.cc.mybatis.basic;

import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;

/**
 * @author chenchong
 * @create 2021/2/5 8:48 下午
 * @description
 *
 *    详见  ReflectorTest
 */

public class ReflectorDemo {

  public static void main(String[] args) {
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    Reflector reflector = reflectorFactory.findForClass(ReflectorDemo.class);
  }
}
