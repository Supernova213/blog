package com.qq.blogapi.Service;

import com.qq.blogapi.dao.pojo.SysUser;
import com.qq.blogapi.vo.Result;
import com.qq.blogapi.vo.UserVo;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @return
     * @param token
     */
    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);
    UserVo findUserVoById(Long id);

    void save(SysUser sysUser);
    SysUser checkToken(String token);
}
