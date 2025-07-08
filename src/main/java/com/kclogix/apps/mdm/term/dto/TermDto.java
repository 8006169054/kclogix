package com.kclogix.apps.mdm.term.dto;

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
public class TermDto {

	private String jqFlag;
	private String id;
	private String name;
	private String use;
	private String createUserId;
	private String createDate;
	private String updateUserId;
	private String updateDate;
}
