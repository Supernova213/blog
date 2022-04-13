package com.qq.blogapi.Service;

import com.qq.blogapi.vo.Result;
import com.qq.blogapi.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
