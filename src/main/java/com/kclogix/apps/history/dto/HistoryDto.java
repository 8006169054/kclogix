package com.kclogix.apps.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


public class HistoryDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class CargoDto {
		private String name;
		private String cargoDate;
		private String location;
		private String depot;
		private String cleaningCost;
		private String difficultLevel;
		private String remark1;
		private String remark2;
		private String createUserId;
		private String createDate;
		private String updateUserId;
		private String updateDate;
	}
	
}
