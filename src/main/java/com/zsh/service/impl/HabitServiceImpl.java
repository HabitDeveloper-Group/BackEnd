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
        //    获得用户认证信息
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //    获得当前登录用户的id
        Integer userId = loginUser.getUser().getUserId();
        //调用Mapper层接口查询数据库
        List<Habit> list = habitMapper.selectNotFinished(userId);
        HashMap<String,Object> res = packageIntoMap(list);
        return Result.success(res);

    }

    @Override
    public Result selectHasFinished() {
        //    获得用户认证信息
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //    获得当前登录用户的id
        Integer userId = loginUser.getUser().getUserId();
        List<Habit> list = habitMapper.selectHasFinished(userId);
        HashMap<String,Object> res = packageIntoMap(list);
        return Result.success(res);


    }

    @Override
    public Result selectHasFailed() {
        //    获得用户认证信息
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //    获得当前登录用户的id
        Integer userId = loginUser.getUser().getUserId();
        List<Habit> list = habitMapper.selectHasFailed(userId);
        HashMap<String,Object> res = packageIntoMap(list);
        return Result.success(res);
    }

    @Override
    public Result checkIn(Integer habitId) {
        //TODO 添加事务

        //根据习惯id，将指定的习惯的hasDoneTimes+1
        habitMapper.updateHasDoneTimesById(habitId);
        //查询该习惯的下限和已打卡次数，判断是否已完成该习惯
        Integer lowerLimit = habitMapper.getLowerLimitById(habitId);
        Integer hasDoneTimes = habitMapper.getHasDoneTimes(habitId);
        if(hasDoneTimes >= lowerLimit){    // 如果已完成该习惯

            try {
                //获取用户id
                LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Integer userId = loginUser.getUser().getUserId();
                // 更新tb_habit_records
                habitMapper.updateHabitRecord(userId,habitId);


                //再判断该用户是否已经完成当天所有习惯
                Integer notFinishedNum = habitMapper.getNotFinishedNumByUserId(userId);
                if(notFinishedNum == 0){   //全部完成,更新tb_date_records
                    habitMapper.updateDateRecord(userId);
                }
            } catch (Exception e) {
                throw new RuntimeException("用户完成习惯，但更新数据库时出错");
            }

        }
        return Result.success();
    }

    /**
     * 将查询到的列表封装成响应数据
     * @param list
     * @return
     */
    private HashMap<String, Object> packageIntoMap(List<Habit>list){
        Integer total = list.size();
        HashMap<String,Object> res = new HashMap<>();
        res.put("total",total);
        res.put("rows",list);
        return res;
    }
}
