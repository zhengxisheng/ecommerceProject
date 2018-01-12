package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by fanlinglong on 2018/1/12.
 * 产品模块
 */
@Controller
@RequestMapping(value = "/manage/product/")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    /**
     *  保存或更新产品信息
     * @param session
     * @param product 产品对象
     * @return
     */
    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveProduct(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMsg("权限不足,需要管理员权限");
        }
    }

    /**
     * 更新产品销售状态信息
     * @param session
     * @param productId 产品Id
     * @param status 产品状态
     * @return
     */
    @RequestMapping(value = "setProductStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setProductStatus(HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setProductStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMsg("权限不足,需要管理员权限");
        }
    }


}
