package com.office.controller;

import com.office.common.Result;
import com.office.entity.Department;
import com.office.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dept")
@CrossOrigin
public class DeptController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping("/list")
    public Result<List<Department>> getList() {
        return Result.success(departmentService.list());
    }
    
    @GetMapping("/page")
    public Result<?> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {
        return Result.success(departmentService.getPage(page, size, name, code));
    }
    
    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        return Result.success(departmentService.getById(id));
    }
    
    @PostMapping
    public Result<?> create(@RequestBody Department department) {
        if (departmentService.save(department)) {
            return Result.success();
        }
        return Result.error("创建部门失败");
    }
    
    @PutMapping
    public Result<?> update(@RequestBody Department department) {
        if (departmentService.updateById(department)) {
            return Result.success();
        }
        return Result.error("更新部门失败");
    }
    
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (departmentService.removeById(id)) {
            return Result.success();
        }
        return Result.error("删除部门失败");
    }
}
