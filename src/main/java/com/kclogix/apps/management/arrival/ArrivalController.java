package com.kclogix.apps.management.arrival;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kclogix.apps.management.arrival.service.ArrivalService;
import com.kclogix.apps.management.website.dto.WebsiteDto;
import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
import com.kclogix.apps.management.website.service.WebsiteService;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.MailUtil;
import com.kclogix.common.util.excel.GridRowSpenHandler;
import com.kclogix.common.util.mail.MicrosoftGraph;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.model.KainosMailDto;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import kainos.framework.utils.KainosStringUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArrivalController {
	
	private final ArrivalService service;
	private final WebsiteService websiteService;
	
	private final GridRowSpenHandler handler;
	private final MailUtil mailutil;
	private final MicrosoftGraph mg; // Microsoft Graph
	
	@GetMapping(value = "/api/management/arrivalnotice")
	public ResponseEntity<WebsiteDto> selectArrivalnotice(
			@RequestParam(required = false) String shblNo, 
			@RequestParam(required = false) String sarrivalNotice, 
			@RequestParam(required = false) String stankNo, 
			@RequestParam(required = false) String sitem, 
			@RequestParam(required = false) String scargo, 
			@RequestParam(required = false) String smblNo, 
			@RequestParam(required = false) String spol, 
			@RequestParam(required = false) String spod, 
			@RequestParam(required = false) String seta,
			@RequestParam(required = false) String sshipmentStatus
			
			) throws Exception {
		
		WebsiteSearchDto paramDto = WebsiteSearchDto.builder()
				.tankNo(stankNo)
				.item(sitem)
				.cargo(scargo)
				.hblNo(shblNo)
				.mblNo(smblNo)
				.pol(spol)
				.pod(spod)
				.shipmentStatus(sshipmentStatus)
				.arrivalNotice(sarrivalNotice)
				.eta(seta)
				.build();
		
		List<WebsiteDto> PortList = service.selectArrivalnotice(paramDto, false);
		return KainosResponseEntity.builder().build()
				.addData(handler.GenerationRowSpen(PortList, WebsiteDto.class))
				.close();
	}
	
	@PostMapping(value = "/api/management/arrival-notice-send-mail-template")
	public ResponseEntity<InputStreamResource> arrivalNoticeSendMailTemplate(@RequestBody WebsiteSearchDto paramDto, @KainosSession SessionDto session) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		byte[] eml = null;
		try {
			List<WebsiteDto> portList = websiteService.selectWebsiteTerminalCode(paramDto, false);
			KainosMailDto mailDto = KainosMailDto.builder().build();
			mailDto.from("KCL", "noreply@kclogix.com"); //보내는사람
			mailDto.addCc("kcl@kclogix.com");
			String[] recipients = null;
			if(paramDto.getConcineEmail().indexOf(";") > 0) 
				recipients = paramDto.getConcineEmail().split(";");
			else 
				recipients = new String[] {paramDto.getConcineEmail()};
			
			for (int i = 0; i < recipients.length; i++) {
				mailDto.addTo(recipients[i]); //받는사람
			}
			mailDto.subject("[KCL] Arrival notice 송부의 건, / " + paramDto.getHblNo());
			mailDto.mailbody(anMailTemplate(paramDto, session, portList), true);
			eml = mailutil.sendMessage(mailDto);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			ContentDisposition contentDisposition = ContentDisposition.builder("attachment").filename("ArrivalNoticeTemplate_" + paramDto.getHblNo() + ".eml", Charset.forName("UTF-8")).build();
			headers.setContentDisposition(contentDisposition);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return ResponseEntity.ok().headers(headers)
				.body(new InputStreamResource(new ByteArrayInputStream(eml)));

	}

	@PostMapping(value = "/api/management/arrival-notice-send-mail")
	public ResponseEntity<Void> arrivalNoticeSendMail(
			@RequestPart("files") MultipartFile[] files,
			@RequestPart("jsonData") WebsiteSearchDto paramDto,
			@KainosSession SessionDto session) throws Exception {
		try {
			List<WebsiteDto> portList = websiteService.selectWebsiteTerminalCode(paramDto, false);
			KainosMailDto mailDto = KainosMailDto.builder().build();
			String[] recipients = null;
			if(paramDto.getConcineEmail().indexOf(";") > 0) 
				recipients = paramDto.getConcineEmail().split(";");
			else 
				recipients = new String[] {paramDto.getConcineEmail()};
			
			for (int i = 0; i < recipients.length; i++) {
				mailDto.addTo(recipients[i]); //받는사람
			}
			
			mailDto.subject("[KCL] Arrival notice 송부의 건, / " + paramDto.getHblNo());
			mailDto.mailbody(anMailTemplate(paramDto, session, portList), true);
			for (int i = 0; i < files.length; i++) {
				mailDto.addAttachment(files[i]);
			}
			mg.sendMail(mailDto);
			service.arrivalNoticeSendMail(paramDto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new KainosBusinessException("common.system.error");
		}
		return KainosResponseEntity.noneData();
	}
	
	private String anMailTemplate(WebsiteSearchDto paramDto, SessionDto session, List<WebsiteDto> portList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<style>");
		sb.append(".wrap {");
		sb.append("padding-left: 20pt;");
		sb.append("width: 700px;");
		sb.append("}");
		sb.append("h3{");
		sb.append("font-family: Arial;");
		sb.append("font-size: 10pt;");
		sb.append("color: #333333;");
		sb.append("margin-block-end: 5px;");
		sb.append("margin-bottom: 5px;");
		sb.append("margin-top: 20px;");
		sb.append("}");
		sb.append("table {");
		sb.append("width: 1500px;");
		sb.append("border-collapse: collapse;");
		sb.append("font-family: Arial;");
		sb.append("}");
		sb.append("table.header{");
		sb.append("font-size: 10pt;");
		sb.append("}");
		sb.append("table.detail tr, table.info tr, table.more tr {");
		sb.append("font-size: 9pt;");
		sb.append("height: 22pt;");
		sb.append("}");
		sb.append("table.detail th {");
		sb.append("	width: 17%;");
		sb.append("}");
		sb.append("");
		sb.append("table.detail th, table.info th, table.more th {");
		sb.append("background: #DEE6F0;");
		sb.append("text-align: center;");
		sb.append("font-size: 9pt;");
		sb.append("font-weight: bold;");
		sb.append("border: solid #C4C4C4 1.0pt;");
		sb.append("}");
		sb.append("table.detail td {");
		sb.append("padding-left: 10pt;");
		sb.append("text-align: left;");
		sb.append("font-size: 9pt;");
		sb.append("font-family: Courier New;");
		sb.append("color: #0A59B3;");
		sb.append("border: solid #C4C4C4 1.0pt;");
		sb.append("}");
		sb.append("");
		sb.append("table.info td, table.more td {");
		sb.append("padding-left: 2pt;");
		sb.append("text-align: center;");
		sb.append("font-size: 9pt;");
		sb.append("font-family: Courier New;");
		sb.append("border: solid #C4C4C4 1.0pt;");
		sb.append("}");
		sb.append("table.detail thead th, table.detail thead td, table.info thead th, table.detail thead td,");
		sb.append("table.more thead th, table.more thead td {");
		sb.append("border-top: solid #272727 1.5pt;");
		sb.append("}");
		sb.append("div {");
		sb.append("	font-family: Arial;");
		sb.append("	font-size: 10pt;");
		sb.append("	padding-bottom: 10px;");
		sb.append("}");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div class='wrap'>");
		sb.append("수신 : " + paramDto.getConcineName() + " / " + paramDto.getConcinePic() + " <br>");
		sb.append("발신 : KCL / " + session.getName() + "<br><br>");
		sb.append("안녕하세요!<br>");
		sb.append("1. " + paramDto.getHblNo() + "(HBL NO)건의 A/N을 첨부와 같이 송부 드리오니 확인 부탁드립니다. <br>");
		sb.append("인보이스는 확인하는대로 빠르게 송부 드릴 수 있도록 하겠습니다.<br><br>");
		sb.append("2. OBL 진행 건의 경우,<br>");
		sb.append("OBL 원본(은행 및 귀사) 직인 및 명판 날인하시어 하기 주소로 접수 부탁드립니다.<br>");
		sb.append("서울시 중구 서소문로 116 유원빌딩 1608호 (04513)<br><br>");
		sb.append("SUR BL 진행 건의 경우,<br>");
		sb.append("SUR BL 상 직인 및 명판 날인하시어 송부 부탁드립니다.<br><br>");
		sb.append("3. DO 신청시 이체증 첨부 부탁드립니다. <br><br>");
		sb.append("Remark:<br>");
		sb.append("비위험물 10일 / 위험물 3일의 터미널 무료장치일 적용되오니 참조 부탁드립니다.<br>");
		sb.append("입항일은 사전에 안내 없이 변경 될 수 있습니다.<br><br>");
		sb.append("<table class='info'>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th style='width: 80px;'>Q'ty</th>");
		sb.append("<th style='width: 100px;'>Tank no.</th>");
		sb.append("<th style='width: 80px;'>Term</th>");
		sb.append("<th style='width: 200px;'>ITEM</th>");
		sb.append("<th style='width: 500px;'>Vessel/Voyage</th>");
		sb.append("<th style='width: 100px;'>Carrier</th>");
		sb.append("<th style='width: 150px;'>MBL NO.</th>");
		sb.append("<th style='width: 200px;'>HBL NO.(REF#)</th>");
		sb.append("<th style='width: 100px;'>POL</th>");
		sb.append("<th style='width: 100px;'>POD</th>");
		sb.append("<th style='width: 150px;'>TERMINAL</th>");
		sb.append("<th style='width: 200px;'>ETD</th>");
		sb.append("<th style='width: 200px;'>ETA</th>");
		sb.append("<th style='width: 80px;'>F/T</th>");
		sb.append("<th style='width: 100px;'>DEM RATE</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		for (int i = 0; i < portList.size(); i++) {
			WebsiteDto content = portList.get(i);			
			sb.append("<tr>");
			sb.append("<td>" + content.getQuantity() + "<span></span></td>");
			sb.append("<td>" + content.getTankNo() + "<span></span></td>");
			sb.append("<td>" + content.getTerm() + "<span></span></td>");
			sb.append("<td>" + content.getItem() + "<span></span></td>");
			sb.append("<td>" + content.getVesselVoyage() + "<span></span></td>");
			sb.append("<td>" + content.getCarrier() + "<span></span></td>");
			sb.append("<td>" + content.getMblNo() + "<span></span></td>");
			sb.append("<td>" + content.getHblNo()+ "<span></span></td>");
			sb.append("<td>" + content.getPol() + "<span></span></td>");
			sb.append("<td>" + content.getPod() + "<span></span></td>");
			if(KainosStringUtils.isEmpty(content.getTerminalName()))
				sb.append("<td>&nbsp;<span></span></td>");
			else
				sb.append("<td>" + content.getTerminalName() + "<span></span></td>");
			sb.append("<td>" + content.getEtd() + "<span></span></td>");
			sb.append("<td>" + content.getEta() + "<span></span></td>");
			sb.append("<td>" + content.getFt() + "<span></span></td>");
			sb.append("<td>" + content.getDemRate() + "<span></span></td>");
			sb.append("</tr>");
		}
		sb.append("		</table>		");
		sb.append("	</div>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
}
