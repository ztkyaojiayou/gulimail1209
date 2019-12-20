package com.ztk.gulimall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ztk.gulimall.bean.PmsProductImage;
import com.ztk.gulimall.bean.PmsProductInfo;
import com.ztk.gulimall.bean.PmsProductSaleAttr;
import com.ztk.gulimall.manage.util.PmsUploadUtil;
import com.ztk.gulimall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;

    //5.获取商家自己定义/录入的商品的(spu)图片列表
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){//传入的参数
        List<PmsProductImage> pmsProductImages = spuService.spuImageList(spuId);
        return pmsProductImages;
    }


    //4.获取商家自己定义/录入的商品的销售属性和对应的属性值
    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){//传入的参数
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    //3.（与商品配的图片/音视频等）文件的上传（不需要service类，直接在这里就完成了）
    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){//传入的参数

        //1.将图片或者音视频上传到分布式的文件存储系统
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);

        // 2.再将图片的存储路径返回到页面
        //System.out.println(imgUrl);
        return imgUrl;
    }



    //2.查询销售属性字典表（baseSaleAttrList）,用于在添加商品的销售属性时进行选择
    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){//传入的参数（即用户添加的pmsProductInfo信息）

        return "success";
    }


    //1.根据第三级目录查询所有符合条件的具体商品
    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){
        List<PmsProductInfo> PmsProductInfos = spuService.spuList(catalog3Id);
        return PmsProductInfos;
    }
}
