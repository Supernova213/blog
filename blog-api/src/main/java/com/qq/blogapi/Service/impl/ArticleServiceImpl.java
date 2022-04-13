package com.qq.blogapi.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qq.blogapi.Service.*;
import com.qq.blogapi.dao.dos.Archives;
import com.qq.blogapi.dao.mapper.ArticleBodyMapper;
import com.qq.blogapi.dao.mapper.ArticleMapper;
import com.qq.blogapi.dao.mapper.ArticleTagMapper;
import com.qq.blogapi.dao.pojo.Article;
import com.qq.blogapi.dao.pojo.ArticleBody;
import com.qq.blogapi.dao.pojo.ArticleTag;
import com.qq.blogapi.dao.pojo.SysUser;
import com.qq.blogapi.utils.UserThreadLocal;
import com.qq.blogapi.vo.*;
import com.qq.blogapi.vo.params.ArticleParam;
import com.qq.blogapi.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private RedisTemplate redisTemplate;
//    @Override
//    public Result listArticle(PageParams pageParams) {
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
//        return Result.success(copyList(articleIPage.getRecords(),true,true));
//    }
@Override
public Result listArticle(PageParams pageParams) {
    Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());

    IPage<Article> articleIPage = articleMapper.listArticle(
            page,
            pageParams.getCategoryId(),
            pageParams.getTagId(),
            pageParams.getYear(),
            pageParams.getMonth());
    List<Article> records = articleIPage.getRecords();
    return Result.success(copyList(records,true,true));
}
//    @Override
//    public Result listArticle(PageParams pageParams) {
//        Page<Article> page =new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> Wrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId()!=null){
//            Wrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//            //and category_id=#{categoryID}
//        }
//        ArrayList<Long> articleIDList = new ArrayList<>();
//        if (pageParams.getTagId()!=null){
//            //article_tag表中 articleId  1:n  tag_id
//            LambdaQueryWrapper<ArticleTag> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIDList.add(articleTag.getArticleId());
//            }
//            if (articleIDList.size()>0){
//                Wrapper.in(Article::getId,articleIDList);
//            }
//        }
//        /**
//         * 方法过期
//         */
//        Wrapper.orderByDesc(Article::getWeight);
//        Wrapper.orderByDesc(Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, Wrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList =copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.orderByDesc(Article::getCreateDate);
        Wrapper.select(Article::getId,Article::getTitle);
        Wrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(Wrapper);
        return Result.success(copyList(articles,false,false,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.orderByDesc(Article::getViewCounts);
        Wrapper.select(Article::getId,Article::getTitle);
        Wrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(Wrapper);
        return Result.success(copyList(articles,false,false,false,false));
    }

    @Override
    public Result listArchives() {
       List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }
    /**
     * 1.根据id查询 文章信息
     * 2.根据bodyid和categoryid 做关联查询
     *
     */
    @Autowired
    private ThreadService threadService;
    @Override
    public ArticleVo findArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        threadService.updateArticleViewCount(articleMapper,article);
        return copy(article,true,true,true,true);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        if (articleParam.getId()!=null){
            articleMapper.deleteById(articleParam.getId());
            articleBodyMapper.deleteById(articleParam.getId());
            articleTagMapper.deleteById(articleParam.getId());
        }
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        this.articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        if (tags!=null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }

        }

        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContent(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String, String> Map = new HashMap<>();
        Map.put("id",article.getId().toString());
        return Result.success(Map);
    }


    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,false));
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }
    @Autowired
    private CategoryService categoryService;
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));

        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
            articleVo.setAuthorId(sysUserService.findUserById(authorId).getId());
            articleVo.setAvatar(sysUserService.findUserById(authorId).getAvatar());

        }
        if (isBody){
            ArticleBodyVo articleBody = findArticleBody(article.getId());
            articleVo.setBody(articleBody);
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategorys(categoryService.findCategoryById(categoryId));
        }
        return articleVo;

    }
    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBody(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId, articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
