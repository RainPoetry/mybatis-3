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
package com.cc.mybatis.why.mybatis.dao;

import com.cc.mybatis.why.entity.Student;
import org.apache.ibatis.annotations.Param;

/**
 * @author chenchong
 * @create 2021/2/3 10:59 上午
 * @description
 */

public interface StudentDao {

  // 这里有一个 bug, Mybatis 是通过 Parameter.getName() 的方式来获取参数名的
  //  Parameter.getName()  在 jdk1.8 获取不到参数名，只能返回 arg0,arg1
  // 如果不配置 @Param(), 则采用 arg0,arg1 来做映射，也就无法映射到 xml 的 sql 中的 #{}
  Student getStudent(Integer id, String name);

}
