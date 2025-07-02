package com.kclogix.apps.system.notices.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.system.notices.dto.NoticesDto;
import com.kclogix.apps.system.notices.repository.NoticesRepository;
import com.kclogix.common.dto.SessionDto;

import kainos.framework.core.KainosKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticesService {

private final NoticesRepository repository;
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<NoticesDto> selectNotices(NoticesDto paramDto) throws Exception {
		return repository.selectNotices(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void saveNotices(NoticesDto paramDto, SessionDto session) throws Exception {
		String userId = session.getUserId();
		if(paramDto.getId() == null)
			repository.insertNotices(paramDto, userId);
		else
			repository.updateNotices(paramDto, userId);
	}
	
}
