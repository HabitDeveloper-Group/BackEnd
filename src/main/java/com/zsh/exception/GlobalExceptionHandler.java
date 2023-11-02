package com.zsh.exception;

import com.zsh.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  //其中的ResponseBody可以将返回的Result转换为前端需要的json格式
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)  //捕获所有异常
    public Result ex(Exception ex){
        ex.printStackTrace();
        return Result.error(ex.getMessage());
    }
}
