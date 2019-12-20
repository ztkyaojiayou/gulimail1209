package com.ztk.gulimall.user.service.impl;


import com.ztk.gulimall.bean.UmsMember;
import com.ztk.gulimall.service.UserService;
import com.ztk.gulimall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;//springboot里面的包，现在要替换成dubbo里面的包
import com.alibaba.dubbo.config.annotation.Service;//dubbo分布式服务里面的包

import java.util.List;

@Service//dubbo分布式服务里面的注解
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<UmsMember> getAllUser() {
        //List<UmsMember> usmMemberList = userMapper.selectAllUser();//原始方法（需要配置“具体实现”的xml文件）
        List<UmsMember> umsMemberList = userMapper.selectAll();//使用了通用mapper之后，就可以用通用mapper这个工具箱里面的常用方法了。
        return umsMemberList;
    }
}
