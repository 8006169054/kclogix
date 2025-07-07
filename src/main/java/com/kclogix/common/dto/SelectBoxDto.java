package com.kclogix.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class SelectBoxDto {

	@Builder
	@Data
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PartnerAutocomplete {
		private String value;
		private String label;
	}
	
	@Builder
	@Data
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CarGoAutoComplete {
		private String value;
		private String label;
		private String code;
		private String cargoDate;
		private String location;
	}
	
	@Builder
	@Data
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TerminalAutoComplete {
		private String value;
		private String name;
		private String label;
		private String code;
		private String region;
		private String parkingLotCode;
		private String homepage;
		
	}
	
	@Builder
	@Data
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CustomerAutoComplete {
		private String value;
		private String label;
		private String code;
		private String pic;
		private String email;
		private String tel;
		
	}
}
