package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台接口")
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;

    @GetMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData(){
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO = workSpaceService.getBusinessData(begin,end);
        return Result.success(businessDataVO);
    }
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> getoverviewSetmeals(){
        SetmealOverViewVO setmealOverViewVO = workSpaceService.getoverviewSetmeals();
        return Result.success(setmealOverViewVO);
    }
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> getoverviewDishes(){
        DishOverViewVO dishOverViewVO = workSpaceService.getoverviewDishes();
        return Result.success(dishOverViewVO);
    }
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getoverviewOrders(){
        OrderOverViewVO orderOverViewVO = workSpaceService.getoverviewOrders();
        return Result.success(orderOverViewVO);
    }
}
