package com.qq.blogapi.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qq.blogapi.Service.CategoryService;
import com.qq.blogapi.dao.mapper.ArticleMapper;
import com.qq.blogapi.dao.mapper.CategoryMapper;
import com.qq.blogapi.dao.pojo.Category;
import com.qq.blogapi.vo.CategoryVo;
import com.qq.blogapi.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public CategoryVo findCategoryById(Long categoryId) {
            Category category = categoryMapper.selectById(categoryId);
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(category.getId().toString());
            BeanUtils.copyProperties(category,categoryVo);
            return categoryVo;
        }


    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        System.out.println(categoryVo.getId());
        return categoryVo;
    }
    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = this.categoryMapper.selectList(Wrapper);

        return Result.success(copyList(categories));
    }

    @Override
    public Result findAllDetail() {
        List<Category> categories = this.categoryMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(categories));
    }

    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

}
