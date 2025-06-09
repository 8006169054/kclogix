package com.kclogix.apps.system.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ComUserDto {

	private String jqFlag;
	private String id;
	private String password;
	private String name;
	private String mail;
	private String activation;
	private String type;
	private String partnerCode;
	private String createUserId;
	private String createDate;
	private String updateUserId;
	private String updateDate;
	
}
