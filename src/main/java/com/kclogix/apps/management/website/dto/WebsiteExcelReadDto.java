package com.kclogix.apps.management.website.dto;

import java.util.Date;

import kainos.framework.core.support.jqgrid.dto.RowSpan;
import kainos.framework.utils.KainosDateUtil;
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
public class WebsiteExcelReadDto {

	private String jqFlag;
	
	@Field(value = "A", merge = true) 
	private String sales; //매출
	
	@Field(value = "B", merge = true)
	private String carryoverSales; //이월 매출
	
	@Field(value = "C", merge = true)
	private String arrivalNotice; //A/N&EDI
	
	@Field(value = "D", merge = true)
	private String invoice; //INVOICE
	
	@Field(value = "E", merge = true)
	private String concine; //CNEE
	
	@Field(value = "F", merge = true)
	private String shipmentStatus; //SHIPMENT STATUS
	
	@Field(value = "G", merge = true)
	private String profitDate; //PROFIT DATE
	
	@Field(value = "H", merge = true, function = "domesticSalesFn")
	private String domesticSales; //국내매출
	
	@Field(value = "I", merge = true, function = "foreignSalesFn")
	private String foreignSales; //해외매출
	
	@Field(value = "J", merge = true)
	private String quantity; //Q'ty
	
	@Field(value = "K")
	private String partner; //Partner
	
	@Field(value = "L")
	private String tankNo; //Tank no.
	
	@Field(value = "M", merge = true)
	private String term; //Term
	
	@Field(value = "N", merge = true)
	private String item; //ITEM
	
	@Field(value = "O", merge = true)
	private String vesselVoyage; //Vessel / Voyage
	
	@Field(value = "P", merge = true)
	private String carrier; //Carrier
	
	@Field(value = "Q", merge = true)
	private String mblNo; //MBL NO
	
	@Field(value = "R", mergeOrder = 0, merge = true)
	private String hblNo; //HBL NO.
	
	@Field(value = "S", merge = true)
	private String pol; //POL
	
	@Field(value = "T")
	private String pod; //POD
	
	@Field(value = "U")
	private String terminal; //TERMINAL
	
	@Field(value = "V")
	private String etd; //ETD
	
	@Field(value = "W")
	private String eta; //ETA
	
	@Field(value = "X")
	private String ata; //ATA
	
	@Field(value = "Y", merge = true)
	private String remark; //비고
	
	@Field(value = "Z", function = "ftFn")
	private String ft; //F/T
	
	@Field(value = "AA", function = "demRateFn")
	private String demRate; //DEM RATE
	
	@Field(value = "AB", function = "endOfFtFn")
	private String endOfFt; //END OF F/T
	
	@Field(value = "AC")
	private String estimateReturnDate; //ESTIMATE RETURN DATE
	
	@Field(value = "AD")
	private String returnDate; //RETURN DATE
	
	@Field(value = "AE")
	private String returnDepot; //RETURN DEPOT
	
	@Field(value = "AF")
	private String demStatus; //DEM STATUS
	
	@Field(value = "AG", function = "totalDemFn")
	private String totalDem; //TOTAL DEM
	
	@Field(value = "AH")
	private String demReceived; //DEM RECEIVED
	
	@Field(value = "AI")
	private String demRcvd; //DEM RCVD
	
	@Field(value = "AJ", function = "demPrchFn")
	private String demPrch; //COMMISSION DEDUCTED
	
	@Field(value = "AK", function = "demSalesFn")
	private String demSales; //DEM COMMISSION
	
	@Field(value = "AL")
	private String depotInDate; //DEPOT IN DATE
	
	@Field(value = "AM")
	private String repositionPrch; //REPOSITION 매입
	
	@Builder.Default /* 필드명을 rowspan 해야 함 필수 */
	private RowSpanOtion rowspan = RowSpanOtion.builder().build();
	
	/**
	 * 포맷 변경
	 * @param dateDto
	 * @return
	 */
	public String foreignSalesFn(WebsiteExcelReadDto dateDto) {
		String value = "-";
		try {
			value = String.format("%.0f", Double.parseDouble(dateDto.getForeignSales()));
		}catch (Exception e) {
//			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 포맷 변경
	 * @param dateDto
	 * @return
	 */
	public String demRateFn(WebsiteExcelReadDto dateDto) {
		String value = "-";
		try {
			value = String.format("%.0f", Double.parseDouble(dateDto.getDemRate()));
		}catch (Exception e) {
//			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 포맷 변경
	 * @param dateDto
	 * @return
	 */
	public String domesticSalesFn(WebsiteExcelReadDto dateDto) {
		String value = "-";
		try {
			value = String.format("%.0f", Double.parseDouble(dateDto.getDomesticSales()));
		}catch (Exception e) {
//			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 포맷 변경 소수점 삭제
	 * @param dateDto
	 * @return
	 */
	public String ftFn(WebsiteExcelReadDto dateDto) {
		String value = "-";
		try {
			value = String.format("%.0f", Double.parseDouble(dateDto.getFt()));
		}catch (Exception e) {
//			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 포맷 변경 마이너스일때 N/A 표기
	 * @param dateDto
	 * @return
	 */
	public String demDaysFn(WebsiteExcelReadDto dateDto) {
		String value = "N/A";
		try {
			if(!dateDto.getReturnDate().equals("N/A") && !dateDto.getEndOfFt().equals("N/A")) {
				int dif = (int) ((KainosDateUtil.string2Date(dateDto.getReturnDate(), "yyyy-MM-dd").getTime()-KainosDateUtil.string2Date(dateDto.getEndOfFt(), "yyyy-MM-dd").getTime()) / (24*60*60*1000));
//				System.out.println(dateDto.getReturnDate() + " : " + dateDto.getEndOfFt() + " : " + dif);
				if(dif < 0) value = "N/A";
				else value = String.valueOf(dif);
			}
		}catch (Exception e) {
//			e.printStackTrace();
		}
		return value;
	}
	
	public String totalDemFn(WebsiteExcelReadDto dateDto) {
		String value = "N/A";
		try {
//			=IF($AC6<=$AA6,"N/A",(($AC6-$AA6)*$Z6))
			if(!dateDto.getReturnDate().equals("N/A") && !dateDto.getEndOfFt().equals("N/A")) {
				Date returnDate = KainosDateUtil.string2Date(dateDto.getReturnDate(), "yyyy-MM-dd");
				Date endOfFt = KainosDateUtil.string2Date(dateDto.getEndOfFt(), "yyyy-MM-dd");
				
				if(returnDate.getTime() <= endOfFt.getTime()) {
					value = "N/A";
				}
				else {
					value = String.valueOf(((int)(returnDate.getTime()-endOfFt.getTime()) / (24*60*60*1000)) * Integer.parseInt(dateDto.getDemRate()));
				}
			}
		}catch (Exception e) {
//			e.printStackTrace();
		}
		
		return value;
	}
	
	public String demPrchFn(WebsiteExcelReadDto dateDto) {
		String value = "N/A";
		try {
//			=IFERROR(IF($AE7>0,$AE7*0.9,"N/A"),"N/A")
			int totalDem = Integer.parseInt(dateDto.getTotalDem());
			if(totalDem > 0) {
				value = String.format("%.2f", totalDem * 0.9);
			}
		}catch (Exception e) {
//			e.printStackTrace();
		}
		return value;
	}
	
	public String demSalesFn(WebsiteExcelReadDto dateDto) {
		String value = "N/A";
		try {
//			=IFERROR(IF(AE6>0,AE6*0.1,"N/A"),"N/A")
			int totalDem = Integer.parseInt(dateDto.getTotalDem());
			if(totalDem > 0) {
				value = String.format("%.2f", totalDem * 0.1);
			}
		}catch (Exception e) {
//			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 포맷 변경 (ETA 날짜보다 + (F/T-1))
	 * @param dateDto
	 * @return
	 */
	public String endOfFtFn(WebsiteExcelReadDto dateDto) {
		// ETA 날짜보다 + F/T
		String value = "N/A";
		try {
			int ft = Integer.parseInt(dateDto.getFt());
			value = KainosDateUtil.addDays(dateDto.getEta(), ft-1);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return value;
	}
	
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RowSpanOtion{
		@Builder.Default
		private RowSpan sales = RowSpan.builder().build();
		@Builder.Default
		private RowSpan carryoverSales = RowSpan.builder().build();
		@Builder.Default
		private RowSpan arrivalNotice = RowSpan.builder().build();
		@Builder.Default
		private RowSpan invoice = RowSpan.builder().build();
		@Builder.Default
		private RowSpan concine = RowSpan.builder().build();
		@Builder.Default
		private RowSpan shipmentStatus = RowSpan.builder().build();
		@Builder.Default
		private RowSpan profitDate = RowSpan.builder().build();
		@Builder.Default
		private RowSpan domesticSales = RowSpan.builder().build();
		@Builder.Default
		private RowSpan foreignSales = RowSpan.builder().build();
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
		private RowSpan hblNo = RowSpan.builder().build();
		@Builder.Default
		private RowSpan pol = RowSpan.builder().build();
		@Builder.Default
		private RowSpan remark = RowSpan.builder().build();
	};
}
