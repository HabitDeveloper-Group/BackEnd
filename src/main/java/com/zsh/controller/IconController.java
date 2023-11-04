package com.zsh.controller;

import com.zsh.mapper.IconMapper;
import com.zsh.pojo.Result;
import com.zsh.service.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IconController {
    @Autowired
    private IconService iconService;

    @GetMapping("/icons")
    public Result listIcons(){
        return iconService.listIcons();
    }
}
