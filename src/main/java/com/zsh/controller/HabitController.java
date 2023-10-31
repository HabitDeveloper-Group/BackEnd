package com.zsh.controller;

import com.zsh.pojo.Result;
import com.zsh.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HabitController {
    @Autowired
    private HabitService habitService;

    /**
     * 查询所有待完成的习惯
     * @return
     */
    @GetMapping("/home")
    public Result selectNotFinished(){
        return  habitService.selectNotFinished();

    }

}
