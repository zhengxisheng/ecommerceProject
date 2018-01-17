package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fanlinglong on 2018/1/17.
 */
@Controller
@RequestMapping(value = "/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 获取产品明细
     * @param productId 产品Id
     * @return
     */
    @RequestMapping(value = "detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetailByProductId(Integer productId){
        return iProductService.getProductDetial(productId);
    }

    /**
     *  查询列表
     * @param keyword  搜索条件
     * @param categroyId  搜索商品分类ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param orderBy  排序关键字 格式: 字段_升序(降序)
     * @return
     */
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyword,
                                         @RequestParam(value = "categoryId",required = false) Integer categroyId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value ="orderBy",required = false) String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,categroyId,pageNum,pageSize,orderBy);
    }
}
