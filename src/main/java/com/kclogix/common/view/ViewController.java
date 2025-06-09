package com.kclogix.common.view;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kainos.framework.core.KainosKey;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.KainosSessionContext;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ViewController {

	private final String contextPath = "view";
    @Value("${kainos.security.sameSite:#{null}}")
    private String sameSite;
    @Value("${kainos.security.secure:#{null}}")
    private boolean secure;
    @Value("${kainos.security.httpOnly:#{null}}")
    private boolean httpOnly;
    private final KainosSessionContext kainosSession;
    private final LocaleResolver localeResolver;
    
	/**
	 * Index 페이지
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/")
    public String index(HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		String cacheControl = CacheControl.noCache().getHeaderValue();
		response.addHeader("Cache-Control", cacheControl);
		response.addHeader("locale", locale.getLanguage());
		KainosKey.Jwt.Code code = kainosSession.resolveToken(request);
		if(code == null || (KainosKey.Jwt.Code.ACCESS != code)) return "html/apps/login/authlogin";
		return "html/index";
    }
	
	@GetMapping(value = "/open/lang")
    public ResponseEntity<Void> lang(@RequestParam String lang, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(lang.equalsIgnoreCase("EN")) 
			localeResolver.setLocale(request, response, Locale.ENGLISH);
		else
			localeResolver.setLocale(request, response, new Locale("zh", "CN"));
		return KainosResponseEntity.noneData();
    }
	
	/**
	 * 메인 페이지
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = contextPath + "/**")
    public String view(HttpServletResponse response,  HttpServletRequest request, Locale locale) throws Exception {
		/** URL 주소 창 직접 접근  */
		if(request.getHeader("REFERER") == null) return "html/index";
		
		String cacheControl = CacheControl.noCache().getHeaderValue();
		response.addHeader("Cache-Control", cacheControl);
		String htmlPath = request.getRequestURI().split(contextPath)[1];
		
		if(PagePaths.links.containsKey(htmlPath.toUpperCase())) 
			htmlPath = PagePaths.links.get(htmlPath.toUpperCase()).getLinkPath();
		
//		setLocale(request, response, locale);
		// OPEN << 권한 체크 패스
		
//		if(sessionCheck(request))
			return "html/apps" + htmlPath;
//		else
//			return "html/apps/login/authlogin";
    }
	
	private void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
//		Cookie langCookie = CookieUtil.getCookie("kainos-lang");
//		Cookie cookie = null;
//		if(langCookie != null && langCookie.getValue().equals("zh")) {
//			localeResolver.setLocale(request, response, Locale.CHINESE);
//	       	cookie = new Cookie("kainos-lang", "zh");
//	    }
//		else {
//			localeResolver.setLocale(request, response, Locale.ENGLISH);
//			cookie = new Cookie("kainos-lang", "en");
//	    }
//		cookie.setPath("/");
//        response.addCookie(cookie);
	}
	
	private boolean sessionCheck(HttpServletRequest request) throws Exception {
		KainosKey.Jwt.Code code = kainosSession.resolveToken(request);
		if(code == null || (KainosKey.Jwt.Code.ACCESS != code)) return false;
		return true;
	}
}
