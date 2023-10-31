package com.zsh.service.impl;

import com.zsh.mapper.UserMapper;
import com.zsh.pojo.LoginUser;
import com.zsh.pojo.Result;
import com.zsh.pojo.User;
import com.zsh.service.LoginService;
import com.zsh.utils.JWTUtils;
import com.zsh.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(User user) {
        //AUthenticationManager authenticate进行用户认证
        //user的用户名和密码封装成Authentication类型
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate  = authenticationManager.authenticate(authenticationToken);  // 这个函数回去调用UserDetailsService中的方法查询数据库

        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authenticate)){
            return Result.error("用户名或密码错误");
        }

        //如果认证通过了，使用userid生成一个jwt
        LoginUser loginUser =(LoginUser) authenticate.getPrincipal();
        String username = loginUser.getUser().getUserName().toString();
        log.info("用户名：{}",username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("username",username);
        String jwt = JWTUtils.generateJwt(claims);

        //把完整的用户信息存入redis，userid作为key
        redisCache.setCacheObject("login:"+username,loginUser);


        return Result.success(jwt);
    }

    @Override
    public Result logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authenticationToken= (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        String username = loginUser.getUser().getUserName();
        //删除redis
        redisCache.deleteObject("login:"+username);
        return Result.success();
    }



    @Override
    public Result userNameCheckExisted(String username) {
        // 调用userMapper方法，查询数据库
        User user = userMapper.selectByUserName(username);
        //如果查询结果为空
        if(Objects.isNull(user)){
            return Result.success();
        }
        else{
            return Result.error("error");
        }
    }

    @Override
    public Result register(User user) {
        //处理密码，并向tb_users表中插入数据
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return Result.success();
    }


}
