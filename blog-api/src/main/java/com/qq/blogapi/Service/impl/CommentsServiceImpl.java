package com.qq.blogapi.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qq.blogapi.Service.CommentsService;
import com.qq.blogapi.Service.SysUserService;
import com.qq.blogapi.Service.ThreadService;
import com.qq.blogapi.dao.mapper.ArticleMapper;
import com.qq.blogapi.dao.mapper.CommentMapper;
import com.qq.blogapi.dao.pojo.Article;
import com.qq.blogapi.dao.pojo.Comment;
import com.qq.blogapi.dao.pojo.SysUser;
import com.qq.blogapi.utils.UserThreadLocal;
import com.qq.blogapi.vo.CommentVo;
import com.qq.blogapi.vo.Result;
import com.qq.blogapi.vo.UserVo;
import com.qq.blogapi.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Override
    public Result commentByArticleID(Long id) {
        /**
         * 1.根据文章id查询评论 从comments列表中查询
         * 2.根据作者id 查询作者信息
         * 3.判断level 有没有子评论
         * 4.如果有 根据评论id 进行查询 （parent_id）
         */
        LambdaQueryWrapper<Comment> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(Comment::getArticleId,id);
        QueryWrapper.eq(Comment::getLevel,1);

        List<Comment> comments = commentMapper.selectList(QueryWrapper);
        List<CommentVo> commentVoList=copylist(comments);

        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        Article article = articleMapper.selectById(commentParam.getArticleId());
        Article update = new Article();
        update.setCommentCounts(article.getCommentCounts()+1);
        LambdaUpdateWrapper<Article> articleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        articleLambdaUpdateWrapper.eq(Article::getId,article.getId());
        articleLambdaUpdateWrapper.eq(Article::getCommentCounts,article.getCommentCounts());
        articleMapper.update(update,articleLambdaUpdateWrapper);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }
    private List<CommentVo> copylist(List<Comment> comments) {
        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : comments) {
            commentVos.add(copy(comment));
        }
        return commentVos;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
        //作者信息
        Long authorId = comment.getAuthorId();
        System.out.println("=========================");

            System.out.println(authorId);

        System.out.println("=========================");
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        Integer level = comment.getLevel();
        if (level==1){
            Long id = comment.getId();
            List<CommentVo> commentVoList=findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to User 给谁评论
        //while level >1 才有to user
        if (level>1){
            Long id = comment.getId();
            UserVo touserVo = this.sysUserService.findUserVoById(authorId);
            commentVo.setToUser(touserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
         return copylist(commentMapper.selectList(queryWrapper));
    }
}
