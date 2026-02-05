package com.kclogix.apps.management.arrival.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.management.arrival.repository.ArrivalRepository;
import com.kclogix.apps.management.website.dto.WebsiteDto;
import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
import com.kclogix.common.dto.SessionDto;

import kainos.framework.core.KainosKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArrivalService {

	private final ArrivalRepository repository;
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<WebsiteDto> selectArrivalnotice(WebsiteSearchDto paramDto, boolean init) throws Exception {
		return repository.selectArrivalnotice(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void arrivalNoticeSendMail(WebsiteSearchDto paramDto)throws Exception {
		repository.arrivalNoticeSendMail(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void updateTerminalCode(WebsiteSearchDto paramDto) throws Exception {
		repository.updateTerminalCode(paramDto);
	}
	
	/**
	 * 
	 * @param paramList
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void arrivalnoticeSave(List<WebsiteDto> paramList, SessionDto session)throws Exception {
		repository.arrivalnoticeSave(paramList, session);
	}
}
