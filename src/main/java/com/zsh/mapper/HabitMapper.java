package com.zsh.mapper;

import com.zsh.pojo.Habit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface HabitMapper {

    /**
     * 查询待完成的习惯
     * @param userId
     * @return
     */
    @Select("select habit_id, habit_name, icon, tag, description, earliest_time, latest_time, lower_limit, added_time, has_done_times " +
            "from tb_habits where user_id = #{userId} and has_done_times < lower_limit")
    List<Habit> selectNotFinished(Integer userId);

    /**
     * 查询已完成的习惯
     * @param userId
     * @return
     */
    @Select("select habit_id, habit_name, icon, tag, description, earliest_time, latest_time, lower_limit, added_time, has_done_times " +
            "from tb_habits where user_id = #{userId} and has_done_times >= lower_limit")
    List<Habit> selectHasFinished(Integer userId);


    /**
     * 查询已失败(来不及)的习惯
     * @param userId
     * @return
     */
    @Select("select habit_id, habit_name, icon, tag, description, earliest_time, latest_time, lower_limit, added_time, has_done_times " +
            "from tb_habits " +
            "where user_id = #{userId} " +
            "and has_done_times < tb_habits.lower_limit " +
            "and curtime() > latest_time")
    List<Habit> selectHasFailed(Integer userId);

    /**
     * 将指定习惯id的已完成次数+1，实现打卡操作
     * @param habitId
     */
    @Update("update tb_habits set has_done_times = has_done_times + 1 where habit_id = #{habitId}")
    void updateHasDoneTimesById(Integer habitId);

    /**
     * 根据习惯id查询下限
     * @param habitId
     * @return
     */
    @Select("select lower_limit from tb_habits where habit_id = #{habitId}")
    Integer getLowerLimitById(Integer habitId);

    /**
     * 根据习惯id查询已打卡次数
     * @param habitId
     * @return
     */
    @Select("select has_done_times from tb_habits where habit_id = #{habitId}")
    Integer getHasDoneTimes(Integer habitId);

    /**
     * 更新tb_hatit_records,记录用户当天完成某习惯
     * @param userId
     * @param habitId
     */
    @Insert("insert into tb_habit_records(user_id, date, habit_id) VALUES (#{userId},curdate(),#{habitId})")
    void updateHabitRecord(Integer userId, Integer habitId);

//    @Insert("insert into tb_date_records(user_id, date, finished_all) VALUES (#{userId},curdate(),1)")

    /**
     * 更新tb_date_records,记录用户当天已完成所有习惯
     * @param userId
     */
    @Update("update tb_date_records set finished_all = 1 where user_id = #{userId} and date = curdate()")
    void updateDateRecord(Integer userId);

    /**
     * 根据用户查询当天未完成的习惯数量
     * @param userId
     * @return
     */
    @Select("select count(*) from tb_habits " +
            "where user_id = #{userId} and has_done_times < tb_habits.lower_limit")
    Integer getNotFinishedNumByUserId(Integer userId);
}
