package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.SalesTop10ReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 修改订单状态
     * @param orderStatus
     * @param orderPaidStatus
     * @param checkOutTime
     * @param orderNumber
     */
    @Select("update orders set status = #{orderStatus}, pay_status = #{orderPaidStatus}, checkout_time = #{checkOutTime} where number = #{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime checkOutTime, String orderNumber);
    /**
     * 查询待派送的订单
     * @param toBeConfirmed
     * @param time
     * @return
     */
    @Select("select * from orders where status = #{toBeConfirmed} and order_time < #{time}")
    public List<Orders> processTimeoutOrderLt(Integer toBeConfirmed, LocalDateTime time);
/**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

/**
     * 根据日期统计营业额
     * @param map
     * @return
     */
    Double sumByDate(Map map);
/**
     * 根据状态统计数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
/**
     * 销量排名
     * @param begintime
     * @param endtime
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begintime, LocalDateTime endtime);


}
