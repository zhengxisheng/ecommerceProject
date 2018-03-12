package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> getProductList();

    List<Product> selectByNameAndProductId(@Param("productName") String productName,@Param("productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param("productName") String productName,@Param("categoryIdList") List<Integer> categoryIdList);

    //考虑到有的商品可能被删除,如果返回值设置为int,为空的时候会报错
    Integer selectStockByProductId(Integer id);
}