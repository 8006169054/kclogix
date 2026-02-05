package com.kclogix.apps.management.arrival.repository;

import static com.kclogix.common.entity.QMdmCargo.mdmCargo;
import static com.kclogix.common.entity.QMdmCustomer.mdmCustomer;
import static com.kclogix.common.entity.QMdmTerm.mdmTerm;
import static com.kclogix.common.entity.QMdmTerminal.mdmTerminal;
import static com.kclogix.common.entity.QWebsiteTerminalCode.websiteTerminalCode;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kclogix.apps.management.website.dto.WebsiteDto;
import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
import com.kclogix.common.dto.SessionDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAUpdateClause;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ArrivalRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public void arrivalNoticeSendMail(WebsiteSearchDto paramDto) throws Exception {
		update(websiteTerminalCode)
		 .set(websiteTerminalCode.arrivalNotice, "1")
		 .set(websiteTerminalCode.arrivalNoticeEmail, paramDto.getConcineEmail())
		 .where(websiteTerminalCode.hblNo.eq(paramDto.getHblNo()))
		 .execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param where
	 */
	private void searchWhere(WebsiteSearchDto paramDto, BooleanBuilder where) {
		if(!KainosStringUtils.isEmpty(paramDto.getHblNo()))
			where.and(websiteTerminalCode.hblNo.contains(paramDto.getHblNo()));
		else if(!KainosStringUtils.isEmpty(paramDto.getMblNo()))
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
				String[] tmp = paramDto.getProfitDate().split("~");
				where.and(websiteTerminalCode.profitDate.goe(tmp[0].trim()).and(websiteTerminalCode.profitDate.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getErd())) {
				log.error("erd : {}", paramDto.getErd());
				String[] tmp = paramDto.getErd().split("~");
				where.and(websiteTerminalCode.estimateReturnDate.goe(tmp[0].trim()).and(websiteTerminalCode.estimateReturnDate.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getAta())) {
				log.error("Ata : {}", paramDto.getAta());
				String[] tmp = paramDto.getAta().split("~");
				where.and(websiteTerminalCode.ata.goe(tmp[0].trim()).and(websiteTerminalCode.ata.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getReturnDate())) {
				log.error("returnDate : {}", paramDto.getReturnDate());
				String[] tmp = paramDto.getReturnDate().split("~");
				where.and(websiteTerminalCode.returnDate.goe(tmp[0].trim()).and(websiteTerminalCode.returnDate.loe(tmp[1].trim())));
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
			
			if(!KainosStringUtils.isEmpty(paramDto.getDemRcvdSelect())) {
				if(paramDto.getDemRcvdSelect().equalsIgnoreCase("1")) {
					where.and(websiteTerminalCode.demRcvd.eq(paramDto.getDemRcvd()));
				}else if(paramDto.getDemRcvdSelect().equalsIgnoreCase("2")) {
					where.and(websiteTerminalCode.demRcvd.eq("N/A").or(websiteTerminalCode.demRcvd.eq("NA")));
				}else if(paramDto.getDemRcvdSelect().equalsIgnoreCase("3")) {
					where.and(websiteTerminalCode.demRcvd.isNull().or(websiteTerminalCode.demRcvd.eq("")));
				}
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getShipmentStatus())) {
				where.and(websiteTerminalCode.shipmentStatus.eq(paramDto.getShipmentStatus()));
			}
			
			
			if(!KainosStringUtils.isEmpty(paramDto.getArrivalNotice())) {
				if(paramDto.getArrivalNotice().equalsIgnoreCase("0")) {
					where.and(websiteTerminalCode.arrivalNotice.eq(paramDto.getArrivalNotice()).or(websiteTerminalCode.arrivalNotice.eq("")));
				}else {
					where.and(websiteTerminalCode.arrivalNotice.eq(paramDto.getArrivalNotice()));
				}
				
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getEta())) {
				log.error("Eta : {}", paramDto.getEta());
				String[] tmp = paramDto.getEta().split("~");
				where.and(websiteTerminalCode.eta.goe(tmp[0].trim()).and(websiteTerminalCode.eta.loe(tmp[1].trim())));
			}
		}
	}
	
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<WebsiteDto> selectArrivalnotice(WebsiteSearchDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		where.and(websiteTerminalCode.delFlg.ne("Y").or(websiteTerminalCode.delFlg.isNull()));
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
					new CaseBuilder().when(websiteTerminalCode.arrivalNoticeEmail.isNull()).then(mdmCustomer.email).otherwise(websiteTerminalCode.arrivalNoticeEmail).as("concineEmail"),
					websiteTerminalCode.profitDate,
					websiteTerminalCode.domesticSales,
					websiteTerminalCode.foreignSales,
					websiteTerminalCode.shipmentStatus,
					websiteTerminalCode.quantity,
					websiteTerminalCode.partner,
					websiteTerminalCode.tankNo,
					new CaseBuilder().when(mdmTerm.name.isNull()).then(websiteTerminalCode.term.upper()).otherwise(mdmTerm.name.upper()).as("term"),
					websiteTerminalCode.item,
					websiteTerminalCode.vesselVoyage,
					websiteTerminalCode.carrier,
					websiteTerminalCode.mblNo,
					websiteTerminalCode.hblNo,
					websiteTerminalCode.pol,
					new CaseBuilder().when(mdmTerminal.region.isNull()).then(websiteTerminalCode.pod.upper()).otherwise(mdmTerminal.region.upper()).as("pod"),
					mdmTerminal.code.as("terminalCode"),
					mdmTerminal.parkingLotCode,
					mdmTerminal.name.as("terminalName"),
					mdmTerminal.homepage.as("terminalHomepage"),
					mdmCargo.code.as("cargo"),
					mdmCargo.cargoDate.upper().as("cargoDate"),
					mdmCargo.location.upper().as("location"),
					new CaseBuilder().when(mdmCargo.name.isNull()).then(websiteTerminalCode.item.upper()).otherwise(mdmCargo.name.upper()).as("item"),
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
				.leftJoin(mdmTerm).on(websiteTerminalCode.term.eq(mdmTerm.id))
				.where(where)
				.orderBy(websiteTerminalCode.uuid.asc(), websiteTerminalCode.seq.asc())
				.fetch();
	}
	
//	.set(websiteTerminalCode.pod,                 paramDto.getPod())
//	.set(websiteTerminalCode.terminal,            paramDto.getTerminalCode())
	
	public void updateTerminalCode(WebsiteSearchDto paramDto) throws Exception {
		JPAUpdateClause update = update(websiteTerminalCode);
		if(!KainosStringUtils.isEmpty(paramDto.getPod()))
				update.set(websiteTerminalCode.pod, paramDto.getPod());
		if(!KainosStringUtils.isEmpty(paramDto.getTerminalCode()))
			update.set(websiteTerminalCode.terminal, paramDto.getTerminalCode());
		
		update.where(websiteTerminalCode.hblNo.eq(paramDto.getHblNo()))
		 .execute();
	}
	
	/**
	 * 
	 * @param paramList
	 * @param session
	 * @throws Exception
	 */
	public void arrivalnoticeSave(List<WebsiteDto> paramList, SessionDto session)throws Exception {
		for (Iterator<WebsiteDto> iterator = paramList.iterator(); iterator.hasNext();) {
			WebsiteDto paramDto = iterator.next();
			update(websiteTerminalCode)
			.set(websiteTerminalCode.eta, paramDto.getEta())
			.set(websiteTerminalCode.etd, paramDto.getEtd())
			.where(websiteTerminalCode.uuid.eq(paramDto.getUuid()).and(websiteTerminalCode.seq.eq(paramDto.getSeq())))
			.execute();
		}
	}
	
}
