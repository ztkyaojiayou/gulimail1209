package com.ztk.gulimall.user.service.impl;

import com.ztk.gulimall.user.bean.UmsMember;
import com.ztk.gulimall.user.mapper.UserMapper;
import com.ztk.gulimall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> usmMemberList = userMapper.selectAllUser();
        //List<UmsMember> umsMemberList = userMapper.selectAll();//使用了通用mapper之后，就可以用通用mapper这个工具箱里面的常用方法了。
        return usmMemberList;
    }
}
