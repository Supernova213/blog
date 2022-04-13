package com.qq.blogapi.Service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qq.blogapi.Service.LoginService;
import com.qq.blogapi.Service.SysUserService;
import com.qq.blogapi.dao.mapper.SysUserMapper;
import com.qq.blogapi.dao.pojo.SysUser;
import com.qq.blogapi.utils.JWTUtils;
import com.qq.blogapi.vo.ErrorCode;
import com.qq.blogapi.vo.LoginUserVo;
import com.qq.blogapi.vo.Result;
import com.qq.blogapi.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysUSerServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser=new SysUser();
            sysUser.setNickname("qq");
            sysUser.setAvatar("/static/user/user_1.png");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(SysUser::getAccount,account);
        QueryWrapper.eq(SysUser::getPassword,password);
        QueryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        QueryWrapper.last("limit 1");
        return sysUserMapper.selectOne(QueryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         * 是否为空，解析是否成功 redis是否存在
         * 2.如果校验失败，返回错误
         * 3.如果成功，返回对应结果  LoginUserVo
         */

        SysUser sysUser =this.checkToken(token);
        if (sysUser==null){

                return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());

        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount,account);
        wrapper.last("limit 1");
        return this.sysUserMapper.selectOne(wrapper);
    }

    @Override
    public UserVo findUserVoById(Long id){
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser==null){
            sysUser=new SysUser();
            sysUser.setId(1L);
            sysUser.setNickname("qq");
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(id));
        return userVo;
    }

    @Override
    public void save(SysUser sysUser) {
    this.sysUserMapper.insert(sysUser);
    }
    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap==null){
            return null;
        }
        String userJSON = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (userJSON==null){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJSON,SysUser.class);
        return sysUser;
    }
}
