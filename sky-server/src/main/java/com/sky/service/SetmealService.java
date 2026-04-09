package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SetmealService {
    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);
}
