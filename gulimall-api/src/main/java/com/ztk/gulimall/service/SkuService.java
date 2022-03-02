package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuService {
    //1.保存/提交用户自定义/录入的所有数据到数据库中（skuInfo）
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    //2.根据skuId来查询商品详情（换成了方法四：getSkuByIdFromDb）
    //PmsSkuInfo getSkuById(String skuId);
    //2.1重载方法
    PmsSkuInfo getSkuById(String skuId,String ip);

    //3.根据spuId来获得商品的属性值
    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String spuId);

    //4.根据skuId来查询商品详情(就是原来的方法二，只是在加入了缓存之后就换了一个名字）
    PmsSkuInfo getSkuByIdFromDb(String skuId);

    //5.根据三级目录catalog3Id来获得平台属性
    List<PmsSkuInfo> getAllSku(String catalog3Id);

    //6.
    boolean checkPrice(String productSkuId, BigDecimal price);
}
