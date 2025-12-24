package com.office.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.office.entity.SupplyItem;
import com.office.entity.SupplyCategory;

import java.util.List;

public interface SupplyItemService extends IService<SupplyItem> {
    
    /**
     * 分页查询用品列表
     */
    IPage<SupplyItem> getPage(Integer page, Integer size, String name, Long categoryId);
    
    /**
     * 根据分类查询用品列表
     */
    List<SupplyItem> getByCategory(Long categoryId);
    
    /**
     * 更新库存
     */
    boolean updateStock(Long id, Integer quantity);
    
    /**
     * 检查库存是否充足
     */
    boolean checkStock(Long id, Integer quantity);
}
