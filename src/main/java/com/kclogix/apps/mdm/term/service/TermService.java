package com.kclogix.apps.mdm.term.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.mdm.term.dto.TermDto;
import com.kclogix.apps.mdm.term.repository.TermRepository;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.CodeGenerationUtil;
import com.kclogix.common.util.JqFlag;

import kainos.framework.core.KainosKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermService {

	private final TermRepository repository;

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<TermDto> selectTerm(TermDto paramDto) throws Exception {
		return repository.selectTerm(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<SelectBoxDto.TermAutocomplete> selectTermAutocomplete() throws Exception {
		return repository.selectTermAutocomplete();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void saveTerm(List<TermDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			TermDto dto = paramList.get(i);
			dto.setCreateUserId(userId);
			dto.setUpdateUserId(userId);
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
				dto.setId(CodeGenerationUtil.createCode("TR"));
				repository.insertTerm(dto, session);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				repository.updateTerm(dto, session);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deleteTerm(dto);
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
	public void savePopupTerm(TermDto param, SessionDto session) throws Exception {
		param.setId(CodeGenerationUtil.createCode("TR"));
		repository.insertTerm(param, session);
	}
}
