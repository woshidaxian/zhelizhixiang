package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisOperator {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * TTL,以秒为单位，返回给定key的剩余生存时间(TTL,time to live)
     * @param key
     * @return
     */

    public long ttl(String key){return redisTemplate.getExpire(key);}

    /**
     * expire 设置过期时间，单位秒
     * @param key
     * @return
     */
    public void expire(String key,long timeout){redisTemplate.expire(key,timeout, TimeUnit.SECONDS);}

    /**
     * INCR key,增加key一次
     * @param key
     * @return
     */
    public long incr(String key,long delta){return redisTemplate.opsForValue().increment(key,delta);}

    /**
     * decr,减少key一次
     */
    public long decr(String key,long delta){
        if (delta < 0){
            throw new RuntimeException("递减因子必须大于0");
        }
        long result = redisTemplate.opsForValue().increment(key, -delta);
        if (result > 0){
            return result;
        }else {
            del(key);
            return 0;
        }
    }

    /**
     * keys pttern 查找所有符合给定模式pattern的key
     */
    public Set<String> keys(String pattern){return redisTemplate.keys(pattern);}

    /**
     * DEL key 删除一个key
     * @param
     */
    public void del(String key){redisTemplate.delete(key);}

    /**
     * SET key value,设置一个key-value(将字符串值value关联到key)
     * @param key
     * @param value
     */

    public void set(String key,String value){redisTemplate.opsForValue().set(key,value);}

    /**
     * SET key value EX seconds,设置key-value和超时时间(秒)
     * @param key
     * @param value
     * @param timeout
     */
    public void set(String key,String value,long timeout){
        redisTemplate.opsForValue().set(key,value,timeout,TimeUnit.SECONDS);
    }

    /**
     * GET key,返回key所关联的字符串值
     * @param key
     * @return00000
     */
    public String get(String key){return (String) redisTemplate.opsForValue().get(key);}

    //hash
    /**
     * HEST key field value 将哈希表key中的域field的值设为value
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key,String field,Object value){redisTemplate.opsForHash().put(key,field,value);}

    /**
     * HEST key field ,返回哈希表key中给field的值
     * @param key
     * @param field
     * @return
     */
    public String hget(String key,String field){return (String) redisTemplate.opsForHash().get(key,field);}

    /**
     * HEDL key field[field]删除哈希表key中的一个或多个制定域，不存在的域将被
     * @param key
     * @param fields
     */
    public void hdel(String key,Object... fields){redisTemplate .opsForHash().delete(key,fields);}

    /**
     * HGETALL key ,返回哈希表，key中，所有的域和值
     * @param key
     * @return
     */
    public Map<Object,Object> hgetall(String key){return redisTemplate.opsForHash().entries(key);}

    //list

    /**
     * LPUSH key value 将一个值 value插入到列表key的表头
     * @param key
     * @param value
     * @return
     */
    public long lpush(String key,String value){return redisTemplate.opsForList().leftPush(key,value);}

    /**
     *  LPOP key移除并返回列表 key的头元素
     * @param key
     * @return
     */
    public String lpop(String key){return (String)redisTemplate.opsForList().leftPop(key);}

    /**
     * RPUSH key value 将一个值value插入到列表key的表尾
     * @param key
     * @param value
     * @return
     */
    public long rpush(String key,String value){return redisTemplate.opsForList().rightPush(key,value);}
}
