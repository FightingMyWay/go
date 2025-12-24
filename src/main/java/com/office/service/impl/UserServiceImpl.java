package com.office.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office.entity.SysUser;
import com.office.mapper.UserMapper;
import com.office.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    // 暂时直接创建实例，避免依赖注入问题
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public SysUser findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    @Override
    public SysUser register(SysUser user) {
        user.setPassword(encodePassword(user.getPassword()));
        if (StrUtil.isBlankIfStr(user.getRole())) {
            user.setRole("USER");
        }
        user.setStatus(1);
        userMapper.insert(user);
        return user;
    }
    
    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    @Override
    public IPage<SysUser> getPage(Integer page, Integer size, String username, String realName, Long deptId, String role, Integer status) {
        Page<SysUser> pageParam = new Page<>(page, size);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            queryWrapper.like("username", username);
        }
        
        if (StringUtils.hasText(realName)) {
            queryWrapper.like("real_name", realName);
        }
        
        if (deptId != null) {
            queryWrapper.eq("dept_id", deptId);
        }
        
        if (StringUtils.hasText(role)) {
            queryWrapper.eq("role", role);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        return this.page(pageParam, queryWrapper);
    }
    
    @Override
    public boolean createUser(SysUser user) {
        // 检查用户名是否已存在
        if (findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 加密密码
        user.setPassword(encodePassword(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        return this.save(user);
    }
    
    @Override
    public boolean updateUser(SysUser user) {
        // 检查用户名是否已被其他用户使用
        SysUser existingUser = findByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new RuntimeException("用户名已存在");
        }
        
        user.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(user);
    }
    
    @Override
    public boolean resetPassword(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 重置为默认密码123456
        user.setPassword(encodePassword("123456"));
        user.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(user);
    }
    
    @Override
    public boolean toggleStatus(Long id, Integer status) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 防止停用admin账号
        if ("admin".equals(user.getUsername()) && status == 0) {
            throw new RuntimeException("不能停用系统管理员账号");
        }
        
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(user);
    }
    
    @Override
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证当前密码
        if (!checkPassword(currentPassword, user.getPassword())) {
            throw new RuntimeException("当前密码错误");
        }
        
        // 更新密码
        user.setPassword(encodePassword(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(user);
    }
}
