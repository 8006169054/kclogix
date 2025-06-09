package com.kclogix.apps.history;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kainos.framework.core.servlet.KainosResponseEntity;
import com.kclogix.apps.history.dto.HistoryDto;
import com.kclogix.apps.history.service.HistoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HistoryController {
	
	private final HistoryService service;
	
	@GetMapping(value = "/api/history/cargo")
	public ResponseEntity<HistoryDto.CargoDto> selectCargo(
			@RequestParam(required = false) String name
			) throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectCargo(HistoryDto.CargoDto.builder().name(name).build()))
				.close();
	}
	
}
