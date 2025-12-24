package com.office.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.office.entity.SysUser;

public interface UserService extends IService<SysUser> {
    
    SysUser findByUsername(String username);
    
    SysUser register(SysUser user);
    
    boolean checkPassword(String rawPassword, String encodedPassword);
    
    String encodePassword(String rawPassword);
    
    /**
     * 分页查询用户列表
     */
    IPage<SysUser> getPage(Integer page, Integer size, String username, String realName, Long deptId, String role, Integer status);
    
    /**
     * 创建用户
     */
    boolean createUser(SysUser user);
    
    /**
     * 更新用户
     */
    boolean updateUser(SysUser user);
    
    /**
     * 重置用户密码
     */
    boolean resetPassword(Long id);
    
    /**
     * 切换用户状态
     */
    boolean toggleStatus(Long id, Integer status);
    
    /**
     * 修改用户密码
     */
    boolean changePassword(Long userId, String currentPassword, String newPassword);
}
