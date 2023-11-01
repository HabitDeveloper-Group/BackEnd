package com.zsh.controller;

import com.zsh.pojo.Result;
import com.zsh.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * 查询所有已完成的习惯
     * @return
     */
    @GetMapping("/home/hasFinished")
    public Result selectHasFinished(){
        return habitService.selectHasFinished();
    }

    /**
     * 查询所有已失败的习惯
     * @return
     */
    @GetMapping("/home/hasFailed")
    public Result selectHasFailed(){
        return habitService.selectHasFailed();
    }

    /**
     * 根据习惯的id完成打卡操作
     * @param habitId
     * @return
     */
    @PostMapping("/home/{habitId}")
    public Result checkIn(@PathVariable Integer habitId){
        return habitService.checkIn(habitId);
    }

}
