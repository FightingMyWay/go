package com.office.controller;

import com.office.common.Result;
import com.office.entity.SupplyItem;
import com.office.entity.SupplyCategory;
import com.office.service.SupplyItemService;
import com.office.service.SupplyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply")
@CrossOrigin
public class SupplyItemController {
    
    @Autowired
    private SupplyItemService supplyItemService;
    
    @Autowired
    private SupplyCategoryService supplyCategoryService;
    
    @GetMapping("/page")
    public Result<?> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId) {
        return Result.success(supplyItemService.getPage(page, size, name, categoryId));
    }
    
    @GetMapping("/{id}")
    public Result<SupplyItem> getById(@PathVariable Long id) {
        return Result.success(supplyItemService.getById(id));
    }
    
    @GetMapping("/category/{categoryId}")
    public Result<List<SupplyItem>> getByCategory(@PathVariable Long categoryId) {
        return Result.success(supplyItemService.getByCategory(categoryId));
    }
    
    @GetMapping("/categories")
    public Result<List<SupplyCategory>> getCategories() {
        return Result.success(supplyCategoryService.getAllCategories());
    }
    
    @GetMapping("/list")
    public Result<List<SupplyItem>> getSupplyItems() {
        return Result.success(supplyItemService.list());
    }
    
    @PostMapping
    public Result<?> save(@RequestBody SupplyItem item) {
        if (supplyItemService.save(item)) {
            return Result.success();
        }
        return Result.error("保存失败");
    }
    
    @PutMapping
    public Result<?> update(@RequestBody SupplyItem item) {
        if (supplyItemService.updateById(item)) {
            return Result.success();
        }
        return Result.error("更新失败");
    }
    
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (supplyItemService.removeById(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
    
    @PutMapping("/stock/{id}")
    public Result<?> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        if (supplyItemService.updateStock(id, quantity)) {
            return Result.success();
        }
        return Result.error("库存更新失败");
    }
}
