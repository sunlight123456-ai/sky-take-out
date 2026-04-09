package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 查看地址簿
     */
    List<AddressBook> list(AddressBook addressBook);
    /**
     * 新增地址
     */
    void add(AddressBook addressBook);


    void update(AddressBook addressBook);

    void delete(Long id);

    AddressBook getById(Long id);

    void setDefault(AddressBook addressBook);
}
