package com.kclogix.apps.management.website.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteSearchDto {

	private String profitDate;
	private String partner;
	private String tankNo;
	private String item;
	private String cargo; 
	private String hblNo; 
	private String mblNo; 
	private String pol;
	private String pod; 
	private String ata; 
	private String erd; 
	private String returnDate; 
	private String returnDepot; 
	private String demRcvd;
	private String demRcvdSelect;
	
}
