package com.kclogix.apps.partner.home;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
import com.kclogix.apps.partner.home.dto.HomeDto;
import com.kclogix.apps.partner.home.dto.HomeExcelDownDto;
import com.kclogix.apps.partner.home.service.HomeService;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.excel.GridRowSpenHandler;
import com.kclogix.common.util.excel.KainosExcelWriteHandler;

import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import kainos.framework.utils.KainosStringUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HomeController {
	
	private final HomeService service;
	private final GridRowSpenHandler handler;
	
	@GetMapping(value = "/api/partner/home/website-terminal-code")
	public ResponseEntity<HomeDto> selectWebsiteTerminalCode(
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
			@KainosSession SessionDto session
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
				.partner(session.getPartnerCode())
				.build();
		
		List<HomeDto> PortList = service.selectWebsiteTerminalCode(paramDto);
		return KainosResponseEntity.builder().build()
				.addData(handler.GenerationRowSpen(PortList, HomeDto.class))
				.close();
	}
	
	@GetMapping(value = "/api/partner/home/website-terminal-code-grid-col")
	public ResponseEntity<String> selectWebsiteTerminalCodeGridCol(@KainosSession SessionDto session) throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectWebsiteTerminalCodeGridCol(session.getUserId()))
				.close();
	}
	
	@GetMapping(value = "/api/partner/home/website-terminal-code-exceldown")
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
			@KainosSession SessionDto session
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
				.partner(session.getPartnerCode())
				.build();
		
		List<HomeExcelDownDto> PortList = service.selectWebsiteTerminalCodeExcel(paramDto);
		handler.GenerationRowSpen(PortList, HomeExcelDownDto.class);
		
		byte[] downLoadFile = null;
		
		List<String> hiddenCelVal = new ArrayList<>();
		String hidStr = service.selectWebsiteTerminalCodeGridCol(session.getUserId());
		if(!KainosStringUtils.isEmpty(hidStr)) {
			if(hidStr.indexOf(",") > 0) {
				hiddenCelVal = List.of(hidStr.split(","));
			}else {
				hiddenCelVal.add(hidStr);
			}
		}
		KainosExcelWriteHandler excelWriteHandler = KainosExcelWriteHandler.builder().startRowNum(1).hideColList(hiddenCelVal)
				.templateFile("excel/website-terminal-code-exceldown.xlsx") // 템플릿 파일 경로
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
}
