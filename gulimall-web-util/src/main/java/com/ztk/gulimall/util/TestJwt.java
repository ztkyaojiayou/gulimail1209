package com.ztk.gulimall.util;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtUtil的测试类
 */
public class TestJwt {
    //1.编码（生成token）
    //key：存放token的公有信息
    //map:存放其私有信息（如用户信息）
    //salt：存放其签名信息（如ip+time）
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("memberId","1");
        map.put("nickname","zhangsan");
        String ip = "127.0.0.1";
        String time = new SimpleDateFormat("yyyyMMdd HHmm").format(new Date());
        String encode = JwtUtil.encode("2019gulimall1228", map, ip + time);//盐值和ip有关(当然，这也是我们自定义的）
        System.err.println(encode);//得到的token为：eyJuaWNrbmFtZSI6InpoYW5nc2FuIiwibWVtYmVySWQiOiIxIn0

        //2.解码（解析token中的“私有部分，token中的私人信息并不是加密的，只是把明文信息变成了不可见的字符串。但是其实只要用一些工具就可以把base64编码解成明文，所以不要在JWT
        //中放入涉及私密的信息，因为实际上JWT并不是加密信息。）
        //怎么不用JwtUtil工具包中自定义的decode方法？？？？
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        byte[] tokenBytes = base64UrlCodec.decode("eyJuaWNrbmFtZSI6InpoYW5nc2FuIiwibWVtYmVySWQiOiIxIn0");
        String tokenJson = null;
        try {
            tokenJson = new String(tokenBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map map1 = JSON.parseObject(tokenJson, Map.class);
        System.out.println("64="+map1);


    }
}
