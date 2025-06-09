package com.kclogix.apps.system.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kainos.framework.core.KainosKey;
import com.kclogix.apps.system.user.dto.ComUserDto;
import com.kclogix.apps.system.user.repository.UserRepository;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

private final UserRepository repository;
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<ComUserDto> selectComUser(ComUserDto paramDto) throws Exception {
		return repository.selectComUser(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void saveComUser(List<ComUserDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			ComUserDto dto = paramList.get(i);
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
				repository.insertComUser(dto, userId);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				repository.updateComUser(dto, userId);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deleteComUser(dto);
			}
		}
	}
	
}
