package com.zsh.service;

import com.zsh.pojo.Result;

public interface QuoteService {
    /**
     * 从数据库表中获取名言警句
     * @return
     */
    Result getQuotes();
}
