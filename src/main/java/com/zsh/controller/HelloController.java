package com.zsh.controller;

import com.zsh.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Result Hello(){
        log.info("有人访问了该路径");
        return Result.success("hello");
    }
}
