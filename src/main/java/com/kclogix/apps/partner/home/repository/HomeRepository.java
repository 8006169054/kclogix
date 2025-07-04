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

import com.kclogix.apps.management.website.dto.WebsiteSearchDto;
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
	public List<HomeExcelDownDto> selectWebsiteTerminalCodeExcel(WebsiteSearchDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
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
				String[] tmp = paramDto.getProfitDate().split("~");
				where.and(websiteTerminalCode.profitDate.goe(tmp[0].trim()).and(websiteTerminalCode.profitDate.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getErd())) {
				String[] tmp = paramDto.getErd().split("~");
				where.and(websiteTerminalCode.estimateReturnDate.goe(tmp[0].trim()).and(websiteTerminalCode.estimateReturnDate.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getAta())) {
				String[] tmp = paramDto.getAta().split("~");
				where.and(websiteTerminalCode.ata.goe(tmp[0].trim()).and(websiteTerminalCode.ata.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getReturnDate())) {
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
				String[] tmp = paramDto.getEta().split("~");
				where.and(websiteTerminalCode.eta.goe(tmp[0].trim()).and(websiteTerminalCode.eta.loe(tmp[1].trim())));
			}
		}
		
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
	public List<HomeDto> selectWebsiteTerminalCode(WebsiteSearchDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
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
				String[] tmp = paramDto.getProfitDate().split("~");
				where.and(websiteTerminalCode.profitDate.goe(tmp[0].trim()).and(websiteTerminalCode.profitDate.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getErd())) {
				String[] tmp = paramDto.getErd().split("~");
				where.and(websiteTerminalCode.estimateReturnDate.goe(tmp[0].trim()).and(websiteTerminalCode.estimateReturnDate.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getAta())) {
				String[] tmp = paramDto.getAta().split("~");
				where.and(websiteTerminalCode.ata.goe(tmp[0].trim()).and(websiteTerminalCode.ata.loe(tmp[1].trim())));
			}
			
			if(!KainosStringUtils.isEmpty(paramDto.getReturnDate())) {
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
				String[] tmp = paramDto.getEta().split("~");
				where.and(websiteTerminalCode.eta.goe(tmp[0].trim()).and(websiteTerminalCode.eta.loe(tmp[1].trim())));
			}
		}
		
		return select(Projections.bean(HomeDto.class,
					websiteTerminalCode.quantity,
					websiteTerminalCode.partner,
					websiteTerminalCode.tankNo,
					websiteTerminalCode.term,
					websiteTerminalCode.shipmentStatus,
					websiteTerminalCode.profitDate,
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
				.leftJoin(mdmCargo).on(websiteTerminalCode.item.eq(mdmCargo.code))
				.leftJoin(mdmTerminal).on(websiteTerminalCode.terminal.eq(mdmTerminal.code))
				.where(where)
				.orderBy(websiteTerminalCode.uuid.asc())
				.fetch();
	}
	
}
