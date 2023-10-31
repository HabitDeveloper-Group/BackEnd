package com.zsh.service.impl;
import com.zsh.mapper.HabitMapper;
import com.zsh.pojo.Habit;
import com.zsh.pojo.LoginUser;
import com.zsh.pojo.Result;
import com.zsh.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class HabitServiceImpl implements HabitService {
    @Autowired
    private HabitMapper habitMapper;
    @Override
    public Result selectNotFinished() {
        //获得当前用户的id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer userId = loginUser.getUser().getUserId();
        //调用Mapper层接口查询数据库
        List<Habit> list = habitMapper.selectNotFinished(userId);
        Integer total = list.size();
        HashMap<String,Object> res = new HashMap<>();
        res.put("total",total);
        res.put("rows",list);
        return Result.success(res);

    }
}
