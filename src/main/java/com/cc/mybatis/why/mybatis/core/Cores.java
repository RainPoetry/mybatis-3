package com.cc.mybatis.why.mybatis.core;

/**
 * @author chenchong
 * @create 2021/2/5 3:34 下午
 * @description
 */

public class Cores {

  /**
   *  1. MethodSignature : 接口方法的描述信息
   *          ParamNameResolver : 接口的参数信息，<索引，参数名>
   *                用于将传递参数转为 map 的形式
   *
   *
   *  2. MappedStatement ： 注解 或者 xml 配置的 执行语句以及环境信息
   *          SqlSource： SQL 信息
   *              StaticSqlSource： SQL 和 参数信息
   *                  将 #{} 替换成 ? 并记录里面的信息
   *              BoundSQL： SQL 信息
   *      MappedStatement 在 Configuration 中存储方式是 StrictMap
   *          会将 key 以及 key 的小数点的最后一个字符写入 map，值都是对应的 MappedStatement
   *          如果存在 shortKey 存在，则写入 Ambiguity 用于后续报错
   *
   *
   *
   *
   *
   *
   */
}
