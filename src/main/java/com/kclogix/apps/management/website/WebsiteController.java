package com.kclogix.apps.management.website;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kclogix.apps.management.website.dto.WebsiteDto;
import com.kclogix.apps.management.website.dto.WebsiteExcelReadDto;
import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
import com.kclogix.apps.management.website.service.WebsiteService;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.JqFlag;
import com.kclogix.common.util.MessageUtil;
import com.kclogix.common.util.excel.GridRowSpenHandler;
import com.kclogix.common.util.excel.KainosExcelReadHandler;
import com.kclogix.common.util.excel.KainosExcelWriteHandler;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WebsiteController {
	
	private final WebsiteService service;
	private final MessageUtil message;
	private final GridRowSpenHandler handler;
	
//	@GetMapping(value = "/api/management/website-terminal-code-init")
//	public ResponseEntity<WebsiteDto> selectWebsiteTerminalCodeInit() throws Exception {
//		List<WebsiteDto> PortList = service.selectWebsiteTerminalCode(null, true);
//		return KainosResponseEntity.builder().build()
//				.addData(handler.GenerationRowSpen(PortList, WebsiteDto.class))
//				.close();
//	}
	
	@GetMapping(value = "/api/management/website-terminal-code")
	public ResponseEntity<WebsiteDto> selectWebsiteTerminalCode(
			@RequestParam(required = false) String sprofitDate, 
			@RequestParam(required = false) String spartner, 
			@RequestParam(required = false) String stankNo, 
			@RequestParam(required = false) String sitem, 
			@RequestParam(required = false) String scargo, 
			@RequestParam(required = false) String shblNo, 
			@RequestParam(required = false) String smblNo, 
			@RequestParam(required = false) String spol, 
			@RequestParam(required = false) String spod, 
			@RequestParam(required = false) String sata, 
			@RequestParam(required = false) String serd, 
			@RequestParam(required = false) String sreturnDate, 
			@RequestParam(required = false) String sreturnDepot, 
			@RequestParam(required = false) String sdemRcvd,
			@RequestParam(required = false) String sdemRcvdSelect,
			@RequestParam(required = false) String sshipmentStatus,
			@RequestParam(required = false) Boolean initSearch
			
		) throws Exception {
		
		WebsiteSearchDto paramDto = WebsiteSearchDto.builder()
		.profitDate(sprofitDate)
		.partner(spartner)
		.tankNo(stankNo)
		.item(sitem)
		.cargo(scargo)
		.hblNo(shblNo)
		.mblNo(smblNo)
		.pol(spol)
		.pod(spod)
		.ata(sata)
		.erd(serd)
		.returnDate(sreturnDate)
		.returnDepot(sreturnDepot)
		.demRcvd(sdemRcvd)
		.demRcvdSelect(sdemRcvdSelect)
		.shipmentStatus(sshipmentStatus)
		.build();
		List<WebsiteDto> PortList = service.selectWebsiteTerminalCode(paramDto, initSearch);
		return KainosResponseEntity.builder().build()
				.addData(handler.GenerationRowSpen(PortList, WebsiteDto.class))
				.close();
	}
	
	@PostMapping(value = "/api/management/closed")
	public ResponseEntity<Void> closed(@RequestBody List<WebsiteDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.closed(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/management/save-port")
	public ResponseEntity<Void> savePort(@RequestBody List<WebsiteDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.savePort(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/management/save-popup-port")
	public ResponseEntity<Void> savePopupPort(@RequestBody WebsiteDto paramDto, @KainosSession SessionDto session) throws Exception {
		try {
			paramDto.setJqFlag(JqFlag.Insert);
			service.savePopupPort(paramDto, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/management/upload-port")
	public ResponseEntity<Void> uploadPort(@RequestBody List<WebsiteDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.uploadPort(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	
	@PostMapping(value = "/api/management/excel-upload")
	public ResponseEntity<WebsiteExcelReadDto> excelupload(@RequestPart MultipartFile upload) throws Exception {
		List<WebsiteExcelReadDto> excelData = new ArrayList<>();
		try {       
			/* 클라이언트에서 넘어온 MultipartFile 객체 */
			KainosExcelReadHandler excelReadHandler = KainosExcelReadHandler.builder().startRowNum(5) // 엑셀파일 데이터 시작 로우
					.excel(upload.getInputStream()) // MultipartFile 객체
					.rowSpan(true)
					.build(); // 객체 생성
			excelReadHandler.readExcel() // 엑셀 파일 읽기
					.getRows() // 데이터 get List
					.forEach(dataRow -> {
						try {
							/* 주의 엑셀 파일에 빈 데이터 체크 필요 */
							excelData.add(excelReadHandler.objectCoyp(dataRow, WebsiteExcelReadDto.class));
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
			
			
			/* 필수 처리 */
			excelReadHandler.objectCoypClose();
			excelReadHandler.rowSapnCoyp(excelData);
			excelReadHandler.customFunctionCall(excelData);
			
			List<WebsiteExcelReadDto> removeIndex = new ArrayList<> ();
			for (int i = 0; i < excelData.size(); i++)
				if(excelData.get(i).getHblNo() == null || excelData.get(i).getHblNo().equalsIgnoreCase("")) removeIndex.add(excelData.get(i));
			
			if(removeIndex.size() > 0) excelData.removeAll(removeIndex);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return KainosResponseEntity.builder().build()
				.addData(handler.GenerationRowSpen(excelData, WebsiteExcelReadDto.class))
				.close();
	}
	
	@GetMapping(value = "/api/management/website-terminal-code-exceldown")
	public ResponseEntity<InputStreamResource> exceldown(
			@RequestParam(required = false) String sprofitDate, 
			@RequestParam(required = false) String spartner, 
			@RequestParam(required = false) String stankNo, 
			@RequestParam(required = false) String sitem, 
			@RequestParam(required = false) String scargo, 
			@RequestParam(required = false) String shblNo, 
			@RequestParam(required = false) String smblNo, 
			@RequestParam(required = false) String spol, 
			@RequestParam(required = false) String spod, 
			@RequestParam(required = false) String sata, 
			@RequestParam(required = false) String serd, 
			@RequestParam(required = false) String sreturnDate, 
			@RequestParam(required = false) String sreturnDepot, 
			@RequestParam(required = false) String sdemRcvd,
			@RequestParam(required = false) String sdemRcvdSelect,
			@RequestParam(required = false) String sshipmentStatus,
			@RequestParam(required = false) Boolean initSearch
		) throws Exception {
		
		WebsiteSearchDto paramDto = WebsiteSearchDto.builder()
				.profitDate(sprofitDate)
				.partner(spartner)
				.tankNo(stankNo)
				.item(sitem)
				.cargo(scargo)
				.hblNo(shblNo)
				.mblNo(smblNo)
				.pol(spol)
				.pod(spod)
				.ata(sata)
				.erd(serd)
				.returnDate(sreturnDate)
				.returnDepot(sreturnDepot)
				.demRcvd(sdemRcvd)
				.demRcvdSelect(sdemRcvdSelect)
				.shipmentStatus(sshipmentStatus)
				.build();
		
		List<WebsiteDto> PortList = service.selectWebsiteTerminalCode(paramDto, initSearch);
		handler.GenerationRowSpen(PortList, WebsiteDto.class);
		
		byte[] downLoadFile = null;
		
		KainosExcelWriteHandler excelWriteHandler = KainosExcelWriteHandler.builder().startRowNum(2)
				.templateFile("excel/admin-website-terminal-code-exceldown.xlsx") // 템플릿 파일 경로
				.build();

		excelWriteHandler.writeALL(PortList);
		downLoadFile = excelWriteHandler.writeFlush();
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(downLoadFile.length);
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                  .filename("aaaaaaaaaaa.xlsx", Charset.forName("UTF-8"))
                  .build();
        headers.setContentDisposition(contentDisposition);
		
		return ResponseEntity.ok()
                .headers(headers)
                .contentLength(downLoadFile.length)
                .body(new InputStreamResource(new ByteArrayInputStream(downLoadFile)));
	}
	
	@PostMapping(value = "/api/management/manualupdate")
	public ResponseEntity<Void> manualUpdate(@RequestBody List<WebsiteDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.manualUpdate(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
	@PostMapping(value = "/api/management/comparing/{comparingType}")
	public ResponseEntity<WebsiteDto> comparing(
			@RequestBody List<WebsiteDto> paramList, 
			@PathVariable String comparingType
			) throws Exception {
		try {
			
			Comparator<? super WebsiteDto> comparator =	null;
			if(comparingType.equalsIgnoreCase("1"))
					comparator = Comparator.comparing(WebsiteDto::getProfitDate).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("2"))
					comparator = Comparator.comparing(WebsiteDto::getProfitDate, Comparator.reverseOrder()).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("3"))
					comparator = Comparator.comparing(WebsiteDto::getEtd).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("4"))
					comparator = Comparator.comparing(WebsiteDto::getEtd, Comparator.reverseOrder()).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("5"))
					comparator = Comparator.comparing(WebsiteDto::getEta).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("6"))
					comparator = Comparator.comparing(WebsiteDto::getEta, Comparator.reverseOrder()).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("7"))
					comparator = Comparator.comparing(WebsiteDto::getEndOfFt).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("8"))
					comparator = Comparator.comparing(WebsiteDto::getEndOfFt, Comparator.reverseOrder()).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("9"))
					comparator = Comparator.comparing(WebsiteDto::getReturnDate).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			else if(comparingType.equalsIgnoreCase("10"))
					comparator = Comparator.comparing(WebsiteDto::getReturnDate, Comparator.reverseOrder()).thenComparing(WebsiteDto::getHblNo).thenComparingInt(WebsiteDto::getSeq);
			
			if(comparator != null)
				paramList.sort(comparator);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return KainosResponseEntity.builder().build()
				.addData(handler.GenerationRowSpen(paramList, WebsiteDto.class))
				.close();
	}
}
