package com.ztk.gulimall.user.controller;


import com.ztk.gulimall.user.bean.UmsMember;
import com.ztk.gulimall.user.bean.UmsMember;
import com.ztk.gulimall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("getAllUser")
    @ResponseBody//用于以json串的形式返回结果
    public List<UmsMember> getAllUser(){

        List<UmsMember> umsMember = userService.getAllUser();
        return umsMember;//测试通过
    }


    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello,user";//测试通过
    }
}
