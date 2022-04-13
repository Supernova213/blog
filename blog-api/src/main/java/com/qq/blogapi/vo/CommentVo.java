package com.qq.blogapi.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class CommentVo  {
    //防止精度损失
    //@JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private UserVo author;
    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}

