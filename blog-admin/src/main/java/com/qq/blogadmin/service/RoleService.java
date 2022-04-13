package com.qq.blogadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qq.blogadmin.mapper.AdminMapper;
import com.qq.blogadmin.mapper.SysUserMapper;
import com.qq.blogadmin.model.params.PageParam;
import com.qq.blogadmin.pojo.Admin;
import com.qq.blogadmin.pojo.PageResult;
import com.qq.blogadmin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private AdminMapper adminMapper;
        public Result roleList(PageParam pageParam) {
            Page<Admin> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
            LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(pageParam.getQueryString())) {
                queryWrapper.eq(Admin::getId,pageParam.getQueryString());
            }
            Page<Admin> sysUserPage = adminMapper.selectPage(page, queryWrapper);
            PageResult<Admin> PageResult = new PageResult<>();
            PageResult.setList(sysUserPage.getRecords());
            PageResult.setTotal(sysUserPage.getTotal());
            return Result.success(PageResult);
        }


}

