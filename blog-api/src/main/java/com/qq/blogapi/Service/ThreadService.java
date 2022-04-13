package com.qq.blogapi.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qq.blogapi.dao.mapper.ArticleMapper;
import com.qq.blogapi.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(article.getViewCounts()+1);
        LambdaQueryWrapper<Article> UpdateWrapper = new LambdaQueryWrapper<>();
        UpdateWrapper.eq(Article::getId,article.getId());
        //设置一个，为了线程安全
        UpdateWrapper.eq(Article::getViewCounts,article.getViewCounts());
        //update article set view_count=100 where view_count=99 ad id =11
        articleMapper.update(articleUpdate,UpdateWrapper);

    }

}
