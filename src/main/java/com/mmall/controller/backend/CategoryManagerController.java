package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
     * @param categoryName 品类名称
     * @param parentId  品类父ID
     * @return
     */
    @RequestMapping(value = "/addCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        return iCategoryService.addCategory(categoryName,parentId);
    }

    /**
     * 根据品类Id更新品类名称
     * @param categoryName 品类名称
     * @param categoryId 品类Id
     * @return
     */
    @RequestMapping(value = "/updateCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse updateCategory(String categoryName,Integer categoryId){
        return iCategoryService.updateCategory(categoryName,categoryId);
    }

    /**
     *  根据品类Id获取平级子类
     * @param categoryId 品类Id
     * @return
     */
    @RequestMapping(value = "/getChildrenParallerCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallerCategory(@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        return iCategoryService.getChildrenParallerCategory(categoryId);
    }

    /**
     * 根据品类Id获取子类(递归tree)
     * @param request
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "getAllChildrenCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getAllChildrenCategory(HttpServletRequest request,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }

}
