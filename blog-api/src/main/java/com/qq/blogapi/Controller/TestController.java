package com.qq.blogapi.Controller;

import com.qq.blogapi.dao.pojo.SysUser;
import com.qq.blogapi.utils.UserThreadLocal;
import com.qq.blogapi.vo.Result;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
