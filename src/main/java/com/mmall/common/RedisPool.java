package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by fanlinglong on 2018/2/28.
 * redis连接池配置
 */
public class RedisPool {

    //jedis连接池
    private static JedisPool pool ;
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));
    //最大空闲数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));
    //最小空闲数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));
    //在borrow一个jedis实例,是否要进行验证操作,如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));
    //在return一个jedis实例,是否要进行验证操作,如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));
    //redis连接地址
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    //redis连接端口
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

    private static void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。
        config.setBlockWhenExhausted(true);
        pool = new JedisPool(config,redisIp,redisPort,1000*5);
    }
    static {
        init();
    }

    /**
     *  获取jedis实例
     * @return
     */
    public static Jedis getJedis(){
        return pool.getResource();
    }

    /**
     *  释放jedis实例到pool
     * @param jedis
     */
    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

    /**
     *  释放损坏jedis实例到pool
     * @param jedis
     */
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }
}
