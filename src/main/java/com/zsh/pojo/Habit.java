package com.zsh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Habit {
    private Integer habitId;
//    private Integer userId;
    private String habitName;
    private String icon;
    private String tag;
    private String description;
    private LocalTime earliestTime;
    private LocalTime latestTime;
    private Integer lowerLimit;
    private LocalDate addedTime;
    private Integer hasDoneTimes;
}
