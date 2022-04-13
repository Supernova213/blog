package com.qq.blogapi.vo.params;

import com.qq.blogapi.vo.CategoryVo;
import com.qq.blogapi.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
