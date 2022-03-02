package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.PmsBaseAttrInfo;
import com.ztk.gulimall.bean.PmsBaseAttrValue;
import com.ztk.gulimall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

/**
 *所有web的service接口都定义在了这个模块里面，因为这是和前端约定好的，因此就单独放在了这里，在具体业务模块中只需要实现它即可
 */

public interface AttrService {
    //1.（从后端数据库中）获取属性信息（再返回到前台）
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    //2.（从后端数据库中）获取属性值（再返回到前台）
    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    //3.保存/新增、修改（用户从前端传入的）属性值
    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    //4.查询所有符合属性值的商品（返回到前端）？
    List<PmsBaseSaleAttr> baseSaleAttrList();

    //5.
    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);
}
