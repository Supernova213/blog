package com.qq.blogapi.Service;

import com.qq.blogapi.vo.CategoryVo;
import com.qq.blogapi.vo.Result;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
