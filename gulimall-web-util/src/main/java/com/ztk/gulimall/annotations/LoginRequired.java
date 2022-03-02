package com.ztk.gulimall.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//这个是我们自定义的注解，“是否需要登录”
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

    boolean loginSuccess() default true;//这不是一个方法，只是一个变量，直接在方法名上设置为true和false即可

}
