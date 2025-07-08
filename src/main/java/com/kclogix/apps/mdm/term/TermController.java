package com.kclogix.apps.mdm.term;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kclogix.apps.mdm.term.dto.TermDto;
import com.kclogix.apps.mdm.term.service.TermService;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.MessageUtil;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TermController {
	
	private final TermService service;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/mdm/term")
	public ResponseEntity<TermDto> selectPartner(@RequestParam(required = false) String name) throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectTerm(TermDto.builder().name(name).build()))
				.close();
	}
	
	@GetMapping(value = "/api/mdm/term/autocomplete")
	public ResponseEntity<SelectBoxDto.TermAutocomplete> selectPartnerAutocomplete() throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectTermAutocomplete())
				.close();
	}
	
	@PostMapping(value = "/api/mdm/term")
	public ResponseEntity<Void> saveTerm(@RequestBody List<TermDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.saveTerm(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/mdm/popup-term")
	public ResponseEntity<TermDto> saveTerm(@RequestBody TermDto param, @KainosSession SessionDto session) throws Exception {
		try {
			service.savePopupTerm(param, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build().addData(param)).close();
	}
	
}
