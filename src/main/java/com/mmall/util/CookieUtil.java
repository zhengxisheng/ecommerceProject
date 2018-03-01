package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fanlinglong on 2018/3/1.
 * Cookie工具类
 */
@Slf4j
public class CookieUtil {

    private static final String COOKIE_DOMAIN = ".happymmall.com";

    private static final String COOKIE_NAME = "mmall_login_token";

    /**
     * 写cookie到浏览器中
     * @param response
     * @param token
     */
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie cookie = new Cookie(COOKIE_NAME,token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //如果不设置maxage,cookie保存在内存中,只有当前页面有效,不会保存到磁盘中。
        //设置为-1 表示永久
        //单位是秒
        cookie.setMaxAge(60*60*24*365);
        log.info("write cookieName :{},cookieValue:{}",cookie.getName(),cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     *  读cookie
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request){
        Cookie [] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                log.info("read cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("read cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    /**
     *  删除cookie
     * @param request
     * @param response
     */
    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie [] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);//设置成0,代表删除此Cookie
                    log.info("del cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                }
            }
        }
    }
}
