package com.ztk.gulimall.util;

import io.jsonwebtoken.*;

import java.util.Map;

/**
 * 制作 JWT（token）的工具类（JwtUtil）
 * JWT（Json Web Token） 是为了在网络应用环境间传递声明而执行的一种基于JSON的开放标准。
 * JWT的声明一般被用来在身份提供者和服务提供者间传递被认证的用户身份信息，以便于从资源服务器获取资源。比如用在用户登录上。
 * JWT 最重要的作用就是对 token信息的防伪作用。
 * 一个JWT由三个部分组成：公共部分、私有部分、签名部分。最后由这三者组合进行base64编码得到JWT。
 */
public class JwtUtil {
    //1.生成token（base64编码）
    public static String encode(String key, Map<String,Object> param, String salt){//采用的是base64编码技术
        if(salt!=null){
            key+=salt;
        }
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,key);

        jwtBuilder = jwtBuilder.setClaims(param);

        String token = jwtBuilder.compact();
        return token;

    }

    //2.解析token，用于验证token的真伪/是否过期（解码）
    public  static Map<String,Object>  decode(String token ,String key,String salt){
        Claims claims=null;
        if (salt!=null){
            key+=salt;
        }
        try {
            claims= Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch ( JwtException e) {
           return null;
        }
        return  claims;
    }
}
