package com.zsh.mapper;

import com.zsh.pojo.Habit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HabitMapper {

    @Select("select habit_id, habit_name, icon, tag, description, earliest_time, latest_time, lower_limit, added_time, has_done_times " +
            "from tb_habits where user_id = #{userId} and has_done_times < lower_limit")
    List<Habit> selectNotFinished(Integer userId);
}
