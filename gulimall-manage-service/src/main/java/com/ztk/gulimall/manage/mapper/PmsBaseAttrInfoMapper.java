package com.ztk.gulimall.manage.mapper;

import com.ztk.gulimall.bean.PmsBaseAttrInfo;
import tk.mybatis.mapper.common.Mapper;
//mapper接口就是dao接口，用于定义与数据库打交道的方法，这里使用的是通用mapper，因此不再需要xml配置文件进行具体实现了
public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {
}
