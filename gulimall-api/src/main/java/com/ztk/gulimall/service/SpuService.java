package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.PmsProductImage;
import com.ztk.gulimall.bean.PmsProductInfo;
import com.ztk.gulimall.bean.PmsProductSaleAttr;

import java.util.List;

//1.根据第三级目录查询所有符合条件的具体商品
public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    //2.获取商家自己定义/录入的商品的销售属性和对应的属性值
    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    //3.根据spuId获取spu的图片列表
    List<PmsProductImage> spuImageList(String spuId);

    //4.根据spuId和skuId所构成的键值对直接获取销售属性列表（由于只需要查询一次数据库，从而进行了优化）
        List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String spuId, String skuId);
    }
