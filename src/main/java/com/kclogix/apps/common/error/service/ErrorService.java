package com.kclogix.apps.common.error.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import kainos.framework.core.KainosKey;
import kainos.framework.core.session.KainosSessionContext;
import com.kclogix.apps.common.error.repository.ErrorRepository;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.entity.ErrorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorService {

	private final ErrorRepository repository;
	private final KainosSessionContext kainosSession;
	
	/**
	 * 
	 * @param throwable
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Primary, rollbackFor = Exception.class)
	public void insertErrorLog(Throwable throwable) {
		try {
			SessionDto dto = kainosSession.getSession(SessionDto.class);
			if(dto == null) dto = SessionDto.builder().name("SYSTEM").build();
			HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			repository.insertErrorLog(ErrorLog.builder()
					.log(ExceptionUtils.getStackTrace(throwable))
					.createUserId(dto.getName())
					.url(servletRequest.getRequestURI())
					.build());
		} catch (Exception e) {
			log.error("{}", e);
		}
	}
	
	
}
