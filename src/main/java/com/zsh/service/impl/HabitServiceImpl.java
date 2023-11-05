package com.zsh.service.impl;
import com.zsh.mapper.HabitMapper;
import com.zsh.pojo.Habit;
import com.zsh.pojo.LoginUser;
import com.zsh.pojo.Result;
import com.zsh.service.HabitService;
import com.zsh.utils.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Math.max;

@Slf4j
@Service
public class HabitServiceImpl implements HabitService {
    @Autowired
    private HabitMapper habitMapper;
    //TODO 如何复用用户认证信息
    @Override
    public Result selectNotFinished() {
        //    获得用户认证信息
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //    获得当前登录用户的id
        Integer userId = loginUser.getUser().getUserId();
        //调用Mapper层接口查询数据库
        List<Habit> list = habitMapper.selectNotFinished(userId);
        HashMap<String,Object> res = packageIntoMap(list,userId);
        return Result.success(res);

    }

    @Override
    public Result selectHasFinished() {
        //    获得用户认证信息
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //    获得当前登录用户的id
        Integer userId = loginUser.getUser().getUserId();
        List<Habit> list = habitMapper.selectHasFinished(userId);
        HashMap<String,Object> res = packageIntoMap(list,userId);
        return Result.success(res);


    }

    @Override
    public Result selectHasFailed() {
        //    获得用户认证信息
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //    获得当前登录用户的id
        Integer userId = loginUser.getUser().getUserId();
        List<Habit> list = habitMapper.selectHasFailed(userId);
        HashMap<String,Object> res = packageIntoMap(list,userId);
        return Result.success(res);
    }

    @Override
    public Result checkIn(Integer habitId) {
        //TODO 添加事务
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = loginUser.getUser().getUserId();
        //首先要判断是否在打卡时间限制之内
        HashMap<String, Time> timeSection =  habitMapper.getTimeSection(habitId);
        System.out.println(timeSection);
        Time now = Time.valueOf(LocalTime.now());
        if(now.toLocalTime().isBefore(timeSection.get("earliest_time").toLocalTime() )||
                now.toLocalTime().isAfter(timeSection.get("latest_time").toLocalTime())){
            throw  new RuntimeException("当前不在打卡时间内，您无法打卡");
        }
        //根据习惯id，将指定的习惯的hasDoneTimes+1
        habitMapper.updateHasDoneTimesById(habitId);
        try {
            //检查该习惯是否完成
            checkFinished(habitId,userId);
            //检查是否完成当日全部习惯
            checkFinishedAll(userId);
        } catch (Exception e) {
            throw new RuntimeException("用户打卡时更新数据库时出错");
        }
        return Result.success();
    }

    @Override
    public Result addHabit(Habit habit) {
        //首先检查开始时间与结束时间是否合法
        checkValidOfTime(habit);
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = loginUser.getUser().getUserId();

        try {
            habit.setAddedTime(LocalDate.now());
            habitMapper.insertHabit(habit, userId);
        } catch (Exception e) {
            throw new RuntimeException("添加失败!"+"已经存在一个习惯名为:"+habit.getHabitName());
        }
        // 重新检查今日是否完成全部习惯
        checkFinishedAll(userId);
        return Result.success();
    }

    @Override
    public Result list() {
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = loginUser.getUser().getUserId();
        List<Habit>list =  habitMapper.listHabits(userId);
        return Result.success(packageIntoMap(list,userId));
    }

    @Transactional
    @Override
    public Result delete(Integer habitId) {
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = loginUser.getUser().getUserId();
        try {
            //删除tb_habits中的数据
            habitMapper.deleteHabit(habitId,userId);
            //删除tb_habit_record中的记录
            habitMapper.deleteHabitRecord(habitId,userId);
            //重新检查当日的习惯是否全部完成
        } catch (Exception e) {
            throw new RuntimeException("删除失败!");
        }
        checkFinishedAll(userId);
        return Result.success();
    }

    @Transactional
    @Override
    public Result update(Habit habit) {
        checkValidOfTime(habit);
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = loginUser.getUser().getUserId();
        try {
            habitMapper.updateHabit(habit);
        } catch (Exception e) {
            throw new RuntimeException("修改失败!"+"已经存在一个习惯名为:"+habit.getHabitName());
        }
        //可能会修改打卡最低下限，因此会影响到是否完成习惯，和是否完成当天所有习惯
        checkFinished(habit.getHabitId(),userId);
        checkFinishedAll(userId);
        return Result.success();
    }

    @Transactional
    @Override
    public Result analyse(Date begin, Date end) {
        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = loginUser.getUser().getUserId();
        int timeLength = MyUtils.getDiffDays(end, begin)+1;
//        获取rows
        List<Habit> habitList = habitMapper.listHabits(userId);          //首先查询所有习惯
        List<HashMap<String, Object>> rows = packageRowsForAnalyse(habitList,begin,end,userId);          //rows为最后返回的一个数据

//        计算finishingRate
        Integer countOfFinishedHabitsInWeek = habitMapper.getCountOfFinishedHabitsInWeek(begin, end, userId); //查询完成的习惯总数和总习惯数,并计算总完成数
        Integer countOfHabitsInWeek = habitMapper.getCountOfHabitsInWeek(begin, end, userId);
        float finishingRate = (float) (countOfFinishedHabitsInWeek * 1.0 / countOfHabitsInWeek * 100);
        //查询完成全部任务的天数
        Integer perfectDays = habitMapper.getCountsOfPerfectDays(begin,end, userId);

//        计算最大连续完美打卡的天数
        List<Integer> records = habitMapper.getFinishedRecordsInWeek(begin, end, userId);         //首先获得时间段内的每天的完成与否列表
        Integer maxConsecutiveDays = MyUtils.getMaxConsecutiveOnes(records);                    //调用工具类中实现的方法，获得list中最大大连续1的个数

//        平均每天完成的习惯数量
        float averagePerDay = (float) countOfFinishedHabitsInWeek / timeLength;

//        封装数据并返回
        HashMap<String,Object> data = new HashMap<>();
        data.put("total",habitList.size());
        data.put("rows",rows);
        data.put("finishingRate",finishingRate);
        data.put("perfectDays",perfectDays);
        data.put("maxConsecutiveDays",maxConsecutiveDays);
        data.put("averagePerDay",averagePerDay);
        return Result.success(data);
    }

    @Override
    public Result get(Integer habitId) {
        Habit habit = habitMapper.getHabitById(habitId);
        return Result.success(habit);
    }


    /**
     * 将查询到的列表封装成响应数据
     * @param list 带封装的数据列表
     * @return 封装好的HashMap
     */
    private HashMap<String, Object> packageIntoMap(List<Habit>list, Integer userId){
        Integer total = list.size();
        List<HashMap<String,Object>>rows  = new ArrayList<>();
        HashMap<String,Object> res = new HashMap<>();
        list.stream().forEach(habit -> {
            HashMap<String,Object> item = new HashMap<>();
            item.put("habit",habit);
            Integer finishedDays = habitMapper.getFinishedTimesByHabitId(habit.getHabitId(), userId);
            item.put("finishedDays",finishedDays);
            rows.add(item);
        });
        res.put("total",total);
        res.put("rows",rows);
        return res;
    }


    /**
     * 检查用户是否完成该习惯，在打卡之后调用
     * @param habitId 习惯id
     * @param userId 用户id
     */
    private void checkFinished(Integer habitId, Integer userId){
        //查询该习惯的下限和已打卡次数，判断是否已完成该习惯
        Integer lowerLimit = habitMapper.getLowerLimitById(habitId);
        Integer hasDoneTimes = habitMapper.getHasDoneTimes(habitId);
        if(hasDoneTimes >= lowerLimit) {    // 如果已完成该习惯
            // 更新tb_habit_records
            habitMapper.updateHabitRecord(userId,habitId);

        }
    }
    /**
     * 检查用户是否已经完成当天的所有习惯，并更新tb_date_records表
     * 需要在打卡，添加习惯，删除，修改习惯时调用
     * @param userId 用户id
     */
    private void checkFinishedAll(Integer userId){
        // 通过用户id查询tb_habits表，查询未完成习惯数量
        Integer notFinishedNum = habitMapper.getNotFinishedNumByUserId(userId);
//        log.info("未完成的习惯数量为：{}",notFinishedNum);
        if(notFinishedNum == 0){   //全部完成,更新tb_date_records
//            log.info("将当天hasFinishedAll置1");
            habitMapper.updateDateRecord(userId,1);
        }
        else{
//            log.info("将当天hasFinishedAll置0");
            habitMapper.updateDateRecord(userId,0);
        }

    }

    /**
     * 在分析过程中调用，封装rows，并返回
     * @param habitList 查询到的习惯列表
     * @param begin 开始日期
     * @param end 结束日期
     * @param userId 用户id
     * @return 封装好的rows数据
     */
    private List<HashMap<String,Object>> packageRowsForAnalyse(List<Habit> habitList,Date begin, Date end, Integer userId){
        Integer timeLength = MyUtils.getDiffDays(end, begin) + 1;
        List<HashMap<String,Object>> rows = new ArrayList<>();
        //对每个习惯，查询其七天的完成记录，存储为recordItem，最后habit和recordItem封装成HashMap，并放到rows中
        habitList.stream().forEach(habit -> {
            List<Date> recordDates= habitMapper.getFinshedDatesByHabitId(begin,end,habit.getHabitId(),userId);
            List<Integer> recordItem = new ArrayList<>( Collections.nCopies(timeLength, 0));
            recordDates.stream().forEach(date -> {
                Integer index = MyUtils.getDiffDays(date,begin);
                recordItem.set(index,1);

            });
            HashMap<String,Object>mapItem = new HashMap<>();
            mapItem.put("habit",habit);
            mapItem.put("records",recordItem);
            rows.add(mapItem);
        });

        return rows;
    }
    private void checkValidOfTime(Habit habit){
        System.out.println(habit);
        if(habit.getEarliestTime().isAfter(habit.getLatestTime())){
            throw new RuntimeException("操作失败，请检查开始时间与结束时间是否合法!");
        }
    }


}
