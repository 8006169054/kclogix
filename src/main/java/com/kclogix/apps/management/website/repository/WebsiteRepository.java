package com.kclogix.apps.management.website.repository;

import static com.kclogix.common.entity.QMdmCargo.mdmCargo;
import static com.kclogix.common.entity.QMdmCustomer.mdmCustomer;
import static com.kclogix.common.entity.QMdmTerminal.mdmTerminal;
import static com.kclogix.common.entity.QWebsiteTerminalCode.websiteTerminalCode;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kclogix.apps.management.website.dto.WebsiteDto;
import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class WebsiteRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public long selectWebsiteTerminalCount(WebsiteDto paramDto) throws Exception {
		return select(websiteTerminalCode.uuid.count())
				.from(websiteTerminalCode)
				.where(websiteTerminalCode.hblNo.eq(paramDto.getHblNo())
						.and(websiteTerminalCode.tankNo.eq(paramDto.getTankNo())))
			.fetchOne();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public void arrivalNoticeSendMail(WebsiteDto paramDto) throws Exception {
		update(websiteTerminalCode)
		 .set(websiteTerminalCode.arrivalNotice, "1")
		 .where(websiteTerminalCode.hblNo.eq(paramDto.getHblNo()))
		 .execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<WebsiteDto> selectWebsiteTerminalCodeNew(WebsiteSearchDto paramDto, boolean init) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(init)
			initWhere(where);
		else
			searchWhere(paramDto, where);
		
		return select(Projections.bean(WebsiteDto.class,
					websiteTerminalCode.uuid,
					websiteTerminalCode.seq,
					websiteTerminalCode.sales,
					websiteTerminalCode.carryoverSales,
					new CaseBuilder().when(websiteTerminalCode.arrivalNotice.eq("1")).then(Expressions.constant("SEND")).otherwise(Expressions.constant("")).as("arrivalNotice"),
					new CaseBuilder().when(websiteTerminalCode.invoice.eq("1")).then(Expressions.constant("SEND")).otherwise(Expressions.constant("")).as("invoice"),
					new CaseBuilder().when(mdmCustomer.name.isNull()).then(websiteTerminalCode.concine).otherwise(mdmCustomer.name).as("concineName"),
					mdmCustomer.code.as("concine"),
					mdmCustomer.pic.as("concinePic"),
					mdmCustomer.email.as("concineEmail"),
//					 new CaseBuilder()
//			            .when(websiteTerminalCode.profitDate.length().goe(10)).then(websiteTerminalCode.profitDate)
//			            .otherwise("9999-99-99")
//			            .as("profitDate"),
			         websiteTerminalCode.profitDate,
					websiteTerminalCode.domesticSales,
					websiteTerminalCode.foreignSales,
					websiteTerminalCode.quantity,
					websiteTerminalCode.partner,
					websiteTerminalCode.tankNo,
					websiteTerminalCode.term,
					websiteTerminalCode.item,
					websiteTerminalCode.vesselVoyage,
					websiteTerminalCode.carrier,
					websiteTerminalCode.mblNo,
					websiteTerminalCode.hblNo,
					websiteTerminalCode.pol,
//					websiteTerminalCode.pod,
					new CaseBuilder().when(mdmTerminal.region.isNull()).then(websiteTerminalCode.pod.upper()).otherwise(mdmTerminal.region.upper()).as("pod"),
					mdmTerminal.code.as("terminalCode"),
					mdmTerminal.name.as("terminalName"),
//					mdmTerminal.region.upper().as("region"),
					mdmTerminal.homepage.as("terminalHomepage"),
					mdmCargo.code.as("cargo"),
					mdmCargo.cargoDate.upper().as("cargoDate"),
					mdmCargo.location.upper().as("location"),
					new CaseBuilder().when(mdmCargo.name.isNull()).then(websiteTerminalCode.item.upper()).otherwise(mdmCargo.name.upper()).as("item"),
//					ExpressionUtils.as(JPAExpressions.select(terminal.homepage).from(terminal).where(websiteTerminalCode.pod.eq(terminal.region)), "homepage"),
					websiteTerminalCode.etd,
					websiteTerminalCode.eta,
					websiteTerminalCode.ata,
					websiteTerminalCode.remark,
					websiteTerminalCode.ft,
					websiteTerminalCode.demRate,
					websiteTerminalCode.endOfFt,
					websiteTerminalCode.estimateReturnDate,
					websiteTerminalCode.returnDate,
					websiteTerminalCode.demReceived,
					websiteTerminalCode.totalDem,
					websiteTerminalCode.returnDepot,
					websiteTerminalCode.demRcvd,
					websiteTerminalCode.demPrch,
					websiteTerminalCode.demSales,
					websiteTerminalCode.depotInDate,
					websiteTerminalCode.repositionPrch,
					websiteTerminalCode.createUserId,
					websiteTerminalCode.shipmentStatus,
					websiteTerminalCode.demStatus,
					Expressions.stringTemplate("to_char({0}, {1})", websiteTerminalCode.createDate, "YYYY-MM-DD").as("createDate"),
					websiteTerminalCode.updateUserId,
					Expressions.stringTemplate("to_char({0}, {1})", websiteTerminalCode.updateDate, "YYYY-MM-DD").as("updateDate")
				)).from(websiteTerminalCode)
				.leftJoin(mdmCargo).on(websiteTerminalCode.item.eq(mdmCargo.code))
				.leftJoin(mdmTerminal).on(websiteTerminalCode.terminal.eq(mdmTerminal.code))
				.leftJoin(mdmCustomer).on(websiteTerminalCode.concine.eq(mdmCustomer.code))
				.where(where)
				.orderBy(websiteTerminalCode.profitDate.asc())
				.fetch();
	}

	/**
	 * 
	 * @param paramDto
	 * @param where
	 */
	private void initWhere(BooleanBuilder where) {
		where.and(websiteTerminalCode.profitDate.length().ne(10));
		where.and(websiteTerminalCode.demRcvd.length().ne(10));
		where.and(websiteTerminalCode.demRcvd.ne("N/A"));
		where.and(websiteTerminalCode.demRcvd.ne("NA"));
//		where.and(websiteTerminalCode.demRcvd.isNull().or(websiteTerminalCode.demRcvd.eq("")));
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param where
	 */
	private void searchWhere(WebsiteSearchDto paramDto, BooleanBuilder where) {
		if(!KainosStringUtils.isEmpty(paramDto.getHblNo()))
			where.and(websiteTerminalCode.hblNo.contains(paramDto.getHblNo()));
		else if(!KainosStringUtils.isEmpty(paramDto.getHblNo()))
			where.and(websiteTerminalCode.mblNo.contains(paramDto.getMblNo()));
		else {
			
			if(!KainosStringUtils.isEmpty(paramDto.getPartner())) {
				where.and(websiteTerminalCode.partner.eq(paramDto.getPartner()));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getTankNo())) {
				where.and(websiteTerminalCode.tankNo.eq(paramDto.getTankNo()));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getItem())) {
				where.and(websiteTerminalCode.item.eq(paramDto.getItem()).or(websiteTerminalCode.item.eq(paramDto.getCargo())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getProfitDate())) {
				log.error("ProfitDate : {}", paramDto.getProfitDate());
				String[] tmp = paramDto.getProfitDate().split(" - ");
				where.and(websiteTerminalCode.profitDate.goe(tmp[0]).and(websiteTerminalCode.profitDate.loe(tmp[1])));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getErd())) {
				log.error("erd : {}", paramDto.getErd());
				String[] tmp = paramDto.getErd().split(" - ");
				where.and(websiteTerminalCode.estimateReturnDate.goe(tmp[0]).and(websiteTerminalCode.estimateReturnDate.loe(tmp[1])));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getAta())) {
				log.error("Ata : {}", paramDto.getAta());
				String[] tmp = paramDto.getAta().split(" - ");
				where.and(websiteTerminalCode.ata.goe(tmp[0]).and(websiteTerminalCode.ata.loe(tmp[1])));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getReturnDate())) {
				log.error("returnDate : {}", paramDto.getReturnDate());
				String[] tmp = paramDto.getReturnDate().split(" - ");
				where.and(websiteTerminalCode.returnDate.goe(tmp[0]).and(websiteTerminalCode.returnDate.loe(tmp[1])));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getPol())) {
				where.and(websiteTerminalCode.pol.eq(paramDto.getPol()));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getPod())) {
				where.and(websiteTerminalCode.pod.eq(paramDto.getPod()));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getReturnDepot())) {
				where.and(websiteTerminalCode.returnDepot.eq(paramDto.getReturnDepot()));
			}
			
			if(paramDto.getDemRcvdSelect().equalsIgnoreCase("1")) {
				where.and(websiteTerminalCode.demRcvd.eq(paramDto.getDemRcvd()));
			}else if(paramDto.getDemRcvdSelect().equalsIgnoreCase("2")) {
				where.and(websiteTerminalCode.demRcvd.eq("N/A").or(websiteTerminalCode.demRcvd.eq("NA")));
			}else if(paramDto.getDemRcvdSelect().equalsIgnoreCase("3")) {
				where.and(websiteTerminalCode.demRcvd.isNull().or(websiteTerminalCode.demRcvd.eq("")));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getShipmentStatus())) {
				where.and(websiteTerminalCode.shipmentStatus.eq(paramDto.getShipmentStatus()));
			}
		}
	}
	
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<WebsiteDto> selectWebsiteTerminalCode(WebsiteDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getHblNo()))
			where.and(websiteTerminalCode.hblNo.contains(paramDto.getHblNo()));
		
		if(!KainosStringUtils.isEmpty(paramDto.getArrivalNotice()) && (paramDto.getArrivalNotice().equals("1") || paramDto.getArrivalNotice().equals("0")))
			where.and(websiteTerminalCode.arrivalNotice.contains(paramDto.getArrivalNotice()));
		
		return select(Projections.bean(WebsiteDto.class,
					websiteTerminalCode.uuid,
					websiteTerminalCode.seq,
					websiteTerminalCode.sales,
					websiteTerminalCode.carryoverSales,
					new CaseBuilder().when(websiteTerminalCode.arrivalNotice.eq("1")).then(Expressions.constant("SEND")).otherwise(Expressions.constant("")).as("arrivalNotice"),
					new CaseBuilder().when(websiteTerminalCode.invoice.eq("1")).then(Expressions.constant("SEND")).otherwise(Expressions.constant("")).as("invoice"),
					new CaseBuilder().when(mdmCustomer.name.isNull()).then(websiteTerminalCode.concine).otherwise(mdmCustomer.name).as("concineName"),
					mdmCustomer.code.as("concine"),
					mdmCustomer.pic.as("concinePic"),
					mdmCustomer.email.as("concineEmail"),
					websiteTerminalCode.profitDate,
					websiteTerminalCode.domesticSales,
					websiteTerminalCode.foreignSales,
					websiteTerminalCode.quantity,
					websiteTerminalCode.partner,
					websiteTerminalCode.tankNo,
					websiteTerminalCode.term,
					websiteTerminalCode.item,
					websiteTerminalCode.vesselVoyage,
					websiteTerminalCode.carrier,
					websiteTerminalCode.mblNo,
					websiteTerminalCode.hblNo,
					websiteTerminalCode.pol,
//					websiteTerminalCode.pod,
					new CaseBuilder().when(mdmTerminal.region.isNull()).then(websiteTerminalCode.pod.upper()).otherwise(mdmTerminal.region.upper()).as("pod"),
					mdmTerminal.code.as("terminalCode"),
					mdmTerminal.name.as("terminalName"),
//					mdmTerminal.region.upper().as("region"),
					mdmTerminal.homepage.as("terminalHomepage"),
					mdmCargo.code.as("cargo"),
					mdmCargo.cargoDate.upper().as("cargoDate"),
					mdmCargo.location.upper().as("location"),
					new CaseBuilder().when(mdmCargo.name.isNull()).then(websiteTerminalCode.item.upper()).otherwise(mdmCargo.name.upper()).as("item"),
//					ExpressionUtils.as(JPAExpressions.select(terminal.homepage).from(terminal).where(websiteTerminalCode.pod.eq(terminal.region)), "homepage"),
					websiteTerminalCode.etd,
					websiteTerminalCode.eta,
					websiteTerminalCode.ata,
					websiteTerminalCode.remark,
					websiteTerminalCode.ft,
					websiteTerminalCode.demRate,
					websiteTerminalCode.endOfFt,
					websiteTerminalCode.estimateReturnDate,
					websiteTerminalCode.returnDate,
					websiteTerminalCode.demReceived,
					websiteTerminalCode.totalDem,
					websiteTerminalCode.returnDepot,
					websiteTerminalCode.demRcvd,
					websiteTerminalCode.demPrch,
					websiteTerminalCode.demSales,
					websiteTerminalCode.depotInDate,
					websiteTerminalCode.repositionPrch,
					websiteTerminalCode.createUserId,
					Expressions.stringTemplate("to_char({0}, {1})", websiteTerminalCode.createDate, "YYYY-MM-DD").as("createDate"),
					websiteTerminalCode.updateUserId,
					Expressions.stringTemplate("to_char({0}, {1})", websiteTerminalCode.updateDate, "YYYY-MM-DD").as("updateDate")
				)).from(websiteTerminalCode)
				.leftJoin(mdmCargo).on(websiteTerminalCode.item.eq(mdmCargo.code))
				.leftJoin(mdmTerminal).on(websiteTerminalCode.terminal.eq(mdmTerminal.code))
				.leftJoin(mdmCustomer).on(websiteTerminalCode.concine.eq(mdmCustomer.code))
				.where(where)
				.orderBy(websiteTerminalCode.uuid.asc(), websiteTerminalCode.seq.asc())
				.fetch();
	}
	
	/**
	 * 
	 * @param hblNo
	 * @return
	 */
	public String getUuid(String hblNo) {
		return select(websiteTerminalCode.uuid).from(websiteTerminalCode).where(websiteTerminalCode.hblNo.eq(hblNo)).fetchFirst();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void insertWebsiteTerminalCode(WebsiteDto paramDto) throws Exception {
		
		int seq = select(websiteTerminalCode.seq.max().coalesce(0)).from(websiteTerminalCode).where(websiteTerminalCode.uuid.eq(paramDto.getUuid())).fetchFirst() + 1;
		paramDto.setSeq(seq);
		
		insert(websiteTerminalCode)
		.columns(
			websiteTerminalCode.uuid,
			websiteTerminalCode.seq,
			websiteTerminalCode.sales,
			websiteTerminalCode.carryoverSales,
			websiteTerminalCode.arrivalNotice,
			websiteTerminalCode.invoice,
			websiteTerminalCode.concine,
			websiteTerminalCode.profitDate,
			websiteTerminalCode.domesticSales,
			websiteTerminalCode.foreignSales,
			websiteTerminalCode.quantity,
			websiteTerminalCode.partner,
			websiteTerminalCode.tankNo,
			websiteTerminalCode.term,
			websiteTerminalCode.item,
			websiteTerminalCode.vesselVoyage,
			websiteTerminalCode.carrier,
			websiteTerminalCode.mblNo,
			websiteTerminalCode.hblNo,
			websiteTerminalCode.pol,
			websiteTerminalCode.pod,
			websiteTerminalCode.terminal,
			websiteTerminalCode.etd,
			websiteTerminalCode.eta,
			websiteTerminalCode.ata,
			websiteTerminalCode.remark,
			websiteTerminalCode.ft,
			websiteTerminalCode.demRate,
			websiteTerminalCode.endOfFt,
			websiteTerminalCode.estimateReturnDate,
			websiteTerminalCode.returnDate,
			websiteTerminalCode.demReceived,
			websiteTerminalCode.totalDem,
			websiteTerminalCode.returnDepot,
			websiteTerminalCode.demRcvd,
			websiteTerminalCode.demPrch,
			websiteTerminalCode.demSales,
			websiteTerminalCode.depotInDate,
			websiteTerminalCode.repositionPrch,
			websiteTerminalCode.createUserId,
			websiteTerminalCode.createDate,
			websiteTerminalCode.updateUserId,
			websiteTerminalCode.updateDate,
			websiteTerminalCode.shipmentStatus,
			websiteTerminalCode.demStatus
		).values(
			paramDto.getUuid(),
			paramDto.getSeq(),
			paramDto.getSales(),
			paramDto.getCarryoverSales(),
			!KainosStringUtils.isEmpty(paramDto.getArrivalNotice()) ? (paramDto.getArrivalNotice().equals("OK") ? "1" : "0") : "",
			!KainosStringUtils.isEmpty(paramDto.getInvoice()) ? (paramDto.getInvoice() == "OK" ? "1" : "0") : "",
			paramDto.getConcine(),
			paramDto.getProfitDate(),
			!KainosStringUtils.isEmpty(paramDto.getDomesticSales()) ? (paramDto.getDomesticSales().replaceAll("-", "")) : "",
			!KainosStringUtils.isEmpty(paramDto.getForeignSales()) ? (paramDto.getForeignSales().replaceAll("-", "")) : "",
			paramDto.getQuantity(),
			paramDto.getPartner(),
			paramDto.getTankNo(),
			paramDto.getTerm(),
			paramDto.getItem(),
			paramDto.getVesselVoyage(),
			paramDto.getCarrier(),
			paramDto.getMblNo(),
			paramDto.getHblNo(),
			paramDto.getPol(),
			paramDto.getPod(),
			paramDto.getTerminalCode(),
			paramDto.getEtd(),
			paramDto.getEta(),
			paramDto.getAta(),
			paramDto.getRemark(),
			!KainosStringUtils.isEmpty(paramDto.getFt()) ? (paramDto.getFt().replaceAll("-", "")) : "",
			!KainosStringUtils.isEmpty(paramDto.getDemRate()) ? (paramDto.getDemRate().replaceAll("-", "")) : "",
			!KainosStringUtils.isEmpty(paramDto.getEndOfFt()) ? (paramDto.getEndOfFt().replaceAll("N/A", "")) : "",
			paramDto.getEstimateReturnDate(),
			paramDto.getReturnDate(),
			!KainosStringUtils.isEmpty(paramDto.getDemReceived()) ? (paramDto.getDemReceived().replaceAll("N/A", "")) : "",
			!KainosStringUtils.isEmpty(paramDto.getTotalDem()) ? (paramDto.getTotalDem().replaceAll("N/A", "")) : "",
			!KainosStringUtils.isEmpty(paramDto.getReturnDepot()) ? (paramDto.getReturnDepot().replaceAll("-", "")) : "",
			!KainosStringUtils.isEmpty(paramDto.getDemRcvd()) ? (paramDto.getDemRcvd().replaceAll("N/A", "")) : "",
			!KainosStringUtils.isEmpty(paramDto.getDemPrch()) ? (paramDto.getDemPrch().replaceAll("N/A", "")) : "",
			!KainosStringUtils.isEmpty(paramDto.getDemSales()) ? (paramDto.getDemSales().replaceAll("N/A", "")) : "",
			paramDto.getDepotInDate(),
			paramDto.getRepositionPrch(),
			paramDto.getCreateUserId(),
			new Date(),
			paramDto.getCreateUserId(),
			new Date(),
			!KainosStringUtils.isEmpty(paramDto.getShipmentStatus()) ? (paramDto.getShipmentStatus()) : "Y",
			!KainosStringUtils.isEmpty(paramDto.getDemStatus()) ? (paramDto.getDemStatus()) : "Y"
		).execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void updateWebsiteTerminalCode(WebsiteDto paramDto) throws Exception {
		update(websiteTerminalCode)
			.set(websiteTerminalCode.sales,				paramDto.getSales())
			.set(websiteTerminalCode.carryoverSales,      paramDto.getCarryoverSales())
//			.set(websiteTerminalCode.arrivalNotice,       paramDto.getArrivalNotice())
//			.set(websiteTerminalCode.invoice,             paramDto.getInvoice())
			.set(websiteTerminalCode.concine,             paramDto.getConcine())
			.set(websiteTerminalCode.profitDate,          paramDto.getProfitDate())
			.set(websiteTerminalCode.domesticSales,       !KainosStringUtils.isEmpty(paramDto.getDomesticSales()) ? (paramDto.getDomesticSales().replaceAll("US\\$", "")) : "")
			.set(websiteTerminalCode.foreignSales,        !KainosStringUtils.isEmpty(paramDto.getForeignSales()) ? (paramDto.getForeignSales().replaceAll("US\\$", "")) : "")
			.set(websiteTerminalCode.quantity,            paramDto.getQuantity())
			.set(websiteTerminalCode.partner,             paramDto.getPartner())
			.set(websiteTerminalCode.tankNo,              paramDto.getTankNo())
			.set(websiteTerminalCode.term,                paramDto.getTerm())
			.set(websiteTerminalCode.item,                !KainosStringUtils.isEmpty(paramDto.getCargo()) ? paramDto.getCargo() : paramDto.getItem())
			.set(websiteTerminalCode.vesselVoyage,        paramDto.getVesselVoyage())
			.set(websiteTerminalCode.carrier,             paramDto.getCarrier())
			.set(websiteTerminalCode.mblNo,               paramDto.getMblNo())
			.set(websiteTerminalCode.hblNo,               paramDto.getHblNo())
			.set(websiteTerminalCode.pol,                 paramDto.getPol())
			.set(websiteTerminalCode.pod,                 paramDto.getPod())
			.set(websiteTerminalCode.terminal,            paramDto.getTerminalCode())
			.set(websiteTerminalCode.etd,                 paramDto.getEtd())
			.set(websiteTerminalCode.eta,                 paramDto.getEta())
			.set(websiteTerminalCode.ata,                 paramDto.getAta())
			.set(websiteTerminalCode.remark,              paramDto.getRemark())
			.set(websiteTerminalCode.ft,                  paramDto.getFt())
			.set(websiteTerminalCode.demRate,             paramDto.getDemRate())
			.set(websiteTerminalCode.endOfFt,             paramDto.getEndOfFt())
			.set(websiteTerminalCode.estimateReturnDate,  paramDto.getEstimateReturnDate())
			.set(websiteTerminalCode.returnDate,          paramDto.getReturnDate())
			.set(websiteTerminalCode.demReceived,         paramDto.getDemReceived())
			.set(websiteTerminalCode.totalDem,            !KainosStringUtils.isEmpty(paramDto.getTotalDem()) ? paramDto.getTotalDem().replaceAll("US\\$", "") : "")
			.set(websiteTerminalCode.returnDepot,         paramDto.getReturnDepot())
			.set(websiteTerminalCode.demRcvd,             paramDto.getDemRcvd())
			.set(websiteTerminalCode.demPrch,             !KainosStringUtils.isEmpty(paramDto.getDemPrch()) ? paramDto.getDemPrch().replaceAll("US\\$", "") : "")
			.set(websiteTerminalCode.demSales,            !KainosStringUtils.isEmpty(paramDto.getDemSales()) ? paramDto.getDemSales().replaceAll("US\\$", "") : "")
			.set(websiteTerminalCode.depotInDate,         paramDto.getDepotInDate())
			.set(websiteTerminalCode.repositionPrch, 	  paramDto.getRepositionPrch())
			.set(websiteTerminalCode.updateUserId, 		  paramDto.getUpdateUserId())
			.set(websiteTerminalCode.updateDate, 			new Date())
			.set(websiteTerminalCode.shipmentStatus, 		paramDto.getShipmentStatus())
			.set(websiteTerminalCode.demStatus, 			paramDto.getDemStatus())
		.where(websiteTerminalCode.uuid.eq(paramDto.getUuid()).and(websiteTerminalCode.seq.eq(paramDto.getSeq())))
		.execute();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void excelUploadHblNoDelete(String hblNo) throws Exception {
		delete(websiteTerminalCode).where(websiteTerminalCode.hblNo.eq(hblNo)).execute();
	}
	
	/**
	 * 
	 * @param uuid
	 * @param seq
	 * @throws Exception
	 */
	public void deleteWebsiteTerminalCode(String uuid, int seq) throws Exception {
		delete(websiteTerminalCode).where(websiteTerminalCode.uuid.eq(uuid).and(websiteTerminalCode.seq.eq(seq))).execute();
	}
	
}
