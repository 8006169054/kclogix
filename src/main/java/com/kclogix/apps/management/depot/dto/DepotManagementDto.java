package com.kclogix.apps.management.depot.dto;

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
public class DepotManagementDto {

	private String jqFlag;
	private String uuid;
	private int seq;
	private String hblNo;
	private String tankNo;
	private String partner;
	private String item;
	private String returnDepot;
	private String returnDate;
	private String cleanedDate;
	private String outDate;
	private String allocation;
	private String remark;
	private String allocationType;
	private String createUserId;
	private String createDate;
	private String updateUserId;
	private String updateDate;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class DateExcelUpload {
		@Field(value = "A")
		private String tankNo;
		@Field(value = "B")
		private String cleanedDate;
		@Field(value = "C")
		private String outDate;
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class AllocationExcelUpload {
		@Field(value = "A")
		private String tankNo;
		@Field(value = "B")
		private String partner;
		@Field(value = "C")
		private String returnDepot;
		@Field(value = "D")
		private String allocation;
	}
}
