package com.kclogix.common.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteTerminalCode {
	
	@Id
	private String uuid;
	private int seq;
	private String sales;
	private String carryoverSales;
	private String arrivalNotice;
	private String invoice;
	private String concine;
	private String profitDate;
	private String domesticSales;
	private String foreignSales;
	private String quantity;
	private String partner;
	private String tankNo;
	private String term;
	private String item;
	private String vesselVoyage;
	private String carrier;
	private String mblNo;
	private String hblNo;
	private String pol;
	private String pod;
	private String terminal;
	private String etd;
	private String eta;
	private String ata;
	private String remark;
	private String ft;
	private String demRate;
	private String endOfFt;
	private String estimateReturnDate;
	private String returnDate;
	private String demReceived;
	private String totalDem;
	private String returnDepot;
	private String demRcvd;
	private String demPrch;
	private String demSales;
	private String depotInDate;
	private String repositionPrch;
	private String createUserId;
	private Date createDate;
	private String updateUserId;
	private Date updateDate;
}
