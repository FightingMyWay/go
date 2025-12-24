package com.office.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.office.entity.Department;

public interface DepartmentService extends IService<Department> {
    
    /**
     * 分页查询部门列表
     */
    IPage<Department> getPage(Integer page, Integer size, String name, String code);
}
