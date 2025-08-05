package com.kclogix.apps.mdm.partner.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kainos.framework.core.KainosKey;
import com.kclogix.apps.mdm.partner.dto.PartnerDto;
import com.kclogix.apps.mdm.partner.repository.PartnerRepository;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnerService {

	private final PartnerRepository repository;

	/**
	 * 
	 * @param partnerName
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<String> selectMonitorColModels(String partnerName) throws Exception {
		return repository.selectMonitorColNames(partnerName);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<PartnerDto> selectPartner(PartnerDto paramDto) throws Exception {
		return repository.selectPartner(paramDto, false);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<SelectBoxDto.PartnerAutocomplete> selectPartnerAutocomplete() throws Exception {
		return repository.selectPartnerAutocomplete();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void savePartner(List<PartnerDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			PartnerDto dto = paramList.get(i);
			dto.setCreateUserId(userId);
			dto.setUpdateUserId(userId);
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
				repository.insertPartner(dto);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				repository.updatePartner(dto);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deletePartner(dto);
			}
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public String selectPartnerCode(String name) throws Exception {
		return repository.selectPartnerCode(name);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void excelupload(List<PartnerDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			PartnerDto dto = paramList.get(i);
			
//			if(selectPartner(dto).size() == 0 ) 
//				repository.insertCargo(dto, userId);
//			else 
//				repository.uploadUpdateCargo(dto, userId);
			
		}
	}
}
