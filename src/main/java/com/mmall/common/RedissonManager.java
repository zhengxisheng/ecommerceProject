package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by fanlinglong on 2018/3/13.
 * redisson 分布式锁
 */
@Slf4j
@Component
public class RedissonManager {

    private Config config = new Config();

    private Redisson redisson = null;

    public Redisson getRedisson() {
        return redisson;
    }

    //redis连接地址
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    //redis连接端口
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

    //构造器执行之后执行
    @PostConstruct
    private void init(){
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(redisIp).append(":").append(redisPort).toString());
            redisson = (Redisson)Redisson.create(config);
            log.info("初始化Redisson结束");
        } catch (Exception e) {
            log.error("redission init error",e);
        }
    }
}
