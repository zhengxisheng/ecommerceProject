package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import com.mmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by fanlinglong on 2018/1/21.
 * 购物车controller
 */

@Controller
@RequestMapping(value = "/cart/")
public class CartController {


    @Autowired
    private ICartService iCartService;
    /**
     *  购物车列表
     * @param request
     * @return
     */
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    /**
     *  增加购物车
     * @param request
     * @param count  数量
     * @param productId 商品id
     * @return
     */
    @RequestMapping(value = "addCart.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> addCart(HttpServletRequest request,Integer count, Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return  iCartService.addCart(count,productId,user.getId());
    }

    /**
     *  更新购物车
     * @param request
     * @param count  数量
     * @param productId 商品id
     * @return
     */
    @RequestMapping(value = "updateCart.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> updateCart(HttpServletRequest request,Integer count,Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.updateCart(count,productId,user.getId());
    }

    /**
     *  删除购物车
     * @param request
     * @param productIds 商品id列表
     * @return
     */
    @RequestMapping(value = "deleteCart.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> deleteCart(HttpServletRequest request,String productIds){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteCart(user.getId(),productIds);
    }

    /**
     *  购物车全选
     * @param request
     * @return
     */
    @RequestMapping(value = "selectAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselect(user.getId(),null,Const.Cart.CHECKED);
    }

    /**
     *  购物车反选
     * @param request
     * @return
     */
    @RequestMapping(value = "unSelectAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselect(user.getId(),null,Const.Cart.UN_CHECKED);
    }

    /**
     *  购物车选中
     * @param request
     * @param productId 商品id
     * @return
     */
    @RequestMapping(value = "selectOne.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> selectOne(HttpServletRequest request,Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselect(user.getId(),productId,Const.Cart.CHECKED);
    }

    /**
     *  购物车撤销选中
     * @param request
     * @param productId 商品id
     * @return
     */
    @RequestMapping(value = "unSelectOne.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelectOne(HttpServletRequest request,Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    /**
     *  获取购物车商品数量
     * @param request 用户id
     * @return
     */
    @RequestMapping(value = "getCartProductCount.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
}
