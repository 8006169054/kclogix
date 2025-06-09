package com.kclogix.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

	public static Cookie[] getAllCookies() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getCookies();
	}

	public static Cookie getCookie(String cookieKey) {
		Cookie[] cookies = getAllCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieKey)) {
					return cookie;
				}
			}
		}
		return null;
	}
}
