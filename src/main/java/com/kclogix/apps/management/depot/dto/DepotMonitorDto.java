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
public class DepotMonitorDto {

	private String location;
	private String busanNewPort;
	private String busanOldPort;
	private String yangsan1;
	private String yangsan2;
	private String yangsan3;
	private String yangsan4;
	private String kwangyang;
	private String yeosu;
	private String dangjin;
	private String ulsan;
	private String total;
}
