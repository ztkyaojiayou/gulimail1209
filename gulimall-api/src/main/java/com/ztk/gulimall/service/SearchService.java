package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.PmsSearchParam;
import com.ztk.gulimall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
