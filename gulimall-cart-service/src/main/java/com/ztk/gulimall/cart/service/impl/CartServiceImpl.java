package com.ztk.gulimall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.ztk.gulimall.bean.OmsCartItem;
import com.ztk.gulimall.cart.mapper.OmsCartItemMapper;
import com.ztk.gulimall.service.CartService;
import com.ztk.gulimall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;


    //1.从数据库中查询购物车数据，用于判断用户是否添加过当前商品
    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;

    }


    //2.添加当前商品至购物车
    @Override
    public void addCart(OmsCartItem omsCartItem) {
        System.out.println(omsCartItem.getQuantity());
        if (StringUtils.isNotBlank(omsCartItem.getMemberId())) {
            omsCartItemMapper.insertSelective(omsCartItem);//避免添加空值
        }
    }


    //3.（购物车已有该商品）更新购物车信息
    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {

        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("id",omsCartItemFromDb.getId());

        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb,e);

    }


    //4.把加入到购物车的商品信息同时同步到缓存中
    @Override
    public void flushCartCache(String memberId) {

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);

        // 同步到redis缓存中
        Jedis jedis = redisUtil.getJedis();

        Map<String,String> map = new HashMap<>();
        for (OmsCartItem cartItem : omsCartItems) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }

        jedis.del("user:"+memberId+":cart");
        jedis.hmset("user:"+memberId+":cart",map);

        jedis.close();
    }


    //5.根据userId查询/进入到用户的购物车清单页面，准备结算
    @Override
    public List<OmsCartItem> cartList(String userId) {
        Jedis jedis = null;
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        try {
             jedis = redisUtil.getJedis();

            List<String> hvals = jedis.hvals("user:" + userId + ":cart");

            for (String hval : hvals) {
                OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
                omsCartItems.add(omsCartItem);
            }
            
        }catch (Exception e){
            // 处理异常，记录系统日志
            e.printStackTrace();
            //String message = e.getMessage();
            //logService.addErrLog(message);
            return null;
        }finally {
            jedis.close();
        }

        return omsCartItems;
    }


    //6.结算前，在购物车清单页面修改/检查购买清单信息
    @Override
    public void checkCart(OmsCartItem omsCartItem) {

        Example e = new Example(OmsCartItem.class);

        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());

        omsCartItemMapper.updateByExampleSelective(omsCartItem,e);

        // 缓存同步
        flushCartCache(omsCartItem.getMemberId());

    }
}
