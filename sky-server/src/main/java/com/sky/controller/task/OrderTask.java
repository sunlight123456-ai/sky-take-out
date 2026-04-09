package com.sky.controller.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
/**
 * 处理支付超时订单
 */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("处理支付超时订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.processTimeoutOrderLt(Orders.TO_BE_CONFIRMED, time);
        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("支付超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("处理派送订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.processTimeoutOrderLt(Orders.DELIVERY_IN_PROGRESS, time);
        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }

}
