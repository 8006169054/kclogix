package com.kclogix.apps.mdm.terminal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import com.kclogix.apps.mdm.terminal.dto.TerminalDto;
import com.kclogix.apps.mdm.terminal.service.TerminalService;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.MessageUtil;
import com.kclogix.common.util.excel.KainosExcelReadHandler;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TerminalController {
	
	private final TerminalService service;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/mdm/terminal")
	public ResponseEntity<TerminalDto> selectTerminal(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String code,
			@RequestParam(required = false) String region
			) throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectTerminal(TerminalDto.builder().name(name).code(code).region(region).build()))
				.close();
	}
	
	@PostMapping(value = "/api/mdm/terminal")
	public ResponseEntity<Void> saveTerminal(@RequestBody List<TerminalDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.saveTerminal(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/mdm/terminal/upload")
	public ResponseEntity<Void> excelupload(@RequestPart MultipartFile upload, @KainosSession SessionDto session) throws Exception {
		List<TerminalDto> excelData = new ArrayList<>();
		try {       
			/* 클라이언트에서 넘어온 MultipartFile 객체 */
			KainosExcelReadHandler excelReadHandler = KainosExcelReadHandler.builder().startRowNum(1) // 엑셀파일 데이터 시작 로우
					.excel(upload.getInputStream()) // MultipartFile 객체
					.build(); // 객체 생성
			excelReadHandler.readExcel() // 엑셀 파일 읽기
					.getRows() // 데이터 get List
					.forEach(dataRow -> {
						try {
							/* 주의 엑셀 파일에 빈 데이터 체크 필요 */
							excelData.add(excelReadHandler.objectCoyp(dataRow, TerminalDto.class));
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
			
			service.excelupload(excelData, session);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	
	@GetMapping(value = "/api/mdm/terminal/autocomplete")
	public ResponseEntity<SelectBoxDto.TerminalAutoComplete> selectAutocomplete() throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectAutocomplete())
				.close();
	}
	
	@GetMapping(value = "/api/mdm/terminal/autocompleteParkingLotCode")
	public ResponseEntity<SelectBoxDto.TerminalAutoComplete> selectParkingLotCodeAutocomplete() throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectParkingLotCodeAutocomplete())
				.close();
	}
}
