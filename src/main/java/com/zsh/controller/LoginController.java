package com.zsh.controller;

import com.zsh.pojo.Result;
import com.zsh.pojo.User;
import com.zsh.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    /**
     * 响应用户名判重请求
     * @return
     */
    @GetMapping("/user/register")
    public Result userNameCheckExisted(String username){
        log.info("收到/user/register GET 用户名判重请求");

        return loginService.userNameCheckExisted(username);
    }

    @PostMapping("/user/register")
    public Result register(@RequestBody User user){
        log.info("收到/user/register POST 注册请求");
        return loginService.register(user);

    }

    @PostMapping("/user/login")
    public Result login(@RequestBody User user){
        //登录
        return loginService.login(user);
    }

    @RequestMapping("/user/logout")
    public Result logout(){
        return loginService.logout();
    }
}
