package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;
    /**
     * 查看地址薄
     */
    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        List<AddressBook> list =  addressBookMapper.list(addressBook);
        return list;
    }

    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBook.setIsDefault(0);
        addressBookMapper.add(addressBook);
    }
/**
     * 根据id修改地址
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.update(addressBook);
    }
/**
     * 删除地址
     * @param id
     */
    @Override
    public void delete(Long id) {
        addressBookMapper.deleteById(id);
    }

    @Override
    public AddressBook getById(Long id) {
        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }
/**
     * 修改默认地址
     * @param addressBook
     */
    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updatedeIsfaultById(addressBook);

        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }


}
