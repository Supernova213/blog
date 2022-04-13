package com.qq.blogadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qq.blogadmin.pojo.Permission;
import com.qq.blogadmin.pojo.pageReturn;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> findPermissionsByAdminId(Long adminId);
    void add(pageReturn pagereturn);
}