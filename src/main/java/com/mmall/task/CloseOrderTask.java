package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by fanlinglong on 2018/3/12.
 * 定时关闭订单(超时未支付订单)
 */
@Slf4j
@Component
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    /**
     *  定时关闭订单
     */
    @Scheduled(cron="0 */1 * * * ?")
    public void closeOrderTask(){
        long lockTimeout =Long.parseLong(PropertiesUtil.getProperty("lock.timeout"));
        //value设置当前时间+锁超时时间
        //根据这个值可以判断锁是否超时
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setnxResult != null && setnxResult == 1){
            log.info("===关闭订单定时任务启动===");
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            //判断分布式锁是否超时
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                String getsetResult = RedisShardedPoolUtil.getset(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis() + lockTimeout));
                // 如果getsetResult == null,说明另一进程执行了任务且删除了分布式锁
                // 如果getsetResult != null且 lockValueStr ！= getsetResult,当进程1和进程2同时判断锁超时,
                // 根据getset返回值与之前lockValueStr的值进行判断
                // 如果相等,说明目前还没有其他进程set锁,如果不相等,说明其他进程执行了set操作
                if (getsetResult == null || (getsetResult != null && StringUtils.equals(lockValueStr,getsetResult))){
                    log.info("===关闭订单定时任务启动===");
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else {
                    log.info("当前有进程执行定时任务,获取分布式锁失败");
                }
            }else {
                log.info("当前有进程执行定时任务,获取分布式锁失败");
            }
        }
        log.info("===关闭订单定时任务结束===");
    }
    public void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,5);//防止死锁,防止当程序异常导致del操作未执行,一直走判断锁是否超时分支
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        //释放锁
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
    }
}
