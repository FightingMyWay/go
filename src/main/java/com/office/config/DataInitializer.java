package com.office.config;

import com.office.entity.SysUser;
import com.office.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== 开始执行数据初始化 ===");
        
        try {
            // 检查是否已存在管理员账号
            System.out.println("检查admin用户是否存在...");
            SysUser adminUser = userService.findByUsername("admin");
            
            if (adminUser == null) {
                System.out.println("admin用户不存在，开始创建...");
                
                // 创建默认管理员账号
                SysUser admin = new SysUser();
                admin.setUsername("admin");
                admin.setPassword("123456"); // 密码会在service层自动加密
                admin.setRealName("系统管理员");
                admin.setRole("ADMIN");
                admin.setStatus(1);
                admin.setDeptId(1L);
                
                userService.register(admin);
                System.out.println("✅ 默认管理员账号创建成功: admin/123456");
                
                // 验证创建是否成功
                SysUser createdUser = userService.findByUsername("admin");
                if (createdUser != null) {
                    System.out.println("✅ 验证成功，用户ID: " + createdUser.getId());
                    System.out.println("✅ 用户角色: " + createdUser.getRole());
                    System.out.println("✅ 用户状态: " + createdUser.getStatus());
                } else {
                    System.err.println("❌ 验证失败，用户创建后无法查询到");
                }
            } else {
                System.out.println("✅ admin用户已存在: " + adminUser.getUsername());
                System.out.println("✅ 用户ID: " + adminUser.getId());
                System.out.println("✅ 用户角色: " + adminUser.getRole());
                System.out.println("✅ 用户状态: " + adminUser.getStatus());
                
                // 测试密码验证
                boolean passwordValid = userService.checkPassword("123456", adminUser.getPassword());
                System.out.println("✅ 密码验证测试: " + (passwordValid ? "通过" : "失败"));
            }
        } catch (Exception e) {
            System.err.println("❌ 创建管理员账号失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== 数据初始化完成 ===");
    }
}
