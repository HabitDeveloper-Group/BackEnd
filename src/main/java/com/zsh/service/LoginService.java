package com.zsh.service;

import com.zsh.pojo.Result;
import com.zsh.pojo.User;

public interface LoginService {
    /**
     * 登录
     * @param user 封装的用户对象
     * @return 成功返回jwt令牌
     */
    Result login(User user);

    /**
     * 注销登录
     * @return
     */
    Result logout();

    /**
     * 根据username查询数据库中是否已经存在重名
     * @param username
     * @return
     */
    Result userNameCheckExisted(String username);


    /**
     * 根据user中封装的用户信息注册一个用户
     * @param user 封装了用户的信息
     * @return
     */
    Result register(User user);
}
