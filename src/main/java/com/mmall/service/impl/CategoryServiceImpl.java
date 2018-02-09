package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by fanlinglong on 2018/1/11.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMsg("参数错误,品类名称为空");
        }
        //这里应该还要判断父ID不为零的时候，父ID存不存在。
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount= categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMsg("添加品类成功");
        }
        return ServerResponse.createByErrorMsg("添加品类失败");
    }

    @Override
    public ServerResponse updateCategory(String categoryName, Integer categoryId) {
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMsg("更新品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMsg("更新品类名称成功");
        }
        return ServerResponse.createByErrorMsg("更新品类名称失败");
    }

    @Override
    public ServerResponse getChildrenParallerCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            return ServerResponse.createByErrorMsg("未查询到关联的子品类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        //递归调用
        findChildCategory(categorySet,categoryId);
        List<Integer>  categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for (Category category :categorySet){
                categoryIdList.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     *  递归查询子节点
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> findChildCategory (Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
