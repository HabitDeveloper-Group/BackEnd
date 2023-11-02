package com.zsh;

import com.zsh.mapper.HabitMapper;
import com.zsh.mapper.UserMapper;
import com.zsh.pojo.User;
import com.zsh.utils.MyUtils;
import com.zsh.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
class HabitDeveloperApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HabitMapper habitMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList();
        System.out.println(users);
    }

    @Test
    public void testPasswordEncoder(){
        String password = "1234";
        System.out.println(bCryptPasswordEncoder.encode(password));
    }

    @Test
    public void testJWT(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("username","zsh");
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InpzaCIsImV4cCI6MTY5ODc4MTM4Nn0.Iokn8y4sQ4UvD4Rw6xgijGiIQFZrRA36QicUfDyCWpQ";
        System.out.println(jwt);
        Claims claims1 = JWTUtils.parseJWT(jwt);
        System.out.println(claims1);

    }

    @Test
    public  void testGetFinishedDates(){
        Integer habitId = 1;
        Integer userId = 1;
        Date begin = Date.valueOf("2023-11-01");
        Date end = Date.valueOf("2023-10-25");
        List<Date> dates = habitMapper.getFinshedDatesByHabitId(begin, end, habitId, userId);
        System.out.println(dates);
    }

    @Test
    public void testDateCompareTo(){
        Date today = Date.valueOf(LocalDate.now());
        Date another = Date.valueOf("2023-11-02");
        java.util.Date beginDate= today;

        java.util.Date endDate= another;

        long day=(beginDate.getTime()-endDate.getTime())/(24*60*60*1000);
        System.out.println(day);
    }

    @Test
    public void testDateUtils(){
        Date end = Date.valueOf("2023-11-01");
        Date begin = Date.valueOf("2023-10-25");
        System.out.println(MyUtils.getDiffDays(end, begin));
    }

    @Test
    public void testUtils(){
        List<Integer> list = Arrays.asList(1,1,1,1,1,1,0);
        System.out.println(MyUtils.getMaxConsecutiveOnes(list));
    }

    @Test
    public void testGetFinishedRecords(){
        Date end = Date.valueOf("2023-11-01");
        Date begin = Date.valueOf("2023-10-26");
        List<Integer> records = habitMapper.getFinishedRecordsInWeek(begin, end, 1);
        System.out.println(records);
        System.out.println(MyUtils.getMaxConsecutiveOnes(records));
    }
}
