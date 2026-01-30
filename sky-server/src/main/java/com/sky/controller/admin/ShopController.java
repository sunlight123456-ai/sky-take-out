package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    private static final String Status = "currrentstatus";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 修改店铺状态接口
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("修改店铺状态接口")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺的营业状态为：{}",status == 1 ? "设置营业中":"设置打烊");
        redisTemplate.opsForValue().set(Status,status);
        return Result.success();
    }

    /**
     * 查看店铺状态接口
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查看店铺状态接口")
    public Result<Integer> getStatus(){
        Integer status = (Integer)redisTemplate.opsForValue().get(Status);
        return Result.success(status);
    }

}
