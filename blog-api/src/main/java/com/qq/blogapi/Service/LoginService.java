package com.qq.blogapi.Service;

import com.qq.blogapi.dao.pojo.SysUser;
import com.qq.blogapi.vo.Result;
import com.qq.blogapi.vo.params.LoginParams;



public interface LoginService {
    /**
     * 登陆功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    Result register(LoginParams loginParam);
}
