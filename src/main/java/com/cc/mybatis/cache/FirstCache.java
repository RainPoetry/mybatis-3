package com.cc.mybatis.cache;

/**
 * @author chenchong
 * @create 2021/2/5 8:04 下午
 * @description
 *
 *    一级缓存
 *
 *    位置：
 *      对应的  BaseExecutor::localCache
 *
 *    原理：
 *    会根据 statementId、SQL、参数 生成 CacheKey
 *        如果 CacheKey 存在，则读缓存，不存在，则读数据库、
 *
 *    作用范围：
 *       SESSION（启用缓存）: 查询操作，不清缓存，更新清
 *       STATEMENT（不启用缓存）：查询、更新操作,清空缓存
 *
 *   缓存的缺点：
 *      可能会产生脏读
 *        一个 sqlSession1 查询数据，本地有了一个缓存
 *        另一个 sqlSession2 修改了数据，不会清空 sqlSession1 的缓存，因为他们对应的是两个 Executor，每一个 Executor 的 缓存都是自己私有的
 */

public class FirstCache {
}
