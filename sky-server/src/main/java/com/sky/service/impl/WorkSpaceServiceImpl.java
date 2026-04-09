package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.SetmealService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;





/**
 * 获取营业数据
 * @return
 */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        HashMap hashMap = new HashMap();
        hashMap.put("begin",begin);
        hashMap.put("end",end);
        //订单总量
        Integer totalCount = orderMapper.countByMap(hashMap);

        hashMap.put("status", Orders.COMPLETED);
        //营业额
        Double turnover = orderMapper.sumByDate(hashMap);
        turnover = turnover ==null ? 0.0 : turnover;
        //有效订单数
        Integer validOrderCount = orderMapper.countByMap(hashMap);

        Double unitPrice = 0.0;

        Double orderCompletionRate = 0.0;
        if(validOrderCount != 0 && totalCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalCount;
            unitPrice = turnover / validOrderCount;
        }
        Integer newUsers = userMapper.countByMap(hashMap);
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }
/**
 * 获取起售/停售套餐数量
 * @return
 */
    @Override
    public SetmealOverViewVO getoverviewSetmeals() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);

        Integer sold = setmealMapper.countByMap(map);
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
/**
 * 获取起售/停售菜品数量
 * @return
 */
    @Override
    public DishOverViewVO getoverviewDishes() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();

    }
/**
 * 获取订单管理数据
 * @return
 */
    @Override
    public OrderOverViewVO getoverviewOrders() {
        Map map = new HashMap();
        map.put("begin",LocalDateTime.now().with(LocalTime.MIN));
        map.put("status",Orders.TO_BE_CONFIRMED);

        //待接单
        Integer WaitingOrders = orderMapper.countByMap(map);
        //带派送
        map.put("status",Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);
        //已完成
        map.put("status",Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);
        //已取消
        map.put("status",Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);
        //全部订单
        map.put("status",null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(WaitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();

    }
}
