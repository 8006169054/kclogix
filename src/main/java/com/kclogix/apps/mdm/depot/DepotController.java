package com.kclogix.apps.mdm.depot;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kclogix.apps.mdm.depot.dto.DepotDto;
import com.kclogix.apps.mdm.depot.service.DepotService;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.MessageUtil;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DepotController {
	
	private final DepotService service;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/mdm/depot")
	public ResponseEntity<DepotDto> selecDepot(
			@RequestParam(required = false) String depotCode,
			@RequestParam(required = false) String location,
			@RequestParam(required = false) String depotName,
			@RequestParam(required = false) String address,
			@RequestParam(required = false) String picName,
			@RequestParam(required = false) String picTel,
			@RequestParam(required = false) String picEmail,
			@RequestParam(required = false) String operationRemark
			) throws Exception {
		
		
		DepotDto paramDto = DepotDto.builder()
		.depotCode(depotCode)
		.location(location)
		.depotNameEn(depotName)
		.address(address)
		.picEmail(picEmail)
		.picName(picName)
		.picTel(picTel)
		.operationRemark(operationRemark)
		.build();
		
		return KainosResponseEntity.builder().build()
				.addData(service.selectDepot(paramDto))
				.close();
	}
	
	
	@GetMapping(value = "/api/mdm/depot/autocomplete")
	public ResponseEntity<SelectBoxDto.DepotAutocomplete> selectDepotAutocomplete() throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectDepotAutocomplete())
				.close();
	}
	
	@PostMapping(value = "/api/mdm/depot")
	public ResponseEntity<Void> saveTerm(@RequestBody List<DepotDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.saveDepot(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
//	@PostMapping(value = "/api/mdm/popup-term")
//	public ResponseEntity<TermDto> saveTerm(@RequestBody TermDto param, @KainosSession SessionDto session) throws Exception {
//		try {
//			service.savePopupTerm(param, session);
//		} catch (KainosBusinessException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new KainosBusinessException("common.system.error");
//		}
//		return message.getInsertMessage(KainosResponseEntity.builder().build().addData(param)).close();
//	}
	
}
