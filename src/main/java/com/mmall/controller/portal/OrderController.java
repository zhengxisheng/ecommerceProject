package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by fanlinglong on 2018/2/4.
 */
@Controller
@RequestMapping(value = "/order/")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    /**
     *  支付接口
     * @param orderNo 订单号
     * @param request
     * @return
     */
    @RequestMapping(value = "pay.do")
    @ResponseBody
    public ServerResponse pay(Long orderNo, HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo,user.getId(),path);
    }

    /**
     *  支付宝回调方法
     * @param request
     * @return
     */
    @RequestMapping(value = "alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String,String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0;i<values.length;i++){
                valueStr = (i==values.length-1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
        //非常重要,验证回调的正确性,是不是支付宝发的.并且还要避免重复通知.
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());

            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMsg("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }
        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if (serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    /**
     * 创建订单
     * @param request
     * @param shippingId
     * @return
     */
    @RequestMapping(value = "create.do")
    @ResponseBody
    public ServerResponse create(HttpServletRequest request,Integer shippingId){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(),shippingId);
    }
    /**
     * 取消订单
     * @param request
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpServletRequest request,Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(),orderNo);
    }

    /**
     * 订单明细
     * @param request
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest request,Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }

    /**
     *  订单列表
     * @param request
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse list(HttpServletRequest request, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                               @RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);
    }

    /**
     *  查询订单状态
     * @param request
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping(value = "queryOrderPayStatus.do")
    @ResponseBody
    public ServerResponse queryOrderPayStatus(HttpServletRequest request,Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse response = iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if (response.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

    /**
     *  获取购物车订单预览
     * @param request
     * @return
     */
    @RequestMapping(value = "getOrderCartProduct.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

}
