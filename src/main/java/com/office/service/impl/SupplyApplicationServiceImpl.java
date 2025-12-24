package com.office.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office.entity.SupplyApplication;
import com.office.mapper.SupplyApplicationMapper;
import com.office.service.SupplyApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class SupplyApplicationServiceImpl extends ServiceImpl<SupplyApplicationMapper, SupplyApplication> implements SupplyApplicationService {
    
    @Override
    public IPage<SupplyApplication> getPage(Integer page, Integer size, String status, Long userId) {
        Page<SupplyApplication> pageParam = new Page<>(page, size);
        QueryWrapper<SupplyApplication> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }
        
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        return this.page(pageParam, queryWrapper);
    }
    
    @Override
    public boolean createApplication(SupplyApplication application) {
        // 生成申请单号
        application.setApplicationNo(generateApplicationNo());
        application.setStatus("PENDING");
        application.setCreateTime(LocalDateTime.now());
        
        return this.save(application);
    }
    
    @Override
    public boolean approveApplication(Long id, String status, String remark, Long approverId) {
        SupplyApplication application = this.getById(id);
        if (application != null) {
            application.setStatus(status);
            application.setApproveRemark(remark);
            application.setApproverId(approverId);
            application.setApproveTime(LocalDateTime.now());
            application.setUpdateTime(LocalDateTime.now());
            
            return this.updateById(application);
        }
        return false;
    }
    
    @Override
    public IPage<SupplyApplication> getUserApplications(Long userId, Integer page, Integer size) {
        Page<SupplyApplication> pageParam = new Page<>(page, size);
        QueryWrapper<SupplyApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
        
        return this.page(pageParam, queryWrapper);
    }
    
    @Override
    public IPage<SupplyApplication> getMyApplications(Long userId, Integer page, Integer size, String status, String keyword) {
        Page<SupplyApplication> pageParam = new Page<>(page, size);
        QueryWrapper<SupplyApplication> queryWrapper = new QueryWrapper<>();
        
        // 设置用户ID条件
        queryWrapper.eq("user_id", userId);
        
        // 设置状态筛选
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status);
        }
        
        // 设置关键词搜索（标题和原因）
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                .like("title", keyword)
                .or()
                .like("reason", keyword)
            );
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        return this.page(pageParam, queryWrapper);
    }
    
    /**
     * 生成申请单号
     */
    private String generateApplicationNo() {
        return "AP" + System.currentTimeMillis();
    }
}
