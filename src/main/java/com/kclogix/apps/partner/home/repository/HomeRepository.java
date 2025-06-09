package com.kclogix.apps.partner.home.repository;

import static com.kclogix.common.entity.QComUser.comUser;
import static com.kclogix.common.entity.QMdmCargo.mdmCargo;
import static com.kclogix.common.entity.QMdmPartner.mdmPartner;
import static com.kclogix.common.entity.QMdmTerminal.mdmTerminal;
import static com.kclogix.common.entity.QWebsiteTerminalCode.websiteTerminalCode;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import com.kclogix.apps.partner.home.dto.HomeDto;
import com.kclogix.apps.partner.home.dto.HomeExcelDownDto;

@Repository
public class HomeRepository extends KainosRepositorySupport {
	
	public String selectWebsiteTerminalCodeGridCol(String userId) {
		return select(mdmPartner.importMoniterRoleCode)
		.from(mdmPartner)
		.innerJoin(comUser).on(comUser.id.eq(userId).and(comUser.partnerCode.eq(mdmPartner.code)))
		.fetchOne();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<HomeExcelDownDto> selectWebsiteTerminalCodeExcel(HomeDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getHblNo()))
			where.and(websiteTerminalCode.hblNo.contains(paramDto.getHblNo()));
		
		return select(Projections.bean(HomeExcelDownDto.class,
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
					new CaseBuilder().when(mdmTerminal.region.isNull()).then(websiteTerminalCode.pod.upper()).otherwise(mdmTerminal.region.upper()).as("pod"),
					new CaseBuilder().when(mdmCargo.name.isNull()).then(websiteTerminalCode.item.upper()).otherwise(mdmCargo.name.upper()).as("item"),
					websiteTerminalCode.etd,
					websiteTerminalCode.eta,
					websiteTerminalCode.ft,
					websiteTerminalCode.demRate,
					websiteTerminalCode.endOfFt,
					websiteTerminalCode.returnDate,
					websiteTerminalCode.demReceived,
					websiteTerminalCode.totalDem,
					websiteTerminalCode.demPrch,
					websiteTerminalCode.demSales,
					websiteTerminalCode.depotInDate
				)).from(websiteTerminalCode)
				.innerJoin(mdmPartner).on(mdmPartner.code.eq(paramDto.getPartner()).and(websiteTerminalCode.partner.eq(mdmPartner.name)))
				.leftJoin(mdmCargo).on(websiteTerminalCode.item.eq(mdmCargo.code))
				.leftJoin(mdmTerminal).on(websiteTerminalCode.terminal.eq(mdmTerminal.code))
				.where(where)
				.orderBy(websiteTerminalCode.uuid.asc(), websiteTerminalCode.seq.asc())
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<HomeDto> selectWebsiteTerminalCode(HomeDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getHblNo()))
			where.and(websiteTerminalCode.hblNo.contains(paramDto.getHblNo()));
		
		return select(Projections.bean(HomeDto.class,
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
					new CaseBuilder().when(mdmTerminal.region.isNull()).then(websiteTerminalCode.pod.upper()).otherwise(mdmTerminal.region.upper()).as("pod"),
					new CaseBuilder().when(mdmCargo.name.isNull()).then(websiteTerminalCode.item.upper()).otherwise(mdmCargo.name.upper()).as("item"),
					websiteTerminalCode.etd,
					websiteTerminalCode.eta,
					websiteTerminalCode.ft,
					websiteTerminalCode.demRate,
					websiteTerminalCode.endOfFt,
					websiteTerminalCode.returnDate,
					websiteTerminalCode.demReceived,
					websiteTerminalCode.totalDem,
					websiteTerminalCode.demPrch,
					websiteTerminalCode.demSales,
					websiteTerminalCode.depotInDate
				)).from(websiteTerminalCode)
				.innerJoin(mdmPartner).on(mdmPartner.code.eq(paramDto.getPartner()).and(websiteTerminalCode.partner.eq(mdmPartner.name)))
				.leftJoin(mdmCargo).on(websiteTerminalCode.item.eq(mdmCargo.code))
				.leftJoin(mdmTerminal).on(websiteTerminalCode.terminal.eq(mdmTerminal.code))
				.where(where)
				.orderBy(websiteTerminalCode.uuid.asc(), websiteTerminalCode.seq.asc())
				.fetch();
	}
	
}
