package com.kclogix.apps.management.arrival.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.management.arrival.repository.ArrivalRepository;
import com.kclogix.apps.management.website.dto.WebsiteDto;
import com.kclogix.apps.management.website.dto.WebsiteSearchDto;

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
	
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void arrivalNoticeSendMail(WebsiteSearchDto paramDto)throws Exception {
		repository.arrivalNoticeSendMail(paramDto);
	}
	
}
