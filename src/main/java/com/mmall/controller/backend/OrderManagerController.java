package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse orderList(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iOrderService.managerList(pageNum,pageSize);
    }
    /**
     *  订单明细
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ServerResponse orderDetail(Long orderNo){
        return iOrderService.managerDetail(orderNo);
    }

    /**
     *  搜索订单->目前值支持按订单号搜索
     * @param orderNo 订单号
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse orderSearch(Long orderNo,
                                      @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iOrderService.managerSearch(orderNo,pageNum,pageSize);
    }

    /**
     *  更改订单状态为已发货
     * @return
     */
    @RequestMapping(value = "sendGoods")
    @ResponseBody
    public ServerResponse managerSendGoods(Long orderNo){
        return iOrderService.managerSendGoods(orderNo);
    }
}
