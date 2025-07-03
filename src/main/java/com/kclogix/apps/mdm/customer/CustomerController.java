package com.kclogix.apps.mdm.customer;

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
import com.kclogix.apps.mdm.customer.dto.CustomerDto;
import com.kclogix.apps.mdm.customer.service.CustomerService;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;
import com.kclogix.common.util.MessageUtil;
import com.kclogix.common.util.excel.KainosExcelReadHandler;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CustomerController {
	
	private final CustomerService service;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/mdm/customer")
	public ResponseEntity<CustomerDto> selectCustomer(@RequestParam(required = false) String name) throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectCustomer(CustomerDto.builder().name(name).build()))
				.close();
	}
	
	@GetMapping(value = "/api/mdm/customer/autocomplete")
	public ResponseEntity<SelectBoxDto.CustomerAutoComplete> selectCustomerAutocomplete() throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectCustomerAutocomplete())
				.close();
	}
	
	@PostMapping(value = "/api/mdm/customer")
	public ResponseEntity<Void> saveCustomer(@RequestBody List<CustomerDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.saveCustomer(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/mdm/customer-popup")
	public ResponseEntity<Void> saveCustomerPopup(@RequestBody CustomerDto.PopupDto paramDto, @KainosSession SessionDto session) throws Exception {
		try {
			
			CustomerDto param = CustomerDto.builder()
					.code(paramDto.getPconcineCode())
					.name(paramDto.getPconcineName())
					.pic(paramDto.getPconcinePic())
					.email(paramDto.getPconcineEmail())
					.jqFlag(JqFlag.Insert)
					.build();
			
//			if(service.selectCustomer(param).size() > 0)
//				param.setJqFlag(JqFlag.Insert);
//			else
//				param.setJqFlag(JqFlag.Update);
			
			service.saveCustomer(List.of(param), session);
			
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/mdm/customer/upload")
	public ResponseEntity<Void> excelupload(@RequestPart MultipartFile upload, @KainosSession SessionDto session) throws Exception {
		List<CustomerDto> excelData = new ArrayList<>();
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
							excelData.add(excelReadHandler.objectCoyp(dataRow, CustomerDto.class));
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
	
}
