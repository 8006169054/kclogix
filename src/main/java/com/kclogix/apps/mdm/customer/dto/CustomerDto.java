package com.kclogix.apps.mdm.customer.dto;

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
public class CustomerDto {

	private String jqFlag;
	@Field(value = "A") 
	private String code;
	@Field(value = "B") 
	private String name;
	@Field(value = "C") 
	private String pic;
	@Field(value = "D") 
	private String tel;
	@Field(value = "E") 
	private String email;
	private String createUserId;
	private String createDate;
	private String updateUserId;
	private String updateDate;
}
