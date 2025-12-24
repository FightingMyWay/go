package com.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office.entity.SupplyItem;
import com.office.mapper.SupplyItemMapper;
import com.office.service.SupplyItemService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SupplyItemServiceImpl extends ServiceImpl<SupplyItemMapper, SupplyItem> implements SupplyItemService {
    
    @Override
    public IPage<SupplyItem> getPage(Integer page, Integer size, String name, Long categoryId) {
        Page<SupplyItem> pageParam = new Page<>(page, size);
        QueryWrapper<SupplyItem> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            queryWrapper.like("item_name", name);
        }
        
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        return this.page(pageParam, queryWrapper);
    }
    
    @Override
    public List<SupplyItem> getByCategory(Long categoryId) {
        QueryWrapper<SupplyItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId)
                   .eq("status", 1)
                   .orderByAsc("sort")
                   .orderByDesc("create_time");
        
        return this.list(queryWrapper);
    }
    
    @Override
    public boolean updateStock(Long id, Integer quantity) {
        SupplyItem item = this.getById(id);
        if (item != null) {
            item.setStockQuantity(item.getStockQuantity() + quantity);
            return this.updateById(item);
        }
        return false;
    }
    
    @Override
    public boolean checkStock(Long id, Integer quantity) {
        SupplyItem item = this.getById(id);
        return item != null && item.getStockQuantity() >= quantity;
    }
}
