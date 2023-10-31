package com.zsh.service;

import com.zsh.pojo.Result;
import org.springframework.stereotype.Service;


public interface HabitService {
    /**
     * 查询所有待完成的习惯
     * @return
     */
    Result selectNotFinished();
}
