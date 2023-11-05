package com.zsh.controller;

import com.zsh.pojo.Habit;
import com.zsh.pojo.Result;
import com.zsh.service.HabitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@Slf4j
@RestController
public class HabitController {
    @Autowired
    private HabitService habitService;

    /**
     * 响应查询待完成习惯请求
     * @return 统一数据格式
     */
    @GetMapping("/home")
    public Result selectNotFinished(){
        log.info("查询未完成的习惯");
        return  habitService.selectNotFinished();
    }

    /**
     * 响应查询已完成习惯请求
     * @return 统一数据格式
     */
    @GetMapping("/home/hasFinished")
    public Result selectHasFinished(){
        log.info("查询已完成的习惯");
        return habitService.selectHasFinished();
    }

    /**
     * 响应查询失败习惯请求
     * @return 统一数据格式
     */
    @GetMapping("/home/hasFailed")
    public Result selectHasFailed(){
        log.info("查询已失败的习惯");
        return habitService.selectHasFailed();
    }

    /**
     * 响应习惯打卡请求
     * @param habitId 习惯id
     * @return 统一数据格式
     */
    @PostMapping("/home/{habitId}")
    public Result checkIn(@PathVariable Integer habitId){
        log.info("打卡习惯:{}",habitId);
        return habitService.checkIn(habitId);
    }

    /**
     * 响应添加习惯请求
     * @param habit 用来封装json数据的习惯对象
     * @return 统一数据格式
     */
    @PostMapping("/habits")
    public Result addHabit(@RequestBody Habit habit){
        log.info("添加习惯:{}",habit.getHabitName());
        return habitService.addHabit(habit);
    }

    /**
     * 响应查询所有习惯请求
     * @return 统一数据格式
     */
    @GetMapping("/habits")
    public Result list()
    {
        log.info("列举所有习惯");
        return habitService.list();
    }

    /**
     * 响应删除指定习惯的请求
     * @param habitId 习惯id
     * @return 统一数据格式
     */
    @DeleteMapping("/habits/{habitId}")
    public Result delete(@PathVariable Integer habitId)
    {
        log.info("删除习惯：{}",habitId);
        return habitService.delete(habitId);
    }

    /**
     * 响应修改指定习惯的请求
     * @param habit 封装的习惯对象
     * @return 统一数据格式
     */
    @PutMapping("/habits")
    public Result update(@RequestBody Habit habit)
    {
        log.info("修改习惯:{}",habit.getHabitName());
        return habitService.update(habit);
    }

    /**
     * 响应分析一周内数据的请求
     * @param begin 开始日期
     * @param end 结束日期
     * @return 统一数据格式
     */
    @GetMapping("/analysis")
    public Result analyse(Date begin, Date end)
    {
        log.info("分析数据");
        return habitService.analyse(begin, end);
    }


    @GetMapping("/habits/{habitId}")
    public Result get(@PathVariable Integer habitId){
        log.info("查询习惯:{}",habitId);
        return habitService.get(habitId);
    }


}
