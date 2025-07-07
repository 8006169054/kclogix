package com.kclogix.apps.mdm.terminal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kainos.framework.core.KainosKey;
import com.kclogix.apps.mdm.terminal.dto.TerminalDto;
import com.kclogix.apps.mdm.terminal.repository.TerminalRepository;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TerminalService {

	private final TerminalRepository repository;
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<TerminalDto> selectTerminal(TerminalDto paramDto) throws Exception {
		return repository.selectTerminal(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void excelupload(List<TerminalDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			TerminalDto dto = paramList.get(i);
			if(selectTerminal(dto).size() == 0 ) {
				repository.insertTerminal(dto, userId);
			}else {
				repository.updateTerminal(dto, userId);
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
	public void saveTerminal(List<TerminalDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			TerminalDto dto = paramList.get(i);
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
				repository.insertTerminal(dto, userId);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				repository.updateTerminal(dto, userId);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deleteTerminal(dto);
			}
		}
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<SelectBoxDto.TerminalAutoComplete> selectAutocomplete() throws Exception {
		return repository.selectAutocomplete();
	}
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<SelectBoxDto.TerminalAutoComplete> selectParkingLotCodeAutocomplete() throws Exception {
		return repository.selectParkingLotCodeAutocomplete();
	}
	
}
