package com.kclogix.apps.common.auth;

import java.util.Locale;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kainos.framework.core.servlet.KainosResponseEntity;
import com.kclogix.apps.common.auth.service.AuthService;
import com.kclogix.common.dto.SessionDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService service;
    private final LocaleResolver localeResolver;
    
	@GetMapping(value = "/open/dblogin")
	public ResponseEntity<SessionDto> dbLogin(@RequestParam(required = true) String id, @RequestParam(required = true) String password) throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.dbLogin(id, password))
				.close();
	}
	
	@GetMapping(value = "/api/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		localeResolver.setLocale(request, response, Locale.ENGLISH);
		service.logout();
		return KainosResponseEntity.noneData();
	}
}
