package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

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
     * @param session
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse orderList(HttpSession session,
                                    @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
     * @param session
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ServerResponse orderDetail(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
     * @param session
     * @param orderNo 订单号
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse orderSearch(HttpSession session,Long orderNo,
                                      @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "sendGoods")
    @ResponseBody
    public ServerResponse managerSendGoods(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
