package com.kclogix.apps.mdm.depot.dto;

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
public class DepotDto {

	private String jqFlag;
	private String depotCode;
	private String location;
	private String depotNameEn;
	private String depotNameKr;
	private String address;
	private String picName;
	private String picTel;
	private String picEmail;
	private String operationRemark;
	private String createUserId;
	private String createDate;
	private String updateUserId;
	private String updateDate;
}
