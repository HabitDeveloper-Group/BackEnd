package com.zsh.service.impl;

import com.zsh.mapper.UserMapper;
import com.zsh.pojo.LoginUser;
import com.zsh.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        User user = userMapper.selectByUserName(username);
        // 如果没有查询到用户，就抛出异常
        if(Objects.isNull(user)){
            throw  new RuntimeException("用户名或密码错误");
        }
        //TODO 查询对应的权限信息


        //把数据封装成UserDetails返回
        return new LoginUser(user);
    }
}
