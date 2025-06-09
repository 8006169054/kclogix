package com.kclogix.apps.mdm.cargo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kainos.framework.core.KainosKey;
import com.kclogix.apps.mdm.cargo.dto.CargoDto;
import com.kclogix.apps.mdm.cargo.repository.CargoRepository;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CargoService {

	private final CargoRepository repository;
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CargoDto> selectCargo(CargoDto paramDto) throws Exception {
		return repository.selectCargo(paramDto);
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<SelectBoxDto.CarGoAutoComplete> selectAutocomplete() throws Exception {
		return repository.selectAutocomplete();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void saveCargo(List<CargoDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			CargoDto dto = paramList.get(i);
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
				repository.insertCargo(dto, userId);
				repository.insertCargoHistory(dto, userId);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				repository.updateCargo(dto, userId);
				repository.insertCargoHistory(dto, userId);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deleteCargo(dto);
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
	public void excelupload(List<CargoDto> paramList, SessionDto session) throws Exception {
		String userId = session.getUserId();
		for (int i = 0; i < paramList.size(); i++) {
			CargoDto dto = paramList.get(i);
			if(dto.getCleaningCost().equalsIgnoreCase("X")) dto.setCleaningCost("");
			if(dto.getDifficultLevel().equalsIgnoreCase("X")) dto.setDifficultLevel("");
			
			dto.setCleaningCost((dto.getCleaningCost().replaceAll(".0", "")));
			dto.setDifficultLevel((dto.getDifficultLevel().replaceAll(".0", "")));
			
			if(selectCargo(dto).size() == 0 ) 
				repository.insertCargo(dto, userId);
			else 
				repository.uploadUpdateCargo(dto, userId);
			
			repository.insertCargoHistory(dto, userId);
		}
	}
}
