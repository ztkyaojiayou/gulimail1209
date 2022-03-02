package com.ztk.gulimall.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpclientUtil工具类（业务关键），用于检查业务方法是否需要用户登录
 * 如果需要，就把cookie中的token和当前登录人的ip地址发给远程服务器进行登录验证(ip和盐值有关），返回的result是验证结果true或者false。
 * 如果验证未登录，则直接重定向到登录页面。
 */
public class HttpclientUtil {
    //Get请求
    public static String doGet(String url)   {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200（代表请求已被服务器成功接收）
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                httpclient.close();
                return result;
            }
            httpclient.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return  null;
    }


    //Post请求
    public static String doPost(String url, Map<String,String> paramMap)   {
       // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http Post请求
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {

            if(paramMap!=null){
                List<BasicNameValuePair> list=new ArrayList<>();
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(),entry.getValue())) ;
                }
                HttpEntity httpEntity=new UrlEncodedFormEntity(list,"utf-8");
                httpPost.setEntity(httpEntity);
            }

            // 执行请求
            response = httpclient.execute(httpPost);

            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                httpclient.close();
                return result;
            }
            httpclient.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        return  null;
    }
}
