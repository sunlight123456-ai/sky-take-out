package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


public interface WorkSpaceService {

/**
 * 查询今日运营数据
 * @return
 */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    SetmealOverViewVO getoverviewSetmeals();

    DishOverViewVO getoverviewDishes();

    OrderOverViewVO getoverviewOrders();
}
