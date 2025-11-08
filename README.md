# 车险续保平台 - 后端服务

## 项目简介

本项目是车险续保平台的后端服务，基于 Spring Boot 3.5.7 + MyBatis + MySQL 构建，提供 C 端用户和管理端的完整 API 服务。

## 技术栈

- **框架**: Spring Boot 3.5.7
- **持久层**: MyBatis 3.0.5
- **数据库**: MySQL 8.0
- **安全**: Spring Security 6
- **认证方式**: Session + Cookie
- **Java 版本**: 17
- **构建工具**: Maven

## 项目结构

```
backend/car/
├── src/main/java/org/demo/car/
│   ├── common/                 # 公共模块
│   │   ├── config/            # 配置类
│   │   ├── enums/             # 枚举类
│   │   ├── exception/         # 异常类
│   │   └── result/            # 响应结果类
│   ├── controller/            # 控制器层
│   │   ├── admin/             # 管理端接口
│   │   ├── user/              # C端接口
│   │   └── common/            # 公共接口
│   ├── dto/                   # 数据传输对象
│   │   ├── request/           # 请求DTO
│   │   └── response/          # 响应DTO
│   ├── entity/                # 实体类
│   ├── mapper/                # MyBatis Mapper
│   ├── security/              # 安全配置
│   ├── service/               # 业务逻辑层
│   │   └── impl/              # 服务实现
│   └── CarApplication.java    # 启动类
├── src/main/resources/
│   ├── application.properties  # 配置文件
│   ├── schema.sql             # 数据库表结构
│   └── data.sql               # 初始化数据
└── pom.xml                    # Maven配置
```

## 数据库设计

### 核心表

1. **用户相关**
   - `user`: C端用户表
   - `admin`: 管理员表
   - `role`: 角色表
   - `permission`: 权限表
   - `role_permission`: 角色权限关联表

2. **品牌车辆**
   - `brand`: 品牌表（含经销商信息）
   - `vehicle`: 车辆表
   - `vehicle_image`: 车辆图片表

3. **业务数据**
   - `vehicle_appraisal`: 旧车估价表
   - `vehicle_inquiry`: 新车询价表
   - `activity`: 活动表
   - `benefit`: 权益表
   - `user_benefit`: 用户权益领取记录表
   - `user_favorite`: 用户喜欢表

## 核心功能

### C端功能

1. **用户认证**
   - 微信 H5 登录（通过 openid）
   - 绑定手机号
   - 用户信息管理

2. **车辆浏览**
   - 精选现车列表
   - 车辆列表（分页）
   - 车辆详情
   - 同品牌车型推荐
   - 车辆收藏

3. **业务提交**
   - 旧车估价申请
   - 新车询价
   - 权益领取

4. **活动**
   - 限时优惠列表
   - 活动中心
   - 活动详情

### 管理端功能

1. **权限管理**
   - 账号管理
   - 角色管理
   - 权限配置

2. **内容管理**
   - 品牌管理
   - 车辆管理
   - 活动管理
   - 权益管理

3. **业务管理**
   - 估价列表及跟进
   - 询价列表及处理
   - 用户管理

## API 接口

### 公共接口

```
POST   /api/common/upload          # 文件上传
POST   /api/common/sms/send        # 发送验证码
POST   /api/common/sms/verify      # 验证验证码
GET    /api/common/config          # 获取配置
GET    /api/health                 # 健康检查
```

### C端接口

```
# 用户认证
POST   /api/user/login             # 用户登录
POST   /api/user/bind-phone        # 绑定手机号
GET    /api/user/info              # 获取用户信息
PUT    /api/user/info              # 更新用户信息
POST   /api/user/logout            # 退出登录

# 车辆
GET    /api/user/vehicle/featured  # 精选现车
GET    /api/user/vehicle/list      # 车辆列表
GET    /api/user/vehicle/:id       # 车辆详情
GET    /api/user/vehicle/:id/images        # 车辆图片
GET    /api/user/vehicle/:id/similar       # 相似车型
```

### 管理端接口

```
# 管理员认证
POST   /api/admin/login            # 管理员登录
GET    /api/admin/info             # 获取管理员信息
POST   /api/admin/logout           # 退出登录

# 其他管理端接口...
```

## 环境配置

### 数据库配置

修改 `application.properties` 中的数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/car_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### 文件上传配置

```properties
file.upload.path=/uploads/
file.upload.base-url=http://localhost:8080/api
```

### 微信配置

```properties
wechat.appid=your_app_id
wechat.secret=your_app_secret
```

## 运行项目

### 1. 创建数据库

```sql
CREATE DATABASE car_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 运行项目

```bash
cd backend/car
mvn spring-boot:run
```

### 3. 访问接口

- 接口地址: http://localhost:8080/api
- 健康检查: http://localhost:8080/api/health

## 默认账号

### 管理员账号

- 用户名: admin
- 密码: admin123

## 开发说明

### 短信验证码

当前使用 Mock 实现，验证码会输出到控制台日志。生产环境需要接入真实的短信服务。

### 微信集成

当前使用 Mock 实现，返回测试数据。生产环境需要接入微信公众号 API。

### 文件存储

当前使用本地文件存储，文件保存在 `/uploads/` 目录。生产环境建议使用云存储（如阿里云 OSS）。

## 权限说明

### 角色权限

1. **超级管理员 (SUPER_ADMIN)**
   - 拥有所有权限
   - 可以管理其他管理员账号

2. **普通管理员 (ADMIN)**
   - 除权限管理外的其他功能
   - 可以管理业务数据

3. **业务员 (SALESMAN)**
   - 只能查看和跟进业务数据
   - 无法修改系统配置

### 权限控制

- 使用 Spring Security 实现
- 基于 Session + Cookie 认证
- 支持角色级别和权限级别的访问控制

## 注意事项

1. **Session 配置**
   - 默认 Session 超时时间: 30分钟
   - Cookie 名称: CAR_SESSION
   - 建议生产环境配置 Redis 实现 Session 共享

2. **CORS 配置**
   - 当前允许所有域名跨域访问
   - 生产环境需要限制允许的域名

3. **密码加密**
   - 使用 BCrypt 加密
   - 不可逆加密，保证安全性

4. **SQL 初始化**
   - 首次运行会自动执行 schema.sql 和 data.sql
   - 如不需要可在配置文件中关闭

## 待实现功能

以下功能已在技术方案中设计，但尚未完全实现，可根据需要继续开发：

- [ ] 更多管理端控制器（品牌、活动、权益等）
- [ ] 完整的权限拦截器
- [ ] 微信真实 API 集成
- [ ] 短信服务真实 API 集成
- [ ] 云存储集成
- [ ] 操作日志记录
- [ ] 数据统计报表

## 联系方式

如有问题，请联系开发团队。

