package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.PmsSkuInfo;

public interface SkuService {
    //保存/提交用户自定义/录入的所有数据到数据库中（skuInfo）
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);
}
