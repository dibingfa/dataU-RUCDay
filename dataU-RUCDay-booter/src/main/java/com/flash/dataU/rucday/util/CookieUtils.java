package com.flash.dataU.rucday.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * .
 *
 * @author sunyiming (sunyiming170619@credithc.com)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月26日 09时29分
 */
public class CookieUtils {

    public static final String USER_COOKIE = "user";

    /**
     * 从request获取某一cookie的值
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        return getCookie(cookies, name);
    }

    /**
     * 从cookie数组中获取某一cookie的值
     */
    public static String getCookie(Cookie[] cookies, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie:cookies) {
            if (name.equals(cookie.getName())) {
                String value = cookie.getValue();
                //value = decodeCookie(value);
                return value;
            }
        }
        return null;
    }

    /**
     * 设置cookie
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        //value = encodeCookie(value);
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static String encodeCookie(String value){
        value = value.replace("\"", "@slash@");
        value = value.replace(",", "@comma@");
        return value;
    }

    private static String decodeCookie(String value){
        value = value.replace( "@slash@", "\"");
        value = value.replace( "@comma@", ",");
        return value;
    }

}
