package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {
/**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from Setmeal where categoy_id = #{id}}")
    Integer countByCategoryId(Long id);
}
