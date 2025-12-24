package com.office.controller;

import com.office.common.Result;
import com.office.entity.SysUser;
import com.office.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/page")
    public Result<?> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer status) {
        return Result.success(userService.getPage(page, size, username, realName, deptId, role, status));
    }
    
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }
    
    @PostMapping
    public Result<?> create(@RequestBody SysUser user) {
        if (userService.createUser(user)) {
            return Result.success();
        }
        return Result.error("创建用户失败");
    }
    
    @PutMapping
    public Result<?> update(@RequestBody SysUser user) {
        if (userService.updateUser(user)) {
            return Result.success();
        }
        return Result.error("更新用户失败");
    }
    
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (userService.removeById(id)) {
            return Result.success();
        }
        return Result.error("删除用户失败");
    }
    
    @PutMapping("/{id}/reset-password")
    public Result<?> resetPassword(@PathVariable Long id) {
        if (userService.resetPassword(id)) {
            return Result.success();
        }
        return Result.error("重置密码失败");
    }
    
    @PutMapping("/{id}/status")
    public Result<?> toggleStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> params) {
        Integer status = (Integer) params.get("status");
        if (userService.toggleStatus(id, status)) {
            return Result.success();
        }
        return Result.error("状态更新失败");
    }
}
