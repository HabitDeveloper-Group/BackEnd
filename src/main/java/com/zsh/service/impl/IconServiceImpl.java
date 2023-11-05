package com.zsh.service.impl;

import com.zsh.mapper.IconMapper;
import com.zsh.pojo.Result;
import com.zsh.service.IconService;
import com.zsh.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class IconServiceImpl implements IconService {
    @Autowired
    private IconMapper iconMapper;

    @Override
    public Result listIcons() {
        //首先查询数据库，获得所有图标的url
        List<String> iconUrls = iconMapper.list();
        //将url封装成对象{"v":0,"icon":"http:...."},然后包装到arr列表中
        List<HashMap<String,Object>> arr = new ArrayList<>();
        iconUrls.stream().forEach(MyUtils.consumerWithIndex((url, index)->{
            HashMap<String, Object> item = new HashMap<>();
            item.put("v", index);
            item.put("content",url);
            arr.add(item);
        }));
        //最后封装data数据，{"total":2,"arr":[....]}
        HashMap<String,Object> data = new HashMap<>();
        data.put("total", iconUrls.size());
        data.put("arr", arr);

        return Result.success(data);

    }
}
