package com.kclogix.apps.system.notices.dto;

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
public class NoticesDto {

	private String id;
	private String title;
	private String viewStartDate;
	private String viewEndDate;
	private String use;
	private String updateUserId;
	private String updateDate;
	private String contentBody;
	
	private String noticesDate;
	
	
}
