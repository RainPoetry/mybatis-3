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

import com.cc.mybatis.common.Jsons;
import com.cc.mybatis.why.entity.Student;
import com.cc.mybatis.why.mybatis.core.MethodProxyer;
import com.cc.mybatis.why.mybatis.core.SqlSessionFactoryParser;
import com.cc.mybatis.why.mybatis.core.SqlSessionParser;
import com.cc.mybatis.why.mybatis.dao.StudentDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

/**
 * @author chenchong
 * @create 2021/2/3 2:23 下午
 * @description
 */
@Slf4j
public class Main {


  public static void main(String[] args) throws Exception {

    // 全局唯一
    SqlSessionFactory sqlSessionFactory = SqlSessionFactoryParser.xml_cc();

    // 一个线程一个，非线程安全
    DefaultSqlSession sqlSession = SqlSessionParser.session_cc(sqlSessionFactory);

    // 生成一个 MapperProxy 对象
    StudentDao studentDao = MethodProxyer.getMapper_3(sqlSession, StudentDao.class);
    Student student = studentDao.getStudent(1,"hello");
    System.out.println(Jsons.toString(student, true));

  }

}
