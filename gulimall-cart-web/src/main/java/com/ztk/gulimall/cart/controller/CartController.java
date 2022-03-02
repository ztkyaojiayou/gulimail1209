package com.ztk.gulimall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.ztk.gulimall.annotations.LoginRequired;
import com.ztk.gulimall.bean.OmsCartItem;
import com.ztk.gulimall.bean.PmsSkuInfo;
import com.ztk.gulimall.service.CartService;
import com.ztk.gulimall.service.SkuService;
import com.ztk.gulimall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 这个模块的思路和逻辑是按照以下的数字顺序进行的
 */
@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    ////4.准备下单结算，跳转到结算页“toTrade.html”（下一个模块的内容）(已经移至OrderController页面）
    //@RequestMapping("toTrade")
    //@LoginRequired(loginSuccess = true)
    //public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {
    //
    //    String memberId = (String)request.getAttribute("memberId");
    //    String nickname = (String)request.getAttribute("nickname");
    //
    //    return "toTrade";
    //}

    //3.结算前，在购物车清单页面修改/检查购买清单信息
    @RequestMapping("checkCart")
    @LoginRequired(loginSuccess = false)
    public String checkCart(String isChecked,String skuId,HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {

        //String memberId = "1";
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");
        //3.1 调用服务，修改状态
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setIsChecked(isChecked);
        cartService.checkCart(omsCartItem);

        //3.2 将最新的数据从缓存中查出，渲染给内嵌页
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        modelMap.put("cartList",omsCartItems);

        //3.3 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        return "cartListInner";
    }

    //2.进入购物车清单，准备结算
    @RequestMapping("cartList")
    @LoginRequired(loginSuccess = false)
    public String cartList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {

        List<OmsCartItem> omsCartItems = new ArrayList<>();
        //String memberId = "1";//由于此时还没有开发“用户登录”模块，因此这里就先用“1”表示“登陆成功”

        //现在换成真实的
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        if(StringUtils.isNotBlank(memberId)){
            // 2.1 说明此时已经登录，则在数据库中（db）查询该用户的购物车数据
            omsCartItems = cartService.cartList(memberId);
        }else{
            // 2.2 说明此时没有登录，则在浏览器的cookie中查询购物车的数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)){
                omsCartItems = JSON.parseArray(cartListCookie,OmsCartItem.class);
            }
        }

            //2.3 计算金额，准备结算
            //2.3.1 先计算每一种商品的总额
        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
        }
        modelMap.put("cartList",omsCartItems);

        //2.3.2 再计算所有被勾选的商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        return "cartList";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();

            if(omsCartItem.getIsChecked().equals("1")){
                totalAmount = totalAmount.add(totalPrice);
            }
        }

        return totalAmount;
    }

    //1.添加商品到购物车
    @RequestMapping("addToCart")
    @LoginRequired(loginSuccess = false)
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        // 1.1调用商品服务查询商品信息
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId, "");

        // 1.2将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getSpuId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(quantity));


        // 1.3判断用户是否登录
       // String memberId = "1";//"1";request.getAttribute("memberId");
        //现在换成真实的
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        if (StringUtils.isBlank(memberId)) {
            // 1.3.1 用户没有登录
            // 获取浏览器的cookie中原有的购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isBlank(cartListCookie)) {
                // 若cookie为空，则直接添加到购物车
                omsCartItems.add(omsCartItem);
            } else {
                // cookie不为空，则先判断将要添加的商品在cookie中是否存在
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);//转换为json字符串

                boolean exist = if_cart_exist(omsCartItems, omsCartItem);
                if (exist) {
                    // 之前添加过，更新购物车添加数量
                    for (OmsCartItem cartItem : omsCartItems) {
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));
                        }
                    }
                } else {
                    // 之前没有添加，新增当前的购物车
                    omsCartItems.add(omsCartItem);
                }
            }

            // 最后，更新cookie
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {

            // 1.3.2 用户已经登录
            // 从数据库中查询购物车数据，用于判断该用户之前是否添加过当前商品
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(memberId,skuId);

            if(omsCartItemFromDb==null){
                // 该用户没有添加过当前商品，则添加至购物车
                omsCartItem.setMemberId(memberId);
                omsCartItem.setMemberNickname("test小明");
                omsCartItem.setQuantity(new BigDecimal(quantity));
                cartService.addCart(omsCartItem);

            }else{
                // 该用户添加过当前商品，更新购物车即可
                omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity().add(omsCartItem.getQuantity()));
                cartService.updateCart(omsCartItemFromDb);
            }

            // 同时，同步到缓存中
            cartService.flushCartCache(memberId);
        }


        return "redirect:/success.html";
    }

    private boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {

        boolean b = false;

        for (OmsCartItem cartItem : omsCartItems) {
            String productSkuId = cartItem.getProductSkuId();

            if (productSkuId.equals(omsCartItem.getProductSkuId())) {
                b = true;
            }
        }
        return b;
    }

}
