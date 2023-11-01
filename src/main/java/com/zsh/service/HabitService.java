package com.zsh.service;

import com.zsh.pojo.Result;
import org.springframework.stereotype.Service;


public interface HabitService {
    /**
     * 查询所有待完成的习惯
     * @return
     */
    Result selectNotFinished();

    /**
     * 查询所有已完成的习惯
     * @return
     */
    Result selectHasFinished();

    /**
     * 查询所有已失败的习惯
     * @return
     */
    Result selectHasFailed();

    /**
     * 根据习惯的id完成打卡操作
     * @param habitId
     * @return
     */
    Result checkIn(Integer habitId);
}
