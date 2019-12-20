package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.PmsBaseCatalog1;
import com.ztk.gulimall.bean.PmsBaseCatalog2;
import com.ztk.gulimall.bean.PmsBaseCatalog3;

import java.util.List;

public interface CatalogService {
    //1.获取一级目录
    List<PmsBaseCatalog1> getCatalog1();
    //2.（根据已经获取到的一级目录）获取二级目录
    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);
    //3.（根据已经获取到的二级目录）获取三级目录
    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
