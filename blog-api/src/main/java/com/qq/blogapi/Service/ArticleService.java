package com.qq.blogapi.Service;

import com.qq.blogapi.vo.ArticleVo;
import com.qq.blogapi.vo.Result;
import com.qq.blogapi.vo.params.ArticleParam;
import com.qq.blogapi.vo.params.PageParams;

import java.util.List;

public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param id
     * @return
     */
    ArticleVo findArticleById(Long id);

    Result publish(ArticleParam articleParam);
}
