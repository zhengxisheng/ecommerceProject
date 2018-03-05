package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fanlinglong on 2018/2/5.
 */
@Controller
@RequestMapping(value = "/manage/order/")
public class OrderManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;
    /**
     *  订单列表
     * @param request
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse orderList(HttpServletRequest request,
                                    @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iOrderService.managerList(pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }
    /**
     *  订单明细
     * @param request
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ServerResponse orderDetail(HttpServletRequest request,Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iOrderService.managerDetail(orderNo);
        }else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    /**
     *  搜索订单->目前值支持按订单号搜索
     * @param request
     * @param orderNo 订单号
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse orderSearch(HttpServletRequest request,Long orderNo,
                                      @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iOrderService.managerSearch(orderNo,pageNum,pageSize);
        }else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    /**
     *  更改订单状态为已发货
     * @param request
     * @return
     */
    @RequestMapping(value = "sendGoods")
    @ResponseBody
    public ServerResponse managerSendGoods(HttpServletRequest request,Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMsg("用户未登陆，无法获取用户信息");
        }
        User user = JsonUtil.String2Obj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iOrderService.managerSendGoods(orderNo);
        }else {
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }
}
