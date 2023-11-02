package com.zsh.mapper;

import com.zsh.pojo.Habit;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface HabitMapper {

    /**
     * 查询tb_habits:查询待完成的习惯
     * @param userId:用户id
     * @return 对象列表
     */
    @Select("select habit_id, habit_name, icon, tag, description, earliest_time, latest_time, lower_limit, added_time, has_done_times " +
            "from tb_habits where user_id = #{userId} and has_done_times < lower_limit")
    List<Habit> selectNotFinished(Integer userId);

    /**
     * 查询tb_habits:查询已完成的习惯
     * @param userId:用户id
     * @return 对象列表
     */
    @Select("select habit_id, habit_name, icon, tag, description, earliest_time, latest_time, lower_limit, added_time, has_done_times " +
            "from tb_habits where user_id = #{userId} and has_done_times >= lower_limit")
    List<Habit> selectHasFinished(Integer userId);


    /**
     * 查询tb_habits:查询已失败(来不及)的习惯
     * @param userId:用户id
     * @return 对象列表
     */
    @Select("select habit_id, habit_name, icon, tag, description, earliest_time, latest_time, lower_limit, added_time, has_done_times " +
            "from tb_habits " +
            "where user_id = #{userId} " +
            "and has_done_times < tb_habits.lower_limit " +
            "and curtime() > latest_time")
    List<Habit> selectHasFailed(Integer userId);

    /**
     * 更新tb_habits:将指定习惯id的已完成次数+1，实现打卡操作
     * @param habitId:习惯id
     */
    @Update("update tb_habits set has_done_times = has_done_times + 1 where habit_id = #{habitId}")
    void updateHasDoneTimesById(Integer habitId);

    /**
     * 查询tb_habits:根据习惯id查询下限
     * @param habitId:习惯id
     * @return 打卡下限
     */
    @Select("select lower_limit from tb_habits where habit_id = #{habitId}")
    Integer getLowerLimitById(Integer habitId);

    /**
     * 查询tb_habits:根据习惯id查询已打卡次数
     * @param habitId:习惯id
     * @return 打卡次数
     */
    @Select("select has_done_times from tb_habits where habit_id = #{habitId}")
    Integer getHasDoneTimes(Integer habitId);

    /**
     * 更新tb_hatit_records:记录用户当天完成某习惯
     * @param userId:用户id
     * @param habitId:习惯id
     */
    @Insert("insert into tb_habit_records(user_id, date, habit_id) VALUES (#{userId},curdate(),#{habitId})")
    void updateHabitRecord(Integer userId, Integer habitId);

//    @Insert("insert into tb_date_records(user_id, date, finished_all) VALUES (#{userId},curdate(),1)")

    /**
     * 更新tb_date_records:记录用户当天已完成所有习惯
     * @param userId:用户id
     */
    @Update("update tb_date_records set finished_all = #{hasFinished} where user_id = #{userId} and date = curdate()")
    void updateDateRecord(Integer userId,Integer hasFinished);

    /**
     * 查询tb_habits:根据用户查询当天未完成的习惯数量
     * @param userId:用户id
     * @return 未完成的习惯数量
     */
    @Select("select count(*) from tb_habits " +
            "where user_id = #{userId} and has_done_times < tb_habits.lower_limit")
    Integer getNotFinishedNumByUserId(Integer userId);

    /**
     * 插入tb_habits:将习惯和所属用户添加到表中
     * 修改tb_date_records:当天习惯总数+1
     * @param habit 习惯对象
     * @param userId 用户id
     */

    void insertHabit(@Param("habit") Habit habit,@Param("userId") Integer userId);

    /**
     * 查询tb_habits:查询用户所有习惯
     * @param userId 用户id
     * @return 习惯列表
     */
    @Select("select habit_id, habit_name, icon, tag, description, earliest_time, latest_time, lower_limit, added_time, has_done_times " +
            "from tb_habits where user_id = #{userId}")
    List<Habit> listHabits(Integer userId);

    /**
     * 删除tb_habits:删除对应id的习惯
     * 更新tb_date_records:当天习惯总数-1
     *
     * @param habitId 习惯id
     * @param userId
     */
    void deleteHabit(@Param("habitId") Integer habitId,@Param("userId") Integer userId);

    /**
     * 删除tb_habit_records:删除用户对应的习惯完成记录
     * @param habitId
     * @param userId
     */
    @Delete("delete from tb_habit_records where user_id = #{userId} and habit_id = #{habitId}")
    void deleteHabitRecord(Integer habitId, Integer userId);

    /**
     * 修改tb_habits:修改指定习惯
     *
     * @param habit 修改后的信息封装
     */
    void updateHabit(Habit habit);

    /**
     * 查询tb_habits:获取习惯的时间限制区间
     * @param habitId 习惯id
     * @return Time列表
     */
    @Select("select earliest_time,latest_time from tb_habits where habit_id = #{habitId}")
    HashMap<String, Time> getTimeSection(Integer habitId);


    /**
     * 查询tb_habit_records:查询指定时间段内，某用户某习惯的完成记录
     * @param begin 时间段起始日期
     * @param end 时间段结束日期
     * @param habitId 习惯id
     * @param userId 用户id
     * @return 日期列表
     */
    List<Date> getFinshedDatesByHabitId(@Param("begin") Date begin,@Param("end") Date end,@Param("habitId") Integer habitId,@Param("userId") Integer userId);


    /**
     * 查询tb_habit_records:查询指定时间段内，用户完成的所有习惯数量
     * @param begin 开始时间
     * @param end 结束时间
     * @param userId 用户id
     * @return 完成的习惯总数
     */
    @Select("select count(*) from tb_habit_records where user_id = #{userId} and date between #{begin} and #{end}")
    Integer getCountOfFinishedHabitsInWeek(Date begin, Date end, Integer userId);

    /**
     * 查询tb_date_records:查询指定时间段内，用户每天习惯数量之和
     * @param begin 开始时间
     * @param end 结束时间
     * @param userId 用户id
     * @return 习惯总数
     */
    @Select("select sum(habits_count) from tb_date_records where date between #{begin} and #{end} and user_id = #{userId}")
    Integer getCountOfHabitsInWeek(Date begin, Date end, Integer userId);

    /**
     * 查询tb_date_records:查询指定时间段内，完成所有习惯的天数
     * @param begin 开始时间
     * @param end 结束时间
     * @param userId 用户id
     * @return 完美天数
     */
    @Select("select sum(finished_all) from tb_date_records where date between #{begin} and #{end} and user_id = #{userId}")
    Integer getCountsOfPerfectDays(Date begin, Date end, Integer userId);

//    @Select("select finished_all from tb_date_records where date between #{begin} and #{end} and user_id = #{userId}")
    List<Integer> getFinishedRecordsInWeek(@Param("begin") Date begin,@Param("end") Date end, @Param("userId") Integer userId);
}
