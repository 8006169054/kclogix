package com.kclogix.apps.management.depot;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kclogix.apps.management.depot.dto.DepotManagementDto;
import com.kclogix.apps.management.depot.service.DepotManagementService;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.MessageUtil;
import com.kclogix.common.util.excel.KainosExcelReadHandler;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DepotManagementController {
	
	private final DepotManagementService service;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/management/depot")
	public ResponseEntity<DepotManagementDto> selecDepot(
			@RequestParam(required = false) String hblNo,
			@RequestParam(required = false) String tankNo,
			@RequestParam(required = false) String partner,
			@RequestParam(required = false) String item,
			@RequestParam(required = false) String returnDate,
			@RequestParam(required = false) String cleanedDate,
			@RequestParam(required = false) String outDate,
			@RequestParam(required = false) String allocation,
			@RequestParam(required = false) String allocationType,
			@RequestParam(required = false) String remark
			) throws Exception {
		
		DepotManagementDto paramDto = DepotManagementDto.builder()
		.hblNo(hblNo)
		.tankNo(tankNo)
		.partner(partner)
		.item(item)
		.returnDate(returnDate)
		.cleanedDate(cleanedDate)
		.outDate(outDate)
		.allocation(allocation)
		.allocationType(allocationType)
		.remark(remark)
		.build();
		
		return KainosResponseEntity.builder().build()
				.addData(service.selectDepotManagement(paramDto))
				.close();
	}
	
//	@GetMapping(value = "/api/mdm/term/autocomplete")
//	public ResponseEntity<SelectBoxDto.TermAutocomplete> selectPartnerAutocomplete() throws Exception {
//		return KainosResponseEntity.builder().build()
//				.addData(service.selectTermAutocomplete())
//				.close();
//	}
	
	@PostMapping(value = "/api/management/depot")
	public ResponseEntity<Void> saveDepotManagement(@RequestBody List<DepotManagementDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.saveDepotManagement(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/management/depot/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> excelupload(@RequestPart("file") MultipartFile file, @RequestPart("jsonData") DepotManagementDto paramDto, @KainosSession SessionDto session) throws Exception {
		try {       
			/* 클라이언트에서 넘어온 MultipartFile 객체 */
			KainosExcelReadHandler excelReadHandler = KainosExcelReadHandler.builder().startRowNum(1) // 엑셀파일 데이터 시작 로우
					.excel(file.getInputStream()) // MultipartFile 객체
					.build(); // 객체 생성
			excelReadHandler.readExcel() // 엑셀 파일 읽기
					.getRows() // 데이터 get List
					.forEach(dataRow -> {
						try {
							System.out.println(dataRow);
//							/* 주의 엑셀 파일에 빈 데이터 체크 필요 */
//							excelData.add(excelReadHandler.objectCoyp(dataRow, CargoDto.class));
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
}
