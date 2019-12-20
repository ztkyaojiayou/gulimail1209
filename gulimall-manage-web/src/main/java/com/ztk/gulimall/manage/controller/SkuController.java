package com.ztk.gulimall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.ztk.gulimall.bean.PmsSkuInfo;
import com.ztk.gulimall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//保存/提交用户自定义/录入的所有数据到数据库中（skuInfo）
@Controller
@CrossOrigin
public class SkuController {

    @Reference
    SkuService SkuService;


    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {
        //处理默认图片(把第一张当做默认图片）
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if (StringUtils.isBlank(skuDefaultImg)) {
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }
        SkuService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }
}
