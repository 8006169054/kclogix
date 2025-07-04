package com.kclogix.apps.system.notices;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kclogix.apps.system.notices.dto.NoticesDto;
import com.kclogix.apps.system.notices.service.NoticesService;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.MessageUtil;

import groovy.util.logging.Slf4j;
import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import kainos.framework.utils.KainosStringUtils;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticesController {
	
	private final NoticesService service;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/system/notices")
	public ResponseEntity<NoticesDto> selectNotices(
			@RequestParam(required = false) String stitle,
			@RequestParam(required = false) String snoticesDate,
			@RequestParam(required = false) String suse
			) throws Exception {
		
		
		NoticesDto paramDto = NoticesDto.builder().title(stitle).use(suse).build();
		if(!KainosStringUtils.isEmpty(snoticesDate)) {
			String[] tmp = snoticesDate.split("~");
			paramDto.setViewStartDate(tmp[0].trim());
			paramDto.setViewEndDate(tmp[1].trim());
		}
		return KainosResponseEntity.builder().build()
				.addData(service.selectNotices(paramDto))
				.close();
	}
	
	@PostMapping(value = "/api/system/notices")
	public ResponseEntity<Void> saveNotices(@RequestBody NoticesDto paramDto, @KainosSession SessionDto session) throws Exception {
		try {
			if(!KainosStringUtils.isEmpty(paramDto.getNoticesDate())) {
				String[] tmp = paramDto.getNoticesDate().split("~");
				paramDto.setViewStartDate(tmp[0].trim());
				paramDto.setViewEndDate(tmp[1].trim());
			}
			service.saveNotices(paramDto, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@DeleteMapping(value = "/api/system/notices")
	public ResponseEntity<Void> deleteNotices(@RequestBody List<NoticesDto> paramDto) throws Exception {
		try {
			service.deleteNotices(paramDto);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
}
