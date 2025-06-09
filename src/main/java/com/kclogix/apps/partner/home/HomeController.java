package com.kclogix.apps.partner.home;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import com.kclogix.apps.partner.home.dto.HomeDto;
import com.kclogix.apps.partner.home.dto.HomeExcelDownDto;
import com.kclogix.apps.partner.home.service.HomeService;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.excel.GridRowSpenHandler;
import com.kclogix.common.util.excel.KainosExcelWriteHandler;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HomeController {
	
	private final HomeService service;
	private final GridRowSpenHandler handler;
	
	@GetMapping(value = "/api/partner/home/website-terminal-code")
	public ResponseEntity<HomeDto> selectWebsiteTerminalCode(
			@RequestParam(required = false) String hblNo, @KainosSession SessionDto session
		) throws Exception {
		List<HomeDto> PortList = service.selectWebsiteTerminalCode(HomeDto.builder().hblNo(hblNo).partner(session.getPartnerCode()).build());
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
	public ResponseEntity<InputStreamResource> exceldown(@RequestParam(required = false) String hblNo, @KainosSession SessionDto session) throws Exception {
		
		List<HomeExcelDownDto> PortList = service.selectWebsiteTerminalCodeExcel(HomeDto.builder().hblNo(hblNo).partner(session.getPartnerCode()).build());
		handler.GenerationRowSpen(PortList, HomeExcelDownDto.class);
		
		byte[] downLoadFile = null;
		
		KainosExcelWriteHandler excelWriteHandler = KainosExcelWriteHandler.builder().startRowNum(1)
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
