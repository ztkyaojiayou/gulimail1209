package com.ztk.gulimall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ztk.gulimall.bean.PmsProductImage;
import com.ztk.gulimall.bean.PmsProductInfo;
import com.ztk.gulimall.bean.PmsProductSaleAttr;
import com.ztk.gulimall.bean.PmsProductSaleAttrValue;
import com.ztk.gulimall.manage.mapper.PmsProductImageMapper;
import com.ztk.gulimall.manage.mapper.PmsProductInfoMapper;
import com.ztk.gulimall.manage.mapper.PmsProductSaleAttrMapper;
import com.ztk.gulimall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.ztk.gulimall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service//用的是dubbo的注解
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    //1.根据第三级目录查询所有符合条件的具体商品
    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);

        return pmsProductInfos;
    }

    //2.获取商家自己定义/录入的商品的销售属性和对应的属性值
    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {

        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for (PmsProductSaleAttr productSaleAttr:pmsProductSaleAttrs) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }


        return pmsProductSaleAttrs ;
    }

    //3.根据spuId获取商家自己定义/录入的商品的(spu)图片列表
    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }

    //4.（客户）根据spuId和skuId所构成的键值对直接获取销售属性列表（由于只需要查询一次数据库，从而进行了优化）
    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String spuId, String skuId) {

        //PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        //pmsProductSaleAttr.setProductId(spuId);
        //List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        //
        //for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
        //    String saleAttrId = productSaleAttr.getSaleAttrId();
        //
        //    PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
        //    pmsProductSaleAttrValue.setSaleAttrId(saleAttrId);
        //    pmsProductSaleAttrValue.setProductId(spuId);
        //    List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
        //
        //    productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        //
        //}

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(spuId
                ,skuId);
        return pmsProductSaleAttrs;

        
    }
}
