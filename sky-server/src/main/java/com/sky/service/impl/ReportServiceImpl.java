package com.sky.service.impl;

import com.alibaba.druid.sql.visitor.functions.Locate;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 营业额数据统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //封装时间段dateList
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> turnoverList = new ArrayList<>();
        //封装营业额turnoverList
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByDate(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }
/**
     * 用户数据统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("end", endTime);
            Long totalUser = userMapper.getUserStatistics(map);
            map.put("begin", beginTime);
            Long newUser = userMapper.getUserStatistics(map);
            totalUserList.add(totalUser.intValue());
            newUserList.add(newUser.intValue());
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }
/**
     * 订单数据统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime =LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            orderCountList.add(getOrderCount(beginTime, endTime, null));
            validOrderCountList.add(getOrderCount(beginTime, endTime, Orders.COMPLETED));
        }
        Integer orderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = 0.0;
        if (orderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / orderCount;
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(orderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }
/**
     * 销量排名
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime begintime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endtime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10ReportVOList = orderMapper.getSalesTop10(begintime, endtime);
        List<String> nameList = salesTop10ReportVOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = salesTop10ReportVOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    /**
     * 导出数据
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {

        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = workSpaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            XSSFWorkbook excels = new XSSFWorkbook(in);

            XSSFSheet sheet = excels.getSheet("Sheet1");
            sheet.getRow(1).getCell(1).setCellValue("时间："+dateBegin+"至"+dateEnd);

            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            for(int i =0;i<30;i++){
                LocalDate date = dateBegin.plusDays(i);
                BusinessDataVO businessDataVO = workSpaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7+i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessDataVO.getTurnover());
                row.getCell(3).setCellValue(businessDataVO.getValidOrderCount());
                row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessDataVO.getUnitPrice());
                row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            }
            ServletOutputStream out = response.getOutputStream();
            excels.write(out);
            out.close();
            excels.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status){
        Map map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }

}
