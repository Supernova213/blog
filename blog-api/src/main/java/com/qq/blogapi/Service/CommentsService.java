package com.qq.blogapi.Service;

import com.qq.blogapi.dao.pojo.Article;
import com.qq.blogapi.vo.Result;
import com.qq.blogapi.vo.params.CommentParam;

public interface CommentsService {
    Result commentByArticleID(Long id);

    Result comment(CommentParam commentParam);
}
