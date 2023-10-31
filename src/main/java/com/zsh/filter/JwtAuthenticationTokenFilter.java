package com.zsh.filter;

import com.alibaba.fastjson.JSONObject;
import com.zsh.pojo.LoginUser;
import com.zsh.pojo.Result;
import com.zsh.utils.JWTUtils;
import com.zsh.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String username;
        try {
            Claims claims = JWTUtils.parseJWT(token);
            username = claims.get("username",String.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录的错误信息");
            Result error = Result.error("NOT_LOGIN");
            //手动将对象转换为json，运用阿里巴巴提供的快速转换工具类
            String notLogin = JSONObject.toJSONString(error);
            //将数据写入到response中
            response.getWriter().write(notLogin);
            return;
         /*   e.printStackTrace();
            throw new RuntimeException("token非法");*/
        }
        //从redis中获取用户信息
        String redisKey = "login:" + username;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(loginUser)){
//            throw new RuntimeException("用户未登录");
            log.info("用户未登录");
            Result error = Result.error("NOT_LOGIN");
            //手动将对象转换为json，运用阿里巴巴提供的快速转换工具类
            String notLogin = JSONObject.toJSONString(error);
            //将数据写入到response中
            response.getWriter().write(notLogin);
            return;
        }
        //TODO 获取权限信息封装到Authentication



        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
