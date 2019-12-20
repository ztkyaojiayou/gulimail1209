package com.ztk.gulimall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ztk.gulimall.bean.PmsBaseCatalog1;
import com.ztk.gulimall.bean.PmsBaseCatalog2;
import com.ztk.gulimall.bean.PmsBaseCatalog3;
import com.ztk.gulimall.service.CatalogService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin//此注解用于解决跨域访问问题
public class CatalogController {

    @Reference
    CatalogService catalogService;

    //1.获取一级目录
    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){

        List<PmsBaseCatalog1> catalog1s = catalogService.getCatalog1();
        return catalog1s;
    }
    //2.（根据已经获取到的一级目录）获取二级目录
    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){

        List<PmsBaseCatalog2> catalog2s = catalogService.getCatalog2(catalog1Id);
        return catalog2s;
    }

    //3.（根据已经获取到的二级目录）获取三级目录
    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){

        List<PmsBaseCatalog3> catalog3s = catalogService.getCatalog3(catalog2Id);
        return catalog3s;
    }

}
