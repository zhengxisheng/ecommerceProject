package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * Created by fanlinglong on 2018/2/4.
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo,Integer userId,String path);

    ServerResponse aliCallback(Map<String,String> params);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse cancel(Integer userId, Long orderNo);

    ServerResponse getOrderDetail(Integer userId, Long orderNo);

    ServerResponse getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse managerList(int pageNum, int pageSize);

    ServerResponse managerDetail(Long orderNo);

    ServerResponse managerSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse managerSendGoods(Long orderNo);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    void closeOrder (int hour);
}
