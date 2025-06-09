package com.kclogix.apps.history.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.history.dto.HistoryDto;
import com.kclogix.apps.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

	private final HistoryRepository repository;
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<HistoryDto.CargoDto> selectCargo(HistoryDto.CargoDto paramDto) throws Exception {
		return repository.selectCargo(paramDto);
	}
}
