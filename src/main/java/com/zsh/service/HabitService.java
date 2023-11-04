package com.zsh.service;

import com.zsh.pojo.Habit;
import com.zsh.pojo.Result;
import org.springframework.stereotype.Service;

import java.sql.Date;


public interface HabitService {
    /**
     * 查询所有待完成的习惯
     * @return 统一数据格式
     */
    Result selectNotFinished();

    /**
     * 查询所有已完成的习惯
     * @return 统一数据格式
     */
    Result selectHasFinished();

    /**
     * 查询所有已失败的习惯
     * @return 统一数据格式
     */
    Result selectHasFailed();

    /**
     * 根据习惯的id完成打卡操作
     * @param habitId 习惯id
     * @return 统一数据格式
     */
    Result checkIn(Integer habitId);

    /**
     * 添加一个习惯
     * @param habit 封装的习惯对象
     * @return 统一数据格式
     */
    Result addHabit(Habit habit);

    /**
     * 查询用户的所有习惯
     * @return 统一数据格式
     */
    Result list();

    /**
     * 删除一个习惯
     * @param habitId 习惯id
     * @return 统一数据格式
     */
    Result delete(Integer habitId);

    /**
     * 修改一个习惯
     * @param habit 封装的习惯对象
     * @return 统一数据格式
     */
    Result update(Habit habit);

    /**
     * 分析指定时间段内的打卡数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return 统一数据格式
     */
    Result analyse(Date begin, Date end);


    /**
     * 查询指定id的习惯信息
     * @param habitId
     * @return
     */
    Result get(Integer habitId);
}
