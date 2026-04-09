package com.sky.mapper;


import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Mapper

public interface UserMapper {

    @Select("select * from user where openid = #{openId}")
    User getByOpenId(String openId);

    void insert(User user);
   /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
    /**
     * 根据用户id修改用户信息
     * @param map
     */
    Long getUserStatistics(Map map);

    /**
     *规定日期内统计用户数量
     * @param hashMap
     * @return
     */
    Integer countByMap(HashMap hashMap);
}
