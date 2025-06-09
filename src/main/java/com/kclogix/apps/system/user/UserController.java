package com.kclogix.apps.system.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import groovy.util.logging.Slf4j;
import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.session.annotation.KainosSession;
import com.kclogix.apps.mdm.cargo.dto.CargoDto;
import com.kclogix.apps.system.user.dto.ComUserDto;
import com.kclogix.apps.system.user.service.UserService;
import com.kclogix.common.dto.SessionDto;
import com.kclogix.common.util.MessageUtil;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService service;
	private final MessageUtil message;
	
	@GetMapping(value = "/api/system/user")
	public ResponseEntity<ComUserDto> selectCargo(
			@RequestParam(required = false) String id
			) throws Exception {
		return KainosResponseEntity.builder().build()
				.addData(service.selectComUser(ComUserDto.builder().id(id).build()))
				.close();
	}
	
	@PostMapping(value = "/api/system/user")
	public ResponseEntity<Void> saveComUser(@RequestBody List<ComUserDto> paramList, @KainosSession SessionDto session) throws Exception {
		try {
			service.saveComUser(paramList, session);
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new KainosBusinessException("common.system.error");
		}
		return message.getInsertMessage(KainosResponseEntity.builder().build()).close();
	}
	
}
