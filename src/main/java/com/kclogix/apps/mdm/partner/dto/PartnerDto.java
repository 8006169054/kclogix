package com.kclogix.apps.mdm.partner.dto;

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
public class PartnerDto {

	private String jqFlag;
	private String code;
	private String name;
	private String company;
	private String pic;
	private String representativeEml;
	private String importMoniterRoleName;
	private String importMoniterRoleCode;
	private String createUserId;
	private String createDate;
	private String updateUserId;
	private String updateDate;
}
