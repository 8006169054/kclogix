package com.kclogix.apps.management.depot.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.management.depot.dto.DepotManagementDto;
import com.kclogix.apps.management.depot.repository.DepotManagementRepository;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;

import kainos.framework.core.KainosKey;
import kainos.framework.utils.KainosStringUtils;
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
	
	/**
	 * 
	 * @param uploadList
	 * @param session
	 * @throws Exception
	 */
//	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
//	public void allocationExcelupload(List<DepotManagementDto.AllocationExcelUpload> uploadList, DepotManagementDto searchDto, SessionDto session) throws Exception {
//		for (int i = 0; i < uploadList.size(); i++) {
//			DepotManagementDto.AllocationExcelUpload dto = uploadList.get(i);
//			searchDto.setTankNo(dto.getTankNo());
//			searchDto.setPartner(dto.getPartner());
//			searchDto.setReturnDepot(dto.getReturnDepot());
//			List<DepotManagementDto> returnData = repository.selectDepotManagement(searchDto);
//			if(returnData.size() == 1) {
//				repository.dateExcelupload(returnData.get(0), dto, session);
//			}
//		}
//	}
	
	/**
	 * 
	 * @param uploadList
	 * @param session
	 * @throws Exception
	 */
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void dateExcelupload(List<DepotManagementDto.DateExcelUpload> uploadList, DepotManagementDto searchDto, SessionDto session) throws Exception {
		for (int i = 0; i < uploadList.size(); i++) {
			DepotManagementDto.DateExcelUpload dto = uploadList.get(i);
			
			if(!KainosStringUtils.isEmpty(dto.getCleanedDate())) {
				if(!isValidDateFormat(dto.getCleanedDate(), "yyyy-MM-dd")) continue;
			}
			if(!KainosStringUtils.isEmpty(dto.getOutDate())) {
				if(!isValidDateFormat(dto.getOutDate(), "yyyy-MM-dd")) continue;
			}
			
			searchDto.setTankNo(dto.getTankNo());
			List<DepotManagementDto> returnData = repository.selectDepotManagement(searchDto);
			if(returnData.size() == 1) {
				repository.dateExcelupload(returnData.get(0), dto, session);
			}
		}
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
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				if(repository.countDepotManagement(dto) == 0)
					repository.insertDepotManagement(dto, session);
				else
					repository.updateDepotManagement(dto, session);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deleteDepotManagemen(dto);
			}
		}
	}

	/**
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public boolean isValidDateFormat(String dateStr, String format) {
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        sdf.setLenient(false); // 엄격한 포맷 검사
	        sdf.parse(dateStr);    // 파싱 시도
	        return true;
	    } catch (ParseException e) {
	        return false;
	    }
	}
}
