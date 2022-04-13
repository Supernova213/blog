package com.qq.blogapi.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserVo {

    private String nickname;
    //@JsonSerialize(using = ToStringSerializer.class)
    private String avatar;
    //@JsonSerialize(using = ToStringSerializer.class)
    private String id;
}