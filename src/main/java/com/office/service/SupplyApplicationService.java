package com.office.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.office.entity.SupplyApplication;

public interface SupplyApplicationService extends IService<SupplyApplication> {
    
    /**
     * 分页查询申请列表
     */
    IPage<SupplyApplication> getPage(Integer page, Integer size, String status, Long userId);
    
    /**
     * 创建申请
     */
    boolean createApplication(SupplyApplication application);
    
    /**
     * 审批申请
     */
    boolean approveApplication(Long id, String status, String remark, Long approverId);
    
    /**
     * 获取用户的申请列表
     */
    IPage<SupplyApplication> getUserApplications(Long userId, Integer page, Integer size);
    
    /**
     * 获取当前用户的申请列表（支持状态和关键词筛选）
     */
    IPage<SupplyApplication> getMyApplications(Long userId, Integer page, Integer size, String status, String keyword);
}
