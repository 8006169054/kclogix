package com.kclogix.apps.partner.home.dto;

import kainos.framework.core.support.jqgrid.dto.RowSpan;
import com.kclogix.common.util.excel.Field;
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
public class HomeExcelDownDto {

	@Field(value = "A", mergeOrder = 0, merge = true)
	private String hblNo; //HBL NO.
	
	@Field(value = "B", merge = true)
	private String quantity; //Q'ty
	
	@Field(value = "C")
	private String partner; //Partner
	
	@Field(value = "D")
	private String tankNo; //Tank no.
	
	@Field(value = "E", merge = true)
	private String term; //Term
	
	@Field(value = "F", merge = true)
	private String item; //ITEM
	
	@Field(value = "G", merge = true)
	private String vesselVoyage; //Vessel / Voyage
	
	@Field(value = "H", merge = true)
	private String carrier; //Carrier
	
	@Field(value = "I", merge = true)
	private String mblNo; //MBL NO
	
	@Field(value = "J", merge = true)
	private String pol; //POL
	
	@Field(value = "K")
	private String pod; //POD
	
	@Field(value = "L")
	private String etd; //ETD
	
	@Field(value = "M")
	private String eta; //ETA
	
	@Field(value = "N")
	private String ft; //F/T
	
	@Field(value = "O")
	private String demRate; //DEM RATE
	
	@Field(value = "P")
	private String endOfFt; //END OF F/T
	
	@Field(value = "Q")
	private String returnDate; //RETURN DATE
	
	@Field(value = "R")
	private String returnDepot; //RETURN DEPOT
	
	@Field(value = "S")
	private String totalDem; //TOTAL DEM
	
	@Field(value = "T")
	private String demPrch; //DEM(USD)-매입
	
	@Field(value = "U")
	private String demSales; //DEM 매출
	
	@Field(value = "V")
	private String depotInDate; //DEPOT IN DATE
	
	@Builder.Default /* 필드명을 rowspan 해야 함 필수 */
	private RowSpanOtion rowspan = RowSpanOtion.builder().build();
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RowSpanOtion{
		@Builder.Default
		private RowSpan hblNo = RowSpan.builder().build();
		@Builder.Default
		private RowSpan quantity = RowSpan.builder().build();
		@Builder.Default
		private RowSpan term = RowSpan.builder().build();
		@Builder.Default
		private RowSpan item = RowSpan.builder().build();
		@Builder.Default
		private RowSpan vesselVoyage = RowSpan.builder().build();
		@Builder.Default
		private RowSpan carrier = RowSpan.builder().build();
		@Builder.Default
		private RowSpan mblNo = RowSpan.builder().build();
		@Builder.Default
		private RowSpan pol = RowSpan.builder().build();
	};
}
