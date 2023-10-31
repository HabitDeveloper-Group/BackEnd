package com.zsh.mapper;

import com.zsh.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper  {
    /**
     * 根据username查询用户
     * @param username 用户名
     * @return User对象
     */
    @Select("select * from tb_users where user_name = #{username}")
    User selectByUserName(String username);

    /**
     * 查询所有用户
     * @return User列表
     */
    @Select("select * from tb_users ")
    List<User> selectList();

    /**
     * 向tb_users表中插入一个新用户
     * @param user 封装了用户信息
     */
    @Insert("insert into tb_users(user_name, name, password) VALUES (#{userName}, #{name}, #{password})")
    void insert(User user);
}
