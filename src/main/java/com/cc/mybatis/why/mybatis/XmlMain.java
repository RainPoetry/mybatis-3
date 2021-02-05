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
package com.cc.mybatis.why.mybatis;

import com.cc.mybatis.why.mybatis.core.MappedStatementParser;
import org.apache.ibatis.reflection.ParamNameUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author chenchong
 * @create 2021/2/3 2:23 下午
 * @description
 */

public class XmlMain {

  public Map<String, String> deal(String aa) {
    return null;
  }

  /**
   *  <compilerArgument>-parameters</compilerArgument> 才能生效
   */
  public static void main(String[] args) throws NoSuchMethodException {
    Method method = XmlMain.class.getDeclaredMethod("deal",String.class);

    System.out.println(method.getParameters()[0].getName());

  }
}
