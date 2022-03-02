package com.ztk.gulimall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.ztk.gulimall.bean.PmsSkuAttrValue;
import com.ztk.gulimall.bean.PmsSkuImage;
import com.ztk.gulimall.bean.PmsSkuInfo;
import com.ztk.gulimall.bean.PmsSkuSaleAttrValue;
import com.ztk.gulimall.manage.mapper.PmsSkuAttrValueMapper;
import com.ztk.gulimall.manage.mapper.PmsSkuImageMapper;
import com.ztk.gulimall.manage.mapper.PmsSkuInfoMapper;
import com.ztk.gulimall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.ztk.gulimall.service.SkuService;
import com.ztk.gulimall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    RedisUtil redisUtil;

    //1.(当商家填写好了要录入的商品的所有信息之后）保存/提交用户自定义/录入的所有数据到数据库中（skuInfo）
    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        int i = pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();

        //1.1保存平台属性，即由平台已经定义好了的属性及其属性值（通过foreach循环写入数据库）
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        //1.2保存销售属性，即由商家自己定义/录入的商品的销售属性和及其对应的属性值（通过foreach循环写入数据库）
        List<PmsSkuSaleAttrValue> skuAttrSaleValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuAttrSaleValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        //1.3保存图片信息，即商家上传的与商品配的图片/音视频等文件
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);

        }

    }

    //2.根据skuId来查询商品详情
    @Override
    public PmsSkuInfo getSkuByIdFromDb(String skuId){
        // sku商品对象
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        // sku的图片集合
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(pmsSkuImages);
        return skuInfo;
    }


    //3.根据skuId来查询商品详情
    //3.1原始策略，不引入缓存机制
    //@Override
    //public PmsSkuInfo getSkuById(String skuId) {
    //    //4.1获取对应商品的详情信息（是一个对象）
    //    PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
    //    pmsSkuInfo.setId(skuId);
    //    PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);//此时已获取商品信息
    //
    //    //4.2获取对应商品的图片（是个集合）
    //    PmsSkuImage pmsSkuImage = new PmsSkuImage();
    //    pmsSkuImage.setSkuId(skuId);
    //    List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);//此时已获取图片集合
    //
    //    skuInfo.setSkuImageList(pmsSkuImages);//再把查询到的图片添加/封装到要返回的skuInfo对象中
    //    return skuInfo;
    //}

    //3.2查询时引入缓存机制（主流方法）
    public PmsSkuInfo getSkuById(String skuId,String ip) {
        System.out.println("ip为"+ip+"的同学:"+Thread.currentThread().getName()+"进入的商品详情的请求");
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        // 链接缓存
        Jedis jedis = redisUtil.getJedis();
        // 查询缓存
        String skuKey = "sku:"+skuId+":info";
        String skuJson = jedis.get(skuKey);

        if(StringUtils.isNotBlank(skuJson)){//if(skuJson!=null&&!skuJson.equals(""))
            System.out.println("ip为"+ip+"的同学:"+Thread.currentThread().getName()+"从缓存中获取商品详情");

            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        }else{
            // 如果缓存中没有，查询mysql
            System.out.println("ip为"+ip+"的同学:"+Thread.currentThread().getName()+"发现缓存中没有，申请缓存的分布式锁："+"sku:" + skuId + ":lock");

            // 设置分布式锁
            String token = UUID.randomUUID().toString();
            String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10*1000);// 拿到锁的线程有10秒的过期时间
            if(StringUtils.isNotBlank(OK)&&OK.equals("OK")){
                // 设置成功，有权在10秒的过期时间内访问数据库
                System.out.println("ip为"+ip+"的同学:"+Thread.currentThread().getName()+"有权在10秒的过期时间内访问数据库："+"sku:" + skuId + ":lock");

                pmsSkuInfo =  getSkuByIdFromDb(skuId);

                if(pmsSkuInfo!=null){
                    // mysql查询结果存入redis
                    jedis.set("sku:"+skuId+":info", JSON.toJSONString(pmsSkuInfo));
                }else{
                    // 数据库中不存在该sku
                    // 为了防止缓存穿透将，null或者空字符串值设置给redis
                    jedis.setex("sku:"+skuId+":info",60*3,JSON.toJSONString(""));
                }

                // 在访问mysql后，将mysql的分布锁释放
                System.out.println("ip为"+ip+"的同学:"+Thread.currentThread().getName()+"使用完毕，将锁归还："+"sku:" + skuId + ":lock");
                String lockToken = jedis.get("sku:" + skuId + ":lock");
                if(StringUtils.isNotBlank(lockToken)&&lockToken.equals(token)){
                    //jedis.eval("lua");可与用lua脚本，再查询到key的同时删除该key，防止高并发下的意外的发生
                    jedis.del("sku:" + skuId + ":lock");// 用token确认删除的是自己的sku的锁
                }


            }else{
                // 设置失败，自旋（该线程在睡眠几秒后，重新尝试访问本方法）
                System.out.println("ip为"+ip+"的同学:"+Thread.currentThread().getName()+"没有拿到锁，开始自旋");

                return getSkuById(skuId,ip);
            }
        }
        jedis.close();
        return pmsSkuInfo;
    }


    //4.根据spuId来获得商品的属性值
    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String spuId) {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(spuId);

        return pmsSkuInfos;
    }


    //5.根据三级目录catalog3Id来获得平台属性
    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String skuId = pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);

            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal price) {
        return false;
    }


    ////6.根据skuId来查询商品详情（与方法2相同，只是换了一个方法名）
    //@Override
    //public PmsSkuInfo getSkuById(String skuId) {
    //        // sku商品对象
    //        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
    //        pmsSkuInfo.setId(skuId);
    //        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
    //
    //        // sku的图片集合
    //        PmsSkuImage pmsSkuImage = new PmsSkuImage();
    //        pmsSkuImage.setSkuId(skuId);
    //        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
    //        skuInfo.setSkuImageList(pmsSkuImages);
    //        return skuInfo;
    //}
}
