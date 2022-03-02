package com.ztk.gulimall.manage.mapper;

import com.ztk.gulimall.bean.PmsSkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {
    //5.根据spuId来获得商品的属性值
    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(String spuId);
}
