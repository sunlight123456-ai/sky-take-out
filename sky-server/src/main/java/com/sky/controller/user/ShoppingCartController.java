package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCutService shoppingCutService;

    @PostMapping("/add")
    public Result add(@RequestBody  ShoppingCartDTO shoppingCartDTO){
        shoppingCutService.add(shoppingCartDTO);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> shoppingCart = shoppingCutService.list();
        return Result.success(shoppingCart);
    }
    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCutService.clean();
        return Result.success();
    }
}
