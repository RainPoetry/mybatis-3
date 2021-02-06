package com.cc.mybatis.interceptor;

/**
 * @author chenchong
 * @create 2021/2/6 3:21 下午
 * @description
 *
 *    责任链模式
 *
 *
 *    原理：
 *      扫描 Interceptor 注解，然后将
 */

public class InterceptorDemo {


  /***
   *
   *  注入插件的时机：
   *      1. 解析 xml 文件的 plugins 节点的时候
   *      2. 手动加入：configuration.addInterceptor
   *
   *
   *
   *  拦截器的周期:
   *
   *                                 执行接口的方法
   *                                      |
   *                          接口代理对象的 invoke 执行
   *                                     |
   *          Executor 的执行，如：query、update、commit (Executor的拦截器将在这里发挥作用)
   *                                    |
   *             构造 ParameterHandler ,同时注入 ParameterHandler 的拦截器
   *             构造 ResultSetHandler，同时注入 ResultSetHandler 的拦截器
   *                                   |
   *       根据 ParameterHandler，ResultSetHandler 构造 StatementHandler,同时注入 StatementHandler 的拦截器
   *                                   |
   *             根据 BoundSQL，获取 SQL，生成 Statement 对象(StatementHandler拦截器发挥作用)
   *                                  |
   *    如果是PrepareStatementHandler ，则为 PrepareStatement 设置参数 (ParameterHandler 拦截器发生作用)
   *                                |
   *                          取出 查询结果，(ResultSetHandler 发挥作用)
   *
   *
   *
   */
}
