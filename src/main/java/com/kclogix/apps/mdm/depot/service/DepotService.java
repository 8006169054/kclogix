package com.kclogix.apps.mdm.depot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.mdm.depot.dto.DepotDto;
import com.kclogix.apps.mdm.depot.repository.DepotRepository;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;

import kainos.framework.core.KainosKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepotService {

	private final DepotRepository repository;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> selectMonitorDepotCode() throws Exception {
		return repository.selectMonitorDepotCode();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<String> selectMonitorColNames() throws Exception {
		return repository.selectMonitorColNames();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<DepotDto> selectDepot(DepotDto paramDto) throws Exception {
		return repository.selectDepot(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<SelectBoxDto.DepotAutocomplete> selectDepotAutocomplete() throws Exception {
		return repository.selectDepotAutocomplete();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void saveDepot(List<DepotDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			DepotDto dto = paramList.get(i);
			dto.setCreateUserId(userId);
			dto.setUpdateUserId(userId);
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
				repository.insertDepot(dto, session);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				repository.updateDepot(dto, session);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deleteDepot(dto);
			}
		}
	}
	
}
