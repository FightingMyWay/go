package com.office.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("supply_application")
public class SupplyApplication {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String applicationNo;
    
    private Long userId;
    
    private Long deptId;
    
    private String title;
    
    private String reason;
    
    private String status;
    
    private Long approverId;
    
    private LocalDateTime approveTime;
    
    private String approveRemark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
