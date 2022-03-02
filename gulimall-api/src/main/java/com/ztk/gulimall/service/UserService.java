package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.UmsMember;
import com.ztk.gulimall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    //1.获取所有的用户信息
    List<UmsMember> getAllUser();

    //2.用户登录
    UmsMember login(UmsMember umsMember);

    //3.将token存入redis一份，缓解服务器的压力（很重要）
    void addUserToken(String token, String memberId);

    //4.微博登录相关方法
    UmsMember checkOauthUser(UmsMember umsCheck);

    UmsMember addOauthUser(UmsMember umsMember);

    UmsMember getOauthUser(UmsMember umsMemberCheck);

    UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId);

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
