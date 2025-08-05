package com.kclogix.apps.partner.management.depot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kclogix.apps.management.depot.dto.DepotManagementDto;
import com.kclogix.apps.management.depot.service.DepotManagementService;
import com.kclogix.apps.mdm.depot.service.DepotService;
import com.kclogix.apps.mdm.partner.service.PartnerService;
import com.kclogix.common.dto.SessionDto;

import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PartnerDepotManagementController {
	
	private final DepotManagementService service;
	private final DepotService depotService;
	private final PartnerService partnerService;
	
	@GetMapping(value = "/api/partner/management/depotmonitor")
	public ResponseEntity<DepotManagementDto> depotmonitor(@KainosSession SessionDto session) throws Exception {
		List<Map<Integer, Object>> returnData = new ArrayList<>();
		List<String> defaultCol = List.of("DEPOT TOTAL", "DEPOT CODE", "PARTNER");
		for (int i = 0; i < defaultCol.size(); i++) {
			Map<Integer, Object> col = new LinkedHashMap<>();
			col.put(0, defaultCol.get(i));
			returnData.add(col);
		}
		
		/* LOCATION 0번쨰 ROW 데이터 셋팅 */
		List<String> partnerList = partnerService.selectMonitorColModels(session.getPartnerCode());
		for (int i = 0; i < partnerList.size(); i++) {
			Map<Integer, Object> col = new LinkedHashMap<>();
			col.put(0, partnerList.get(i));
			returnData.add(col);
		}
		
		/* DEPOT CODE 설정 2번째 행 데이터 */
		Map<Integer, Object> depotCodeMap = returnData.get(1);
		List<String> depotCodeList = depotService.selectMonitorDepotCode();
		int depotCodeIndex = 1;
		for (int i = 0; i <= depotCodeList.size(); i++) {
			if(i == depotCodeList.size()) {
				depotCodeMap.put(depotCodeIndex++, "TOTAL");
				depotCodeMap.put(depotCodeIndex++, "TOTAL");
			}
			else {
				depotCodeMap.put(depotCodeIndex++, depotCodeList.get(i));
				depotCodeMap.put(depotCodeIndex++, depotCodeList.get(i));
			}
		}
		
		/* PARTNER 설정 3번째 행 데이터 */
		Map<Integer, Object> partnerMap = returnData.get(2);
		List<String> depotList = depotService.selectMonitorColNames();
		for (int i = 0; i <= depotList.size()*2; i++) {
			if(i == depotList.size()*2) {
				partnerMap.put(i+1, "INVENTORY");
				partnerMap.put(i+2, "AV");
			}
			else {
				if(i % 2 == 1)  partnerMap.put(i+1, "AV");
				else partnerMap.put(i+1, "INVENTORY");
			}
		}
		
		/* 2번째 행 토탈 */
		Map<Integer, Object> depotReportTotal = returnData.get(0);
		/* 4번째 행부터는 파트너코드로 DEPORT 카운드 조회 */
		for (int i = 0; i < partnerList.size(); i++) {
			Map<Integer, Object> reportMap = returnData.get(i+3);
			/* 파트너 별로 DEPORT 의 갯수를 조회한다. */
			List<Long> depotInventory = service.selectDepotReport(partnerList.get(i), depotCodeList, "INVENTORY");
			List<Long> depotAv = service.selectDepotReport(partnerList.get(i), depotCodeList, "AV");
			int reportColCount = 1;
			for (int j = 0; j < depotInventory.size(); j++) {
				long depotInventoryTotal = depotInventory.get(j);
				long depotAvTotal = depotAv.get(j);
				
				if(depotReportTotal.get(reportColCount) != null && (long)depotReportTotal.get(reportColCount) > 0)
					depotInventoryTotal += (long)depotReportTotal.get(reportColCount);
				
				depotReportTotal.put(reportColCount, depotInventoryTotal);
				reportMap.put(reportColCount++, depotInventory.get(j)); //INVENTORY
				
				
				if(depotReportTotal.get(reportColCount) != null && (long)depotReportTotal.get(reportColCount) > 0)
					depotAvTotal += (long)depotReportTotal.get(reportColCount);
				
				depotReportTotal.put(reportColCount, depotAvTotal);
				reportMap.put(reportColCount++, depotAv.get(j)); //AV
				
			}
		}
		
		return KainosResponseEntity.builder().build()
				.addData(returnData)
				.close();
	}
	
}
