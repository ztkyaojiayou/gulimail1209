package com.ztk.gulimall.user.mapper;

import com.ztk.gulimall.user.bean.UmsMember;
import com.ztk.gulimall.user.bean.UmsMember;
//import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 回顾：mapper接口就是之前所说的Dao接口
 */
//public interface UserMapper extends Mapper<UmsMember> {//由于一个项目中需要与数据库打交道的次数非常多，因此需要写很多的增删改查方法，于是需要在mapper
    // .xml中写很多的“具体实现”代码，非常麻烦，通用mapper就是用于解决这件事情的 (只要这个工具箱里面有的方法，就不需要再写xml文件），它就是一个内部集成了很多增删改查的方法的工具箱，我们直接使用即可。（但我这边会报错：java.lang
// .RuntimeException: java.lang.reflect.InvocationTargetException，用不了，不知道什么原因？？？）
    public interface UserMapper{
    List<UmsMember> selectAllUser();
}
