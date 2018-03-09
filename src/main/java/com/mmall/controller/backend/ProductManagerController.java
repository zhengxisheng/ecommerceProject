package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
    @Autowired
    private IFileService iFileService;

    /**
     *  保存或更新产品信息
     * @param product 产品对象
     * @return
     */
    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveProduct(Product product){
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 更新产品销售状态信息
     * @param productId 产品Id
     * @param status 产品状态
     * @return
     */
    @RequestMapping(value = "setProductStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setProductStatus(Integer productId,Integer status){
        return iProductService.setProductStatus(productId,status);
    }

    /**
     *  查看商品详情
     * @param productId 产品Id
     * @return
     */
    @RequestMapping(value = "getDetailByProductId.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetailByProductId(Integer productId){
        return iProductService.getDetailByProductId(productId);
    }

    /**
     *  获取商品列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(value = "getProductList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getProductList(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iProductService.getProductList(pageNum,pageSize);
    }

    /**
     *  根据商品名称和Id 获取商品列表
     * @param productName 商品名称
     * @param productId 商品id
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping(value = "searchProduct.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse searchProduct (String productName,Integer productId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iProductService.searchProduct(productName,productId,pageNum,pageSize);
    }

    /**
     *  文件上传接口
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload (@RequestParam(value = "upload_file",required = false) MultipartFile file,
                                  HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     *  富文本simditor图片上传
     * @param file
     * @param request
     * @return
     */

    /**
     * simditor返回格式
        "success": true/false,
        "msg": "error message",
        "file_path": "[real file path]"
     */
    @RequestMapping(value = "richtextImgUpload.do")
    @ResponseBody
    public Map richtextImgUpload(@RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request){
        Map resultMap = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        if (StringUtils.isBlank(targetFileName)){
            resultMap.put("success",true);
            resultMap.put("msg","上传失败");
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        return resultMap;
    }
}
