package com.zsh.utils;

import java.sql.Date;
import java.util.List;

import static java.lang.Math.max;

public class MyUtils {
    public static Integer getDiffDays(Date end, Date begin){
        java.util.Date beginDate= begin;

        java.util.Date endDate= end;

        long day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        return Math.toIntExact(day);
    }

    /**
     * 给定一个序列返回最大的连续的1
     * @param list
     * @return
     */
    public static Integer getMaxConsecutiveOnes(List<Integer> list){
        //首先获得时间段内的每天的完成与否列表
        final Integer length = list.size();
        int[] f = new int[length];
        //[1,1,0,0,1,1,1]
        f[0] = list.get(0);

        Integer res = 0;
        for (int i = 1; i<length;i++) {
            if(list.get(i) == 1){
                f[i] = f[i-1] + 1;
                res = max(res, f[i]);
            }
        }
        return res;

    }
}
