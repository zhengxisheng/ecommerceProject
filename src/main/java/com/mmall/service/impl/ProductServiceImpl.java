package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fanlinglong on 2018/1/13.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product != null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray =product.getSubImages().split(",");
                if(subImageArray.length >0){
                    //主图片取子图片第一张
                    product.setMainImage(subImageArray[0]);
                }
            }
            //判断更新还是新增
            if(product.getId()!=null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("更新产品信息成功");
                }
            }else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0){
                    return ServerResponse.createBySuccessMsg("新增产品信息成功");
                }
            }
        }
        return ServerResponse.createByErrorMsg("参数错误:新增或更新产品参数不正确");
    }

    @Override
    public ServerResponse<String> setProductStatus(Integer productId, Integer status) {
        if (productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0){
            return ServerResponse.createBySuccessMsg("更新产品销售状态成功");
        }
        return ServerResponse.createByErrorMsg("更新产品销售状态失败");
    }
}
