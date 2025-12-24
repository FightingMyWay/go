package com.office.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("supply_item")
public class SupplyItem {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String itemName;
    
    private Long categoryId;
    
    private String specification;
    
    private String unit;
    
    private BigDecimal price;
    
    private Integer stockQuantity;
    
    private Integer minStock;
    
    private String description;
    
    private String image;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
