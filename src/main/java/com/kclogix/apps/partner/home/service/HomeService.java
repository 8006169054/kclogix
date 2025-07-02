package com.kclogix.apps.partner.home.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
import com.kclogix.apps.partner.home.dto.HomeDto;
import com.kclogix.apps.partner.home.dto.HomeExcelDownDto;
import com.kclogix.apps.partner.home.repository.HomeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {

	private final HomeRepository repository;
	
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<HomeDto> selectWebsiteTerminalCode(WebsiteSearchDto paramDto) throws Exception {
		return repository.selectWebsiteTerminalCode(paramDto);
	}
	
	@Transactional(readOnly = true)
	public String selectWebsiteTerminalCodeGridCol(String userId) throws Exception {
		return repository.selectWebsiteTerminalCodeGridCol(userId);
	}
	
	@Transactional(readOnly = true)
	public List<HomeExcelDownDto> selectWebsiteTerminalCodeExcel(WebsiteSearchDto paramDto) throws Exception {
		return repository.selectWebsiteTerminalCodeExcel(paramDto);
	}
}
