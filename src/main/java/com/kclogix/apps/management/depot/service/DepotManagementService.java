package com.kclogix.apps.management.depot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.management.depot.dto.DepotManagementDto;
import com.kclogix.apps.management.depot.repository.DepotManagementRepository;
import com.kclogix.common.dto.SessionDto;

import kainos.framework.core.KainosKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepotManagementService {

	private final DepotManagementRepository repository;

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<DepotManagementDto> selectDepotManagement(DepotManagementDto paramDto) throws Exception {
		return repository.selectDepotManagement(paramDto);
	}
	
//	/**
//	 * 
//	 * @param paramDto
//	 * @return
//	 * @throws Exception
//	 */
//	@Transactional(readOnly = true)
//	public List<SelectBoxDto.TermAutocomplete> selectTermAutocomplete() throws Exception {
//		return repository.selectTermAutocomplete();
//	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void saveDepotManagement(List<DepotManagementDto> paramList, SessionDto session) throws Exception {
		for (int i = 0; i < paramList.size(); i++) {
			DepotManagementDto dto = paramList.get(i);
			if(repository.countDepotManagement(dto) == 0)
				repository.insertDepotManagement(dto, session);
			else
				repository.updateDepotManagement(dto, session);
		}
	}
	
}
