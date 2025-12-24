package com.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office.entity.SupplyCategory;
import com.office.mapper.SupplyCategoryMapper;
import com.office.service.SupplyCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyCategoryServiceImpl extends ServiceImpl<SupplyCategoryMapper, SupplyCategory> implements SupplyCategoryService {
    
    @Override
    public List<SupplyCategory> getAllCategories() {
        QueryWrapper<SupplyCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1)
                   .orderByAsc("sort")
                   .orderByDesc("create_time");
        
        return this.list(queryWrapper);
    }
}
