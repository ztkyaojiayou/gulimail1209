package com.ztk.gulimall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.ztk.gulimall.bean.UmsMember;
import com.ztk.gulimall.bean.UmsMemberReceiveAddress;
import com.ztk.gulimall.service.UserService;
import com.ztk.gulimall.user.mapper.UserMapper;
import com.ztk.gulimall.user.mapper.UmsMemberReceiveAddressMapper;
import com.ztk.gulimall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;//springboot里面的包，现在要替换成dubbo里面的包
import com.alibaba.dubbo.config.annotation.Service;//dubbo分布式服务里面的包
import redis.clients.jedis.Jedis;

import java.util.List;

@Service//这是dubbo分布式服务里面的注解
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Autowired
    RedisUtil redisUtil;

    //1.获取所有的用户信息
    @Override
    public List<UmsMember> getAllUser() {
        //List<UmsMember> usmMemberList = userMapper.selectAllUser();//原始方法（需要配置“具体实现”的xml文件）
        List<UmsMember> umsMemberList = userMapper.selectAll();//使用了通用mapper之后，就可以用通用mapper这个工具箱里面的常用方法了。
        return umsMemberList;
    }


    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {

        // 封装的参数对象
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);


//       Example example = new Example(UmsMemberReceiveAddress.class);
//       example.createCriteria().andEqualTo("memberId",memberId);
//       List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(example);

        return umsMemberReceiveAddresses;
    }


    //2.用户登录
    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();

            if(jedis!=null){
                String umsMemberStr = jedis.get("user:" + umsMember.getPassword()+umsMember.getUsername() + ":info");

                if (StringUtils.isNotBlank(umsMemberStr)) {
                    // 密码正确
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                }
            }
            // 链接redis失败，从数据库查询
            UmsMember umsMemberFromDb =loginFromDb(umsMember);
            if(umsMemberFromDb!=null){
                jedis.setex("user:" + umsMember.getPassword()+umsMember.getUsername() + ":info",60*60*24, JSON.toJSONString(umsMemberFromDb));
            }
            return umsMemberFromDb;
        }finally {
            jedis.close();
        }
    }

    //从数据库查询用户信息（loginFromDb方法）
    private UmsMember loginFromDb(UmsMember umsMember) {

        List<UmsMember> umsMembers = userMapper.select(umsMember);

        if(umsMembers!=null){
            return umsMembers.get(0);
        }

        return null;

    }

    //3.将token存入redis一份，缓解服务器的压力（很重要）
    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();

        jedis.setex("user:"+memberId+":token",60*60*2,token);

        jedis.close();
    }

    //4.微博登录相关方法
    @Override
    public UmsMember addOauthUser(UmsMember umsMember) {
        userMapper.insertSelective(umsMember);

        return umsMember;
    }
    //5.微博登录相关方法
    @Override
    public UmsMember checkOauthUser(UmsMember umsCheck) {
        UmsMember umsMember = userMapper.selectOne(umsCheck);
        return umsMember;
    }

    //6.微博登录相关方法
    public UmsMember getOauthUser(UmsMember umsMemberCheck) {


        UmsMember umsMember = userMapper.selectOne(umsMemberCheck);
        return umsMember;
    }

    //7.
    public UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(receiveAddressId);
        UmsMemberReceiveAddress umsMemberReceiveAddress1 = umsMemberReceiveAddressMapper.selectOne(umsMemberReceiveAddress);
        return umsMemberReceiveAddress1;
    }



}
