package com.zsh.controller;

import com.zsh.pojo.Result;
import com.zsh.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {
    @Autowired
    private QuoteService quoteService;

    @GetMapping("/quotes")
    public Result getQuotes()
    {
        return quoteService.getQuotes();
    }
}
