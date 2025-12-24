package com.office.controller;

import com.office.common.Result;
import com.office.entity.SysUser;
import com.office.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserProfileController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户个人资料
     */
    @GetMapping("/profile")
    public Result<SysUser> getCurrentUserProfile() {
        // TODO: 从JWT token中获取当前用户ID
        // 这里暂时返回模拟数据，实际应该从token中解析用户ID
        Long currentUserId = 1L; // 临时硬编码，需要从JWT token中获取
        
        SysUser user = userService.getById(currentUserId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 隐藏敏感信息
        user.setPassword(null);
        
        return Result.success(user);
    }

    /**
     * 更新个人资料
     */
    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody SysUser user) {
        // TODO: 从JWT token中获取当前用户ID
        Long currentUserId = 1L; // 临时硬编码，需要从JWT token中获取
        
        // 确保只能更新自己的资料
        user.setId(currentUserId);
        
        if (userService.updateUser(user)) {
            return Result.success();
        }
        return Result.error("更新个人资料失败");
    }

    /**
     * 修改密码
     */
    @PutMapping("/change-password")
    public Result<?> changePassword(@RequestBody Map<String, String> passwordData) {
        String currentPassword = passwordData.get("currentPassword");
        String newPassword = passwordData.get("newPassword");
        
        if (currentPassword == null || newPassword == null) {
            return Result.error("密码参数不完整");
        }
        
        // TODO: 从JWT token中获取当前用户ID
        Long currentUserId = 1L; // 临时硬编码，需要从JWT token中获取
        
        if (userService.changePassword(currentUserId, currentPassword, newPassword)) {
            return Result.success();
        }
        return Result.error("密码修改失败");
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    public Result<?> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只能上传图片文件");
        }
        
        // 检查文件大小（2MB）
        if (file.getSize() > 2 * 1024 * 1024) {
            return Result.error("文件大小不能超过2MB");
        }
        
        try {
            // TODO: 实现文件上传逻辑
            // 这里暂时返回模拟的URL，实际应该上传到文件服务器或本地存储
            
            // 模拟文件上传成功
            String avatarUrl = "/uploads/avatars/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            
            // TODO: 从JWT token中获取当前用户ID
            Long currentUserId = 1L; // 临时硬编码，需要从JWT token中获取
            
            // 更新用户头像
            SysUser user = new SysUser();
            user.setId(currentUserId);
            user.setAvatar(avatarUrl);
            
            if (userService.updateById(user)) {
                Map<String, String> result = new HashMap<>();
                result.put("url", avatarUrl);
                return Result.success(result);
            } else {
                return Result.error("头像更新失败");
            }
            
        } catch (Exception e) {
            return Result.error("头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取个人资料统计
     */
    @GetMapping("/profile/stats")
    public Result<?> getProfileStats() {
        // TODO: 从JWT token中获取当前用户ID
        Long currentUserId = 1L; // 临时硬编码，需要从JWT token中获取
        
        try {
            // 模拟统计数据，实际应该从数据库查询
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalApplications", 15);
            stats.put("pendingApplications", 3);
            stats.put("approvedApplications", 10);
            stats.put("rejectedApplications", 2);
            
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取统计信息失败");
        }
    }

    /**
     * 获取最近活动
     */
    @GetMapping("/profile/activities")
    public Result<?> getRecentActivities() {
        // TODO: 从JWT token中获取当前用户ID
        Long currentUserId = 1L; // 临时硬编码，需要从JWT token中获取
        
        try {
            // 模拟活动数据，实际应该从数据库查询
            // 这里返回空数组，实际应该查询用户的活动记录
            return Result.success(new Object[0]);
        } catch (Exception e) {
            return Result.error("获取活动记录失败");
        }
    }
}
