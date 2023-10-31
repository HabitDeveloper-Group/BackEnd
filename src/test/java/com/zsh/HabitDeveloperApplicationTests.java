package com.zsh;

import com.zsh.mapper.UserMapper;
import com.zsh.pojo.User;
import com.zsh.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class HabitDeveloperApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private UserMapper userMapper;
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

}
