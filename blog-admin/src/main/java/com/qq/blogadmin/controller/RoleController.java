package com.qq.blogadmin.controller;
import com.qq.blogadmin.mapper.AdminMapper;
import com.qq.blogadmin.mapper.PermissionMapper;
import com.qq.blogadmin.model.params.PageParam;
import com.qq.blogadmin.pojo.pageReturn;
import com.qq.blogadmin.service.AdminService;
import com.qq.blogadmin.service.AuthService;
import com.qq.blogadmin.service.MySimpleGrantedAuthority;
import com.qq.blogadmin.service.RoleService;
import com.qq.blogadmin.vo.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("user")
public class RoleController {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionMapper permissionMapper;
    @PostMapping("list")
    public Result roleList(@RequestBody PageParam pageParam){
        return roleService.roleList(pageParam);
    }
    @PostMapping("add")
    public Result add(@RequestBody pageReturn pageReturn){
        this.permissionMapper.add(pageReturn);
        return Result.success(null);
    }
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        adminMapper.deleteById(id);
        return Result.success(null);
    }

}
