package com.kclogix.common.dto;

import java.io.Serializable;

import kainos.framework.core.session.KainosSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SessionDto implements KainosSession, Serializable{

	private String id;
	private String name;
	private String mail;
	private String type;
	private String activation;
	private String partnerCode;
	
	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return id;
	}
	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return name;
	}

}
