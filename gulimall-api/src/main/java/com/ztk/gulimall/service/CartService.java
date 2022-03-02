package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.OmsCartItem;

import java.util.List;

public interface CartService {
    //1.从数据库中查询购物车数据，用于判断用户是否添加过当前商品
    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    //2.添加当前商品至购物车
    void addCart(OmsCartItem omsCartItem);

    //3.（购物车已有该商品）更新购物车信息
    void updateCart(OmsCartItem omsCartItemFromDb);

    //4.把加入到购物车的商品信息同时同步到缓存中
    void flushCartCache(String memberId);

    //5.根据userId查询/进入到用户的购物车清单页面，准备结算
    List<OmsCartItem> cartList(String userId);

    //6.结算前，在购物车清单页面修改/检查购买清单信息
    void checkCart(OmsCartItem omsCartItem);
}
