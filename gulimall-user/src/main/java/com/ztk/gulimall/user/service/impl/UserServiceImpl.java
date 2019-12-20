package com.ztk.gulimall.user.service.impl;

import com.ztk.gulimall.bean.UmsMember;
import com.ztk.gulimall.user.mapper.UserMapper;
import com.ztk.gulimall.service.UserService;//导入的是api里面的UserService包（其实，它会自动导入，但关键是必须把之前导入的包路径删掉）
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//UserService这个接口在gulimall-api这个模块里面（其实所有的service接口都在这个模块里面），这里只需要实现它即可
@Service
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
