package com.kclogix.apps.management.depot.dto;

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
}
