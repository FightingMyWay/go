package com.office.controller;

import com.office.common.Result;
import com.office.common.JwtUtil;
import com.office.entity.SupplyApplication;
import com.office.service.SupplyApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/application")
@CrossOrigin
public class SupplyApplicationController {
    
    @Autowired
    private SupplyApplicationService applicationService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/page")
    public Result<?> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId) {
        return Result.success(applicationService.getPage(page, size, status, userId));
    }
    
    @GetMapping("/my")
    public Result<?> getMyApplications(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestHeader("Authorization") String token) {
        try {
            // 从Authorization header中提取token
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 验证token并获取用户名
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                // 这里应该根据用户名查询用户ID，暂时使用固定值1
                // TODO: 实现根据用户名查询用户ID的逻辑
                Long currentUserId = 1L;
                
                return Result.success(applicationService.getMyApplications(currentUserId, page, size, status, keyword));
            } else {
                return Result.error("Token无效");
            }
        } catch (Exception e) {
            return Result.error("获取申请列表失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public Result<SupplyApplication> getById(@PathVariable Long id) {
        return Result.success(applicationService.getById(id));
    }
    
    @PostMapping
    public Result<?> create(@RequestBody SupplyApplication application) {
        if (applicationService.createApplication(application)) {
            return Result.success();
        }
        return Result.error("创建申请失败");
    }
    
    @PutMapping("/approve/{id}")
    public Result<?> approve(
            @PathVariable Long id,
            @RequestBody Map<String, Object> params) {
        String status = (String) params.get("status");
        String remark = (String) params.get("remark");
        Long approverId = Long.valueOf(params.get("approverId").toString());
        
        if (applicationService.approveApplication(id, status, remark, approverId)) {
            return Result.success();
        }
        return Result.error("审批失败");
    }
    
    @GetMapping("/user/{userId}")
    public Result<?> getUserApplications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(applicationService.getUserApplications(userId, page, size));
    }
}
