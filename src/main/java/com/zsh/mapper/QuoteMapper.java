package com.zsh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface QuoteMapper {
    @Select("select value,author from tb_quotes ")
    public List<HashMap<String,String>> list() ;
}
