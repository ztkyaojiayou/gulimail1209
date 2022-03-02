package com.ztk.gulimall.service;

import com.ztk.gulimall.bean.OmsOrder;

public interface OrderService {
    String checkTradeCode(String memberId, String tradeCode);

    void saveOrder(OmsOrder omsOrder);

    String genTradeCode(String memberId);


    void updateOrder(OmsOrder omsOrder);


    OmsOrder getOrderByOutTradeNo(String outTradeNo);
}
