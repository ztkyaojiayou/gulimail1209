# gulimail1209 本地修改了第一次版本
gulimall-user：此时还没有把web层和service层分开，只是一个演示项目，用户服务端口用的是：8080
#第一天课程：用springboot写了一个demo
实现了简单的增删改查
#第二天课程：搭建分布式环境，正式开始项目开发
采用分布式架构对项目进行调整，把之前的一个模块（user）拆分为两个项目（service和web），因此端口号也要调整
gulimall-user-service这个模块使用的端口为：8070（即后端）
gulimall-user-web这个模块使用的端口为：8080（即前端）

#第三天课程：项目的前后端分离准备工作
gulimall-manage-service这个模块使用的端口为：8071（即后端）
gulimall-manage-web这个模块使用的端口为：8081（即前端）

#第四天课程：开发后台商品管理系统

#第五天课程：开发前端商品展示系统
gulimall-item-service这个模块使用的端口为：8072（即后端）这个模块我们不需要再做，直接调用gulimall-manage-service这个模块即可
gulimall-item-web这个模块使用的端口为：8085（即前端）