package com.kclogix.apps.management.depot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.kclogix.apps.management.depot.dto.DepotManagementDto;
import com.kclogix.apps.management.depot.dto.DepotMonitorDto;
import com.kclogix.apps.management.depot.service.DepotManagementService;
import com.kclogix.apps.mdm.depot.service.DepotService;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.MessageUtil;
import com.kclogix.common.util.excel.KainosExcelReadHandler;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import kainos.framework.utils.KainosStringUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DepotManagementController {
	
	private final DepotManagementService service;
	private final DepotService depotService;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/management/depotmonitor")
	public ResponseEntity<DepotManagementDto> depotmonitor() throws Exception {
		
		List<DepotMonitorDto> returnData = new ArrayList<>();
		returnData.add(
				DepotMonitorDto.builder()
				.location("DEPOT TOTAL")
				.busanNewPort("WS")
				.busanOldPort("SBCD")
				.build()
				);
		returnData.add(DepotMonitorDto.builder().location("DEPOT CODE").build());
		returnData.add(DepotMonitorDto.builder().location("PARTNER").build());
		returnData.add(DepotMonitorDto.builder().location("USIL").build());
		returnData.add(DepotMonitorDto.builder().location("BESTCHEM").build());
		returnData.add(DepotMonitorDto.builder().location("SPM").build());
		returnData.add(DepotMonitorDto.builder().location("DJD").build());
		returnData.add(DepotMonitorDto.builder().location("DTDZ").build());
		returnData.add(DepotMonitorDto.builder().location("EBEST").build());
		returnData.add(DepotMonitorDto.builder().location("SUMEX").build());
		returnData.add(DepotMonitorDto.builder().location("PROTANK").build());
		returnData.add(DepotMonitorDto.builder().location("BONDY").build());
		returnData.add(DepotMonitorDto.builder().location("LAVISH").build());
		returnData.add(DepotMonitorDto.builder().location("LIDA").build());
		returnData.add(DepotMonitorDto.builder().location("EVERSTAR").build());
		returnData.add(DepotMonitorDto.builder().location("CELERITY").build());
		returnData.add(DepotMonitorDto.builder().location("LUCKYPAL").build());
		return KainosResponseEntity.builder().build()
				.addData(returnData)
				.close();
	}
	
	@GetMapping(value = "/api/management/depot")
	public ResponseEntity<DepotManagementDto> selecDepot(
			@RequestParam(required = false) String hblNo,
			@RequestParam(required = false) String tankNo,
			@RequestParam(required = false) String partner,
			@RequestParam(required = false) String item,
			@RequestParam(required = false) String returnDate,
			@RequestParam(required = false) String returnDepot,
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
		.returnDepot(returnDepot)
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
	
//	@PostMapping(value = "/api/management/depot/date-upload")
//	public ResponseEntity<Void> dateExcelupload(@RequestPart("file") MultipartFile file, @RequestPart("jsonData") DepotManagementDto searchDto, @KainosSession SessionDto session) throws Exception {
//		List<DepotManagementDto.DateExcelUpload> uploadList = new ArrayList<>();
//		try {
//			/* 클라이언트에서 넘어온 MultipartFile 객체 */
//			KainosExcelReadHandler excelReadHandler = KainosExcelReadHandler.builder().startRowNum(1) // 엑셀파일 데이터 시작 로우
//					.excel(file.getInputStream()) // MultipartFile 객체
//					.build(); // 객체 생성
//			excelReadHandler.readExcel() // 엑셀 파일 읽기
//					.getRows() // 데이터 get List
//					.forEach(dataRow -> {
//						try {
//							uploadList.add(excelReadHandler.objectCoyp(dataRow, DepotManagementDto.DateExcelUpload.class));
//						} catch (Exception e) {
//							throw new RuntimeException(e);
//						}
//					});
//			service.dateExcelupload(uploadList, searchDto, session);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new KainosBusinessException("common.system.error");
//		}
//		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
//	}
	
	@PostMapping(value = "/api/management/depot/date-upload")
	public ResponseEntity<DepotManagementDto> dateExcelupload(@RequestPart("file") MultipartFile file, @RequestPart("jsonData") List<DepotManagementDto> paramListDto, @KainosSession SessionDto session) throws Exception {
		try {
			/* 클라이언트에서 넘어온 MultipartFile 객체 */
			KainosExcelReadHandler excelReadHandler = KainosExcelReadHandler.builder().startRowNum(1) // 엑셀파일 데이터 시작 로우
					.excel(file.getInputStream()) // MultipartFile 객체
					.build(); // 객체 생성
			excelReadHandler.readExcel() // 엑셀 파일 읽기
					.getRows() // 데이터 get List
					.forEach(dataRow -> {
						try {
							DepotManagementDto.DateExcelUpload excelData = excelReadHandler.objectCoyp(dataRow, DepotManagementDto.DateExcelUpload.class);
							
							for (int i = 0; i < paramListDto.size(); i++) {
								if(paramListDto.get(i).getTankNo().equalsIgnoreCase(excelData.getTankNo())) {
									if(!KainosStringUtils.isEmpty(excelData.getCleanedDate())) {
										paramListDto.get(i).setCleanedDate(excelData.getCleanedDate());
										paramListDto.get(i).setJqFlag("U");
									}
									if(!KainosStringUtils.isEmpty(excelData.getOutDate())) {
										paramListDto.get(i).setOutDate(excelData.getOutDate());
										paramListDto.get(i).setJqFlag("U");
									}
									break;
								}
							}
							
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return KainosResponseEntity.builder().build().addData(paramListDto).close();
	}
	
	@PostMapping(value = "/api/management/depot/allocation-upload")
	public ResponseEntity<DepotManagementDto> allocationExcelupload(@RequestPart("file") MultipartFile file, @RequestPart("jsonData") List<DepotManagementDto> paramListDto, @KainosSession SessionDto session) throws Exception {
		try {       
			/* 클라이언트에서 넘어온 MultipartFile 객체 */
			KainosExcelReadHandler excelReadHandler = KainosExcelReadHandler.builder().startRowNum(1) // 엑셀파일 데이터 시작 로우
					.excel(file.getInputStream()) // MultipartFile 객체
					.build(); // 객체 생성
			excelReadHandler.readExcel() // 엑셀 파일 읽기
					.getRows() // 데이터 get List
					.forEach(dataRow -> {
						try {
							DepotManagementDto.AllocationExcelUpload excelData = excelReadHandler.objectCoyp(dataRow, DepotManagementDto.AllocationExcelUpload.class);
							for (int i = 0; i < paramListDto.size(); i++) {
								if(paramListDto.get(i).getTankNo().equalsIgnoreCase(excelData.getTankNo())
										&& paramListDto.get(i).getPartner().equalsIgnoreCase(excelData.getPartner())
										&& paramListDto.get(i).getReturnDepot().equalsIgnoreCase(excelData.getReturnDepot())
										) {
									if(!KainosStringUtils.isEmpty(excelData.getAllocation())) {
										paramListDto.get(i).setAllocation(excelData.getAllocation());
										paramListDto.get(i).setJqFlag("U");
									}
								}
							}
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return KainosResponseEntity.builder().build().addData(paramListDto).close();
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
