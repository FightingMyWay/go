package com.office.controller;

import com.office.common.JwtUtil;
import com.office.common.Result;
import com.office.entity.SysUser;
import com.office.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        String username = loginForm.get("username");
        String password = loginForm.get("password");
        
        if (username == null || password == null) {
            return Result.error("用户名和密码不能为空");
        }
        
        SysUser user = userService.findByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (!userService.checkPassword(password, user.getPassword())) {
            return Result.error("密码错误");
        }
        
        if (user.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }
        
        String token = jwtUtil.generateToken(username);
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        
        return Result.success(result);
    }
    
    @PostMapping("/register")
    public Result<SysUser> register(@RequestBody SysUser user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        
        SysUser newUser = userService.register(user);
        return Result.success(newUser);
    }
    
    @GetMapping("/userinfo")
    public Result<SysUser> getUserInfo(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);
            String username = jwtUtil.getUsernameFromToken(actualToken);
            if (username != null) {
                SysUser user = userService.findByUsername(username);
                if (user != null) {
                    // 清除敏感信息
                    user.setPassword(null);
                    return Result.success(user);
                }
            }
        }
        return Result.error("获取用户信息失败");
    }
}
