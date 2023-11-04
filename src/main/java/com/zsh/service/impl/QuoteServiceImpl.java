package com.zsh.service.impl;

import com.zsh.mapper.QuoteMapper;
import com.zsh.pojo.Result;
import com.zsh.service.QuoteService;
import com.zsh.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class QuoteServiceImpl implements QuoteService {
    @Autowired
    private QuoteMapper quoteMapper;
    @Override
    public Result getQuotes() {
        List<HashMap<String, String>> quotes = quoteMapper.list();
        //每天从中获取三句
        Date today = Date.valueOf(LocalDate.now());
        Date base = Date.valueOf("2023-11-01");
        int diff = MyUtils.getDiffDays(today, base);
        List<HashMap<String,String>> data = new ArrayList<>();
        data.add(quotes.get(diff * 3 % quotes.size()));
        data.add(quotes.get((diff * 3 + 1) % quotes.size()));
        data.add(quotes.get((diff * 3 + 2) % quotes.size()));

        return Result.success(data);


    }
}
