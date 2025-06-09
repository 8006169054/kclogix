package com.kclogix.apps.mdm.cargo.dto;

import com.kclogix.common.util.excel.Field;
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
public class CargoDto {

	private String jqFlag;
	private String code;
	@Field(value = "D") 
	private String name;
	@Field(value = "A") 
	private String cargoDate;
	@Field(value = "B") 
	private String location;
	@Field(value = "C") 
	private String depot;
	@Field(value = "E") 
	private String cleaningCost;
	@Field(value = "F") 
	private String difficultLevel;
	@Field(value = "G") 
	private String remark1;
	@Field(value = "H") 
	private String remark2;
	private String createUserId;
	private String createDate;
	private String updateUserId;
	private String updateDate;
	
}
