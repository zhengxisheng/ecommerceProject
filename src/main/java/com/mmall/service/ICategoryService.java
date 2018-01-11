package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Created by fanlinglong on 2018/1/11.
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName,Integer parentId);

    ServerResponse updateCategory(String categoryName,Integer categoryId);

    ServerResponse getChildrenParallerCategory(Integer categoryId);
}
