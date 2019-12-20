package com.ztk.gulimall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ztk.gulimall.bean.PmsBaseAttrInfo;
import com.ztk.gulimall.bean.PmsBaseAttrValue;
import com.ztk.gulimall.bean.PmsBaseSaleAttr;
import com.ztk.gulimall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin//此注解用于解决跨域访问问题
public class AttrController  {

    @Reference//当这个接口不是在本模块时（此时在api模块中)，则只能通过这个注解进行远程自动注入
    AttrService attrService;

    //1.（从后端数据库中）获取属性信息和对应的属性值（也即方法2）（再返回到前台）
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;//通过@ResponseBody注解的配合，向前端返回json格式的字符串
    }

    //2.（从后端数据库中）获取属性值（再返回到前台）（方法1后面已经集成了）
    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){

        List<PmsBaseAttrValue> PmsBaseAttrValues= attrService.getAttrValueList(attrId);
        return PmsBaseAttrValues;//通过@ResponseBody注解的配合，向前端返回json格式的字符串
    }

    //3.保存/新增、修改（用户从前端传入的）属性值
    @RequestMapping("saveAttrInfo")
    @ResponseBody//用于以json字符串的形式直接向前端返回“success”这个字符串，以提示提交成功
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){//@RequestBody这个注解用于接收前端传过来的json
        // 字符串，并把它封装到PmsBaseAttrInfo类中
        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return "success";
    }

    //4.查询所有符合属性值的商品（返回到前端）？
    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList(){

        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }

}
