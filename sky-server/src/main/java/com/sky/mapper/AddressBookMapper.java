package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    /**
     * 查看地址
     * @param addressBook
     */
    List<AddressBook> list (AddressBook addressBook);

    /**
     * 添加地址
     * @param addressBook
     */
    void add(AddressBook addressBook);

   /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);
    /**
     * 删除地址
     * @param id
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
/**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 均恢复默认地址
     * @param addressBook
     */
    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updatedeIsfaultById(AddressBook addressBook);
}
