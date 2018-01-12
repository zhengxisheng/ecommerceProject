package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by fanlinglong on 2018/1/11.
 * 商品分类controller
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManagerController {

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IUserService iUserService;
    /**
     * 增加品类
     * @param session
     * @param categoryName 品类名称
     * @param parentId  品类父ID
     * @return
     */
    @RequestMapping(value = "/addCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServerResponse.createByErrorMsg("权限不足,需要管理员权限");
        }
    }

    /**
     * 根据品类Id更新品类名称
     * @param session
     * @param categoryName 品类名称
     * @param categoryId 品类Id
     * @return
     */
    @RequestMapping(value = "/updateCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse updateCategory(HttpSession session,String categoryName,Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.updateCategory(categoryName,categoryId);
        }else {
            return ServerResponse.createByErrorMsg("权限不足,需要管理员权限");
        }
    }

    /**
     *  根据品类Id获取平级子类
     * @param session
     * @param categoryId 品类Id
     * @return
     */
    @RequestMapping(value = "/getChildrenParallerCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallerCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.getChildrenParallerCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMsg("权限不足,需要管理员权限");
        }
    }

    /**
     * 根据品类Id获取子类(递归tree)
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "getAllChildrenCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getAllChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("权限不足,需要管理员权限");
        }

    }

}
