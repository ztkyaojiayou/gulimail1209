package com.gulimall.user.controller;

//import com.ztk.gulimall.user.bean.UmsMember;//由于bean已经全部放在了api子模块中，因此所有文件中的包应该都从api这个子模块导入，否则会报错

import com.alibaba.dubbo.config.annotation.Reference;
import com.ztk.gulimall.bean.UmsMember;
import com.ztk.gulimall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class UserController {
    //@Autowired//当在同一个容器时使用，现在不在同一个容器里面，则不能使用了，要换成dubbo里面的的注解

    @Reference//这是dubbo里面的注解
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
