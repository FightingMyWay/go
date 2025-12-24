package com.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office.entity.Department;
import com.office.mapper.DepartmentMapper;
import com.office.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    
    @Override
    public IPage<Department> getPage(Integer page, Integer size, String name, String code) {
        Page<Department> pageParam = new Page<>(page, size);
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            queryWrapper.like("name", name);
        }
        
        if (StringUtils.hasText(code)) {
            queryWrapper.like("code", code);
        }
        
        queryWrapper.orderByAsc("sort").orderByAsc("id");
        
        return this.page(pageParam, queryWrapper);
    }
}
