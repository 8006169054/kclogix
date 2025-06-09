package com.kclogix.apps.mdm.terminal.dto;

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
public class TerminalDto {

	private String jqFlag;
	@Field(value = "C") 
	private String code;
	@Field(value = "D") 
	private String name;
	@Field(value = "A") 
	private String region;
	@Field(value = "B") 
	private String type;
	@Field(value = "E") 
	private String parkingLotCode;
	@Field(value = "F") 
	private String homepage;
	private String createUserId;
	private String createDate;
	private String updateUserId;
	private String updateDate;
	
}
