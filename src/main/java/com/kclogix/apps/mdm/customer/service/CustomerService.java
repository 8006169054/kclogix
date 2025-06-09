package com.kclogix.apps.mdm.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kainos.framework.core.KainosKey;
import com.kclogix.apps.mdm.customer.dto.CustomerDto;
import com.kclogix.apps.mdm.customer.repository.CustomerRepository;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository repository;

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CustomerDto> selectCustomer(CustomerDto paramDto) throws Exception {
		return repository.selectCustomer(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<SelectBoxDto.CustomerAutoComplete> selectCustomerAutocomplete() throws Exception {
		return repository.selectCustomerAutocomplete();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void saveCustomer(List<CustomerDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			CustomerDto dto = paramList.get(i);
			dto.setCreateUserId(userId);
			dto.setUpdateUserId(userId);
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
				repository.insertCustomer(dto);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				repository.updateCustomer(dto);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deleteCustomer(dto);
			}
		}
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void excelupload(List<CustomerDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			CustomerDto dto = paramList.get(i);
			dto.setCreateUserId(userId);
			dto.setUpdateUserId(userId);
			repository.insertCustomer(dto);
		}
	}
}
