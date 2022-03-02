package com.ztk.gulimall.interceptors;

import com.alibaba.fastjson.JSON;
import com.ztk.gulimall.annotations.LoginRequired;
import com.ztk.gulimall.util.CookieUtil;
import com.ztk.gulimall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;

/**
 *自定义的登录检查的拦截器，用于标识具体的方法是否需要通过拦截器以及调用认证中心对请求中的token进行验证
 *虽然不需要每一个模块都要做一个token的保存功能，但加入拦截器(登录检查拦截器)这个验证功能是每个模块都必须要有的，也就是所有web模块都需要的
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    // preHandle：拦截之后要先做的事情
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断并获取被拦截请求方法上的注解@LoginRequired
        HandlerMethod hm = (HandlerMethod) handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);

        StringBuffer url = request.getRequestURL();
        System.out.println(url);

        //若有此注解，则要拦截
        if (methodAnnotation == null) {
            return true;
        }

        String token = "";
        //获取老token
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }
        //获取新token
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }

        //是否必须登录
        boolean loginSuccess = methodAnnotation.loginSuccess();// 获得该请求前是否必登录成功

        // 调用认证中心进行验证（重点）
        String success = "fail";
        Map<String,String> successMap = new HashMap<>();
        if(StringUtils.isNotBlank(token)){
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            String successJson  = HttpclientUtil.doGet("http://passport.gmall.com:8085/verify?token=" + token+"&currentIp="+ip);

            successMap = JSON.parseObject(successJson,Map.class);

            success = successMap.get("status");

        }

        if (loginSuccess) {
            // 必须登录成功才能使用
            if (!success.equals("success")) {
                //重定向回passport登录页面
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://passport.gmall.com:8085/index?ReturnUrl="+requestURL);
                return false;
            }

            // 需要将token携带的用户信息写入
            request.setAttribute("memberId", successMap.get("memberId"));
            request.setAttribute("nickname", successMap.get("nickname"));
            //验证通过，覆盖cookie中的token
            if(StringUtils.isNotBlank(token)){
                //把此token保存到浏览器中
                CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
            }

        } else {
            //没有登录也能用，但是必须验证
            if (success.equals("success")) {
                //需要将token携带的用户信息写入
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickname"));

                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }

            }
        }


        return true;
    }
}
