package com.kclogix.apps.mdm.cargo;

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

import com.kclogix.apps.mdm.cargo.dto.CargoDto;
import com.kclogix.apps.mdm.cargo.service.CargoService;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;
import com.kclogix.common.util.MessageUtil;
import com.kclogix.common.util.excel.KainosExcelReadHandler;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CargoController {
	
	private final CargoService service;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/mdm/cargo")
	public ResponseEntity<CargoDto> selectCargo(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String location,
			@RequestParam(required = false) String depot,
			@RequestParam(required = false) String difficultLevel
			) throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectCargo(CargoDto.builder().name(name).location(location).depot(depot).difficultLevel(difficultLevel).build()))
				.close();
	}
	
	@GetMapping(value = "/api/mdm/cargo/autocomplete")
	public ResponseEntity<SelectBoxDto.CarGoAutoComplete> selectAutocomplete() throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectAutocomplete())
				.close();
	}
	
	@PostMapping(value = "/api/mdm/cargo")
	public ResponseEntity<Void> saveCargo(@RequestBody List<CargoDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.saveCargo(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}

	@PostMapping(value = "/api/mdm/cargo-popup")
	public ResponseEntity<CargoDto.PopupDto> saveCargoPopup(@RequestBody CargoDto.PopupDto paramDto, @KainosSession SessionDto session) throws Exception {
		CargoDto param = CargoDto.builder().build();
		try {
			param.setName(paramDto.getPitemName());
			param.setCargoDate(paramDto.getPitemCargoDate());
			param.setJqFlag(JqFlag.Insert);
			service.saveCargo(List.of(param), session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build().addData(param)).close();
	}
	
	@PostMapping(value = "/api/mdm/cargo/upload")
	public ResponseEntity<Void> excelupload(@RequestPart MultipartFile upload, @KainosSession SessionDto session) throws Exception {
		List<CargoDto> excelData = new ArrayList<>();
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
							excelData.add(excelReadHandler.objectCoyp(dataRow, CargoDto.class));
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
