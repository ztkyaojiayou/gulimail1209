package com.ztk.gulimall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.ztk.gulimall.bean.PmsProductSaleAttr;
import com.ztk.gulimall.bean.PmsSkuInfo;
import com.ztk.gulimall.bean.PmsSkuSaleAttrValue;
import com.ztk.gulimall.service.SkuService;
import com.ztk.gulimall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    //2.前台通过skuId来查找/搜索商品详情页
    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId,ModelMap map) {

        PmsSkuInfo pmsSkuInfo = skuService.getSkuByIdFromDb(skuId);
        //sku对象
        map.put("skuInfo", pmsSkuInfo);
        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getSpuId(), pmsSkuInfo.getId());
        map.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);

        //2.1查询当前sku的spu的其他sku的集合的hash表
        Map<String, String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getSpuId());

        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";// "239|245"
            }
            skuSaleAttrHash.put(k, v);
        }

        //2.2将sku的销售属性hash表放到页面
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        map.put("skuSaleAttrHashJsonStr", skuSaleAttrHashJsonStr);


        return "item";
    }


    //1.测试方法
    @RequestMapping("index")
    //@ResponseBody//当要访问静态数据时，则不能再加此注解啦！！！！
    public String index(ModelMap modelMap) {
        modelMap.put("hello", "hello thymeleaf !!"); //往hello标签里面传递一个值
        return "index";
        //用于端口号设置的是8085，因此访问此页面的路径应该为：http://localhost:8085/index（测试通过）
        //结果如下：
        //hello thymeleaf !
        //{hello}
        //hello thymeleaf !!
    }
}
