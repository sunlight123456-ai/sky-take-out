package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
public class ShopController {
    private static final String Status = "currrentstatus";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查看店铺状态接口用户端
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查看店铺状态接口")
    public Result<Integer> getStatus(){
        Integer status = (Integer)redisTemplate.opsForValue().get(Status);
        return Result.success(status);
    }
}
