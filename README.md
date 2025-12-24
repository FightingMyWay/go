# 办公用品申领系统

## 项目简介

这是一个基于Spring Boot + Vue3的办公用品申领管理系统，支持用户申请办公用品、管理员审批、库存管理等功能。

## 技术栈

### 后端
- Spring Boot 2.7.0
- MyBatis-Plus 3.5.2
- MySQL 8.0+
- Redis
- JWT认证
- Maven

### 前端
- Vue 3.3.4
- Vue Router 4.2.4
- Pinia 2.1.6
- Element Plus 2.3.8
- Vite 4.4.5
- SCSS

## 项目结构

```
code50810/
├── database/                 # 数据库脚本
│   └── init-corrected.sql
├── frontend/                 # 前端项目
│   ├── src/
│   │   ├── api/             # API接口
│   │   ├── components/      # 组件
│   │   ├── layouts/         # 布局组件
│   │   ├── router/          # 路由配置
│   │   ├── stores/          # 状态管理
│   │   ├── views/           # 页面组件
│   │   └── style/           # 样式文件
│   ├── package.json
│   └── vite.config.js
├── src/                      # 后端项目
│   └── main/
│       ├── java/
│       │   └── com/office/
│       │       ├── common/   # 公共类
│       │       ├── config/   # 配置类
│       │       ├── controller/ # 控制器
│       │       ├── entity/   # 实体类
│       │       ├── mapper/   # 数据访问层
│       │       └── service/  # 服务层
│       └── resources/
│           └── application.yml
└── pom.xml
```

## 快速开始

### 1. 环境要求

- JDK 11+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE office_supply DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
```sql
source database/init-corrected.sql
```

3. 修改后端配置文件 `src/main/resources/application.yml` 中的数据库连接信息

### 3. 启动后端

1. 进入项目根目录
2. 执行Maven命令：
```bash
mvn clean install
mvn spring-boot:run
```

后端将在 `http://localhost:8085` 启动

### 4. 启动前端

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run dev
```

前端将在 `http://localhost:3000` 启动

## 功能特性

### 用户功能
- 用户注册/登录
- 浏览办公用品
- 申请办公用品
- 查看申请状态
- 个人资料管理

### 管理员功能
- 用户管理
- 部门管理
- 用品分类管理
- 用品管理
- 申请审批
- 库存管理

## 默认账号

- 管理员：admin / 123456
- 普通用户需要注册

## API接口

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/userinfo` - 获取用户信息

### 用品管理
- `GET /api/supply/page` - 分页查询用品
- `GET /api/supply/{id}` - 获取用品详情
- `POST /api/supply` - 创建用品
- `PUT /api/supply` - 更新用品
- `DELETE /api/supply/{id}` - 删除用品

### 申请管理
- `GET /api/application/page` - 分页查询申请
- `POST /api/application` - 创建申请
- `PUT /api/application/approve/{id}` - 审批申请

## 开发说明

### 后端开发
- 使用MyBatis-Plus进行数据访问
- 统一返回格式使用Result类
- JWT进行身份认证
- 支持逻辑删除

### 前端开发
- 使用Vue3 Composition API
- Pinia进行状态管理
- Element Plus组件库
- 响应式设计

## 部署说明

### 后端部署
1. 打包：`mvn clean package`
2. 运行：`java -jar target/office-supply-system-1.0.0.jar`

### 前端部署
1. 构建：`npm run build`
2. 将dist目录部署到Web服务器

## 注意事项

1. 确保MySQL和Redis服务正常运行
2. 前端开发时注意API接口的跨域配置
3. 生产环境部署时需要修改数据库密码等敏感信息
4. 建议使用HTTPS进行生产环境部署

## 页面展示

![image-20250906141507614](https://i-blog.csdnimg.cn/img_convert/4f6b7ded2d484ff91ffa67886dfc2dd5.png)

![image-20250906141543625](https://i-blog.csdnimg.cn/img_convert/52a57d5fb370527b8161f798c9308e93.png)

![image-20250906141552543](https://i-blog.csdnimg.cn/img_convert/a944ad29ee2d16c540587429b4fd857f.png)

![image-20250906141602194](https://i-blog.csdnimg.cn/img_convert/70e01ef03eaeee44bc6dc8f7c661afdf.png)

![image-20250906141614146](https://i-blog.csdnimg.cn/img_convert/f72eaa086cf5a76cb47f917bc3e0b077.png)

![image-20250906141623674](https://i-blog.csdnimg.cn/img_convert/236f29b13b1b38952f6d73c2310560bb.png)

![image-20250906141631730](https://i-blog.csdnimg.cn/img_convert/400680104fcbc9c4b67490496a70d09e.png)

![image-20250906141659571](https://i-blog.csdnimg.cn/img_convert/062d20c32bfdae015f92906b935fd767.png)
