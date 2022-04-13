package com.qq.blogadmin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
@Data
public class pageReturn {
    @JsonAlias({"id","adminId"})
    private long adminId;
    @JsonAlias("permissionId")
    private long permissionId;
}
