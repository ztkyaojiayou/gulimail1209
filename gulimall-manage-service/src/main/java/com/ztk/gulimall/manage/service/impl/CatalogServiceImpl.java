package com.ztk.gulimall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ztk.gulimall.bean.PmsBaseCatalog1;
import com.ztk.gulimall.bean.PmsBaseCatalog2;
import com.ztk.gulimall.bean.PmsBaseCatalog3;
import com.ztk.gulimall.manage.mapper.PmsBaseCatalog1Mapper;
import com.ztk.gulimall.manage.mapper.PmsBaseCatalog2Mapper;
import com.ztk.gulimall.manage.mapper.PmsBaseCatalog3Mapper;
import com.ztk.gulimall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
//CatalogService这个接口在gulimall-api这个模块里面（其实所有的service接口都在这个模块里面，因为这是和前端约定好的，因此就单独放在了这里），这里（在具体业务模块中）只需要实现它即可
@Service
public class CatalogServiceImpl implements CatalogService {

    //先自动注入mapper
    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    //1.获取一级目录
    @Override
    public List<PmsBaseCatalog1> getCatalog1() {

        return pmsBaseCatalog1Mapper.selectAll();
    }

    //2.（根据已经获取到的一级目录）获取二级目录
    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {

        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        List<PmsBaseCatalog2> pmsBaseCatalog2s = pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);

        return pmsBaseCatalog2s;
    }

    //3.（根据已经获取到的二级目录）获取三级目录
    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {

        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        List<PmsBaseCatalog3> pmsBaseCatalog3s = pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);

        return pmsBaseCatalog3s;
    }
}
