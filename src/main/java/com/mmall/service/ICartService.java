package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * Created by fanlinglong on 2018/1/21.
 */
public interface ICartService {

     ServerResponse<CartVo> list (Integer userId);

     ServerResponse<CartVo> addCart(Integer count,Integer productId,Integer userId);

     ServerResponse<CartVo> updateCart(Integer count, Integer productId,Integer userId);

     ServerResponse<CartVo> deleteCart(Integer userId,String productIds);

     ServerResponse<CartVo> selectOrUnselect(Integer userId,Integer productId,Integer checked);

     ServerResponse<Integer> getCartProductCount(Integer userId);
}
