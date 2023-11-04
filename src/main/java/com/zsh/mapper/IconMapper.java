package com.zsh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IconMapper {
    @Select("select url from tb_icons ")
    List<String> list();
}
