package com.qq.blogapi.Controller;

import com.qq.blogapi.Service.ArticleService;
import com.qq.blogapi.Service.SysUserService;
import com.qq.blogapi.Service.TagService;
import com.qq.blogapi.common.aop.LogAnnotation;
import com.qq.blogapi.common.cache.Cache;
import com.qq.blogapi.dao.mapper.ArticleBodyMapper;
import com.qq.blogapi.dao.mapper.ArticleMapper;
import com.qq.blogapi.dao.mapper.ArticleTagMapper;
import com.qq.blogapi.vo.ArticleVo;
import com.qq.blogapi.vo.Result;
import com.qq.blogapi.vo.params.ArticleParam;
import com.qq.blogapi.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//json数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {
    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @PostMapping
    @LogAnnotation(module = "文章",operater = "获取文章列表")
    public Result listArticles(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    @PostMapping("hot")
    @Cache(expire = 5*60*1000,name = "hot_article")
    public Result hotArticle(){
        int limit =5;
    return articleService.hotArticle(limit);
    }

    /**
     * 最新文章
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5*60*1000,name = "new")
    public Result newArticles(){
        int limit =5;
        return articleService.newArticles(limit);
    }
    @PostMapping("listArchives")
    @Cache(expire = 5*60*1000,name = "listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }


    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {
        ArticleVo articleVo = articleService.findArticleById(id);
        return Result.success(articleVo);
    }
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

    @PostMapping("{id}")
    public Result view(@PathVariable("id") Long id)
    {
        ArticleVo articleById = articleService.findArticleById(id);
        return Result.success(articleById);
    }
    @GetMapping("delete/{id}")
    public  Result deleteById(@PathVariable("id")Long id){
        articleMapper.deleteById(id);
        articleBodyMapper.deleteById(id);
        articleTagMapper.deleteById(id);
        return Result.success(null);
    }
}
