package com.kclogix.apps.management.website.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.apps.management.website.dto.WebsiteDto;
import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
import com.kclogix.apps.management.website.repository.WebsiteRepository;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;

import kainos.framework.core.KainosKey;
import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.utils.KainosDateUtil;
import kainos.framework.utils.KainosStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebsiteService {

	private final WebsiteRepository repository;
	
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<WebsiteDto> selectWebsiteTerminalCode(WebsiteSearchDto paramDto, boolean init) throws Exception {
		return repository.selectWebsiteTerminalCode(paramDto, init);
	}
	
//	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
//	public void uploadPort(List<WebsiteDto> paramList, SessionDto session)throws Exception {
//		String hbl = null;
//		String uuid = null;
//		for (int i = 0; i < paramList.size(); i++) {
//			WebsiteDto dto = paramList.get(i);
//			if(hbl == null || !dto.getHblNo().equals(hbl)) {
//				// BL No 삭제
//				hbl = dto.getHblNo();
//				uuid = KainosDateUtil.getCurrentDay("yyyyMMddHHmmssSSS") + new RandomDataGenerator().nextSecureHexString(3);
//				repository.excelUploadHblNoDelete(dto.getHblNo());
//			}
//			if(!KainosStringUtils.isEmpty(dto.getQuantity())) {
//				String onlyDigits = dto.getQuantity().replaceAll("\\D", ""); // 숫자가 아닌 문자 제거
//				String onlyLetters = dto.getQuantity().replaceAll("\\d", ""); // 숫자 제거
//				dto.setQuantity(onlyDigits);
//				dto.setQuantityType(onlyLetters);
//			}
//			if(KainosStringUtils.isEmpty(dto.getCargo())) {
//				dto.setCargo(dto.getItem());
//			}
//			dto.setUuid(uuid);
//			dto.setCreateUserId(session.getUserId());
//			dto.setUpdateUserId(session.getUserId());
//			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
//				repository.insertWebsiteTerminalCode(dto);
//			}
//		}
//	}
	
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void uploadPort(List<WebsiteDto> paramList, SessionDto session)throws Exception {
		for (int i = 0; i < paramList.size(); i++) {
			WebsiteDto dto = paramList.get(i);
			if(!KainosStringUtils.isEmpty(dto.getQuantity())) {
				String onlyDigits = dto.getQuantity().replaceAll("\\D", ""); // 숫자가 아닌 문자 제거
				String onlyLetters = dto.getQuantity().replaceAll("\\d", ""); // 숫자 제거
				dto.setQuantity(onlyDigits);
				dto.setQuantityType(onlyLetters);
			}
			if(KainosStringUtils.isEmpty(dto.getCargo())) {
				dto.setCargo(dto.getItem());
			}
//			dto.setUuid(uuid);
//			dto.setCreateUserId(session.getUserId());
//			dto.setUpdateUserId(session.getUserId());
//			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
			repository.excleUpdateWebsiteTerminalCode(dto);
//			}
		}
	}
	
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void savePopupPort(WebsiteDto paramDto, SessionDto session)throws Exception {
		List<WebsiteDto> paramList = new ArrayList<>();
		if(paramDto.getTankNo().indexOf(",") > 0) {
			String[] tankNos = paramDto.getTankNo().split(",");
			for (int i = 0; i < tankNos.length; i++) {
				WebsiteDto websiteDto = WebsiteDto.builder().build();
				BeanUtils.copyProperties(paramDto, websiteDto);
				websiteDto.setTankNo(tankNos[i]);
				paramList.add(websiteDto);
			}
		}
		else
			paramList.add(paramDto);
		
		for (int i = 0; i < paramList.size(); i++) {
			long count = repository.selectWebsiteTerminalCount(paramList.get(i));
			if(count > 0) {
				throw new KainosBusinessException("management.website.insert.tankno.duplicated", new String[] {paramList.get(i).getTankNo()});
			}
		}
		
		savePort(paramList, session);
	}

	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void closed(List<WebsiteDto> paramList, SessionDto session)throws Exception {
		for (int i = 0; i < paramList.size(); i++) {
			WebsiteDto dto = paramList.get(i);
			dto.setUpdateUserId(session.getUserId());
			dto.setCreateUserId(session.getUserId());
			repository.closed(dto);
		}
	}
	
	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void savePort(List<WebsiteDto> paramList, SessionDto session)throws Exception {
		for (int i = 0; i < paramList.size(); i++) {
			WebsiteDto dto = paramList.get(i);
			dto.setUpdateUserId(session.getUserId());
			dto.setCreateUserId(session.getUserId());
			if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Insert)) {
				dto.setUuid(repository.getUuid(dto.getHblNo()));
				if(dto.getUuid() == null) dto.setUuid(KainosDateUtil.getCurrentDay("yyyyMMddHHmmssSSS") + new RandomDataGenerator().nextSecureHexString(3));
				repository.insertWebsiteTerminalCode(dto);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Update)) {
				repository.updateWebsiteTerminalCode(dto);
				String concine = (!KainosStringUtils.isEmpty(dto.getConcine()) ? dto.getConcine() : dto.getConcineName().trim());
				log.error(dto.getUuid() + " : " + dto.getHblNo() + " : " + dto.getSeq() + " : " + concine);
			} else if(dto.getJqFlag().equalsIgnoreCase(JqFlag.Delete)) {
				repository.deleteWebsiteTerminalCode(dto.getUuid(), dto.getSeq());
			}
		}
	}

	@Transactional(transactionManager = KainosKey.DBConfig.TransactionManager.Default, rollbackFor = Exception.class)
	public void manualUpdate(List<WebsiteDto> paramList, SessionDto session)throws Exception {
		for (int i = 0; i < paramList.size(); i++) {
			WebsiteDto dto = paramList.get(i);
			repository.excleUpdateWebsiteTerminalCode(dto);
		}
	}

}


