package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api("C端-地址薄")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @RequestMapping("/list")
    public Result<List<AddressBook>> list(){
        AddressBook addressBook = AddressBook.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result add( @RequestBody AddressBook addressBook){
        addressBookService.add(addressBook);
        return Result.success();
    }
     /**
     * 获取默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        AddressBook addressBook = AddressBook.builder()
                .userId(BaseContext.getCurrentId())
                .isDefault(1)
                .build();
        List<AddressBook> list = addressBookService.list(addressBook);
        if(list != null || list.size() != 0){
            return Result.success(list.get(0));
        }
        return Result.error("没有默认地址");
    }
    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return Result.success();
    }
    /**
     * 删除地址
     */
    @DeleteMapping
    public Result delete(Long id){
        addressBookService.delete(id);
        return Result.success();
    }
    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}
