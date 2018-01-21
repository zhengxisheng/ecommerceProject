package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by fanlinglong on 2018/1/21.
 * 地址管理controller
 */
@Controller
@RequestMapping(value = "/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     *  增加收货地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(),shipping);
    }

    /**
     *  删除收货地址
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping(value = "del.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse del(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(),shippingId);
    }

    /**
     *  更新收货地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpSession session,Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(),shipping);
    }

    /**
     *  查询收货地址详细信息
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping(value = "select.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(),shippingId);
    }

    /**
     *  查询地址列表
     * @param session
     * @return
     */
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);
    }
}
