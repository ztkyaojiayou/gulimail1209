package com.ztk.gulimall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ztk.gulimall.bean.PmsSkuAttrValue;
import com.ztk.gulimall.bean.PmsSkuImage;
import com.ztk.gulimall.bean.PmsSkuInfo;
import com.ztk.gulimall.bean.PmsSkuSaleAttrValue;
import com.ztk.gulimall.manage.mapper.PmsSkuAttrValueMapper;
import com.ztk.gulimall.manage.mapper.PmsSkuImageMapper;
import com.ztk.gulimall.manage.mapper.PmsSkuInfoMapper;
import com.ztk.gulimall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.ztk.gulimall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    //(当商家填写好了要录入的商品的所有信息之后）保存/提交用户自定义/录入的所有数据到数据库中（skuInfo）
    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        int i = pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();

        //1.保存平台属性，即由平台已经定义好了的属性及其属性值（通过foreach循环写入数据库）
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue:skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        //2.保存销售属性，即由商家自己定义/录入的商品的销售属性和及其对应的属性值（通过foreach循环写入数据库）
        List<PmsSkuSaleAttrValue> skuAttrSaleValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue:skuAttrSaleValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        //3.保存图片信息，即商家上传的与商品配的图片/音视频等文件
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage:skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);

        }

    }
}
