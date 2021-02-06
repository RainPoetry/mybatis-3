package com.cc.mybatis.cache;

/**
 * @author chenchong
 * @create 2021/2/6 4:14 下午
 * @description
 *      二级缓存
 *      用于 SqlSession 间的缓存共享
 *
 *      位置：
 *        CachingExecutor
 *
 *
 *      原理：
 *        一个 namespace 对应一个 cache
 *        xml：
 *        在构造 CacheExecutor 的时候，会根据<cache/>创建一个 Cache，如果使用了 <cache-ref/>,则会注入对应的cache
 *        注解：
 *        通过注解的方式注入 Mapper 的时候 ，通过 @CacheNamespace 来开启二级缓存
 *
 *        二级缓存的真正实现是在那个 Cache（内部是一个哈希表）
 *        CachingExecutor 内的 TransactionalCacheManager 只是获取 缓存信息的傀儡而已
 *
 *
 */

public class SecondCache {
}
