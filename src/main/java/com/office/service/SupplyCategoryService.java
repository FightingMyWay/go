package com.office.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.office.entity.SupplyCategory;

import java.util.List;

public interface SupplyCategoryService extends IService<SupplyCategory> {
    
    /**
     * 获取所有分类
     */
    List<SupplyCategory> getAllCategories();
}
