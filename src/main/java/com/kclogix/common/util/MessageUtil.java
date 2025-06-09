package com.kclogix.common.util;

import org.springframework.stereotype.Component;

import kainos.framework.core.context.KainosMessageAccessor;
import kainos.framework.core.servlet.KainosResponseEntity;
import kainos.framework.core.servlet.model.KainosMessageString;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageUtil {

	private final KainosMessageAccessor message;
	
	/**
	 * 등록 메세지
	 * @return
	 */
	public KainosResponseEntity getInsertMessage(KainosResponseEntity responseEntity) {
		responseEntity.setMessage(KainosMessageString.builder()
				.message(message.getMessage("common.data.insert"))
				.messageType(message.getMessageType("common.data.insert"))
				.build());
		return responseEntity;
	}
	
	/**
	 * 수정 메세지
	 * @return
	 */
	public KainosResponseEntity getUpdateMessage(KainosResponseEntity responseEntity) {
		responseEntity.setMessage(KainosMessageString.builder()
				.message(message.getMessage("common.data.update"))
				.messageType(message.getMessageType("common.data.update"))
				.build());
		return responseEntity;
	}
	
	/**
	 * 삭제 메세지
	 * @return
	 */
	public KainosResponseEntity getDeleteMessage(KainosResponseEntity responseEntity) {
		responseEntity.setMessage(KainosMessageString.builder()
				.message(message.getMessage("common.data.delete"))
				.messageType(message.getMessageType("common.data.delete"))
				.build());
		return responseEntity;
	}
}

