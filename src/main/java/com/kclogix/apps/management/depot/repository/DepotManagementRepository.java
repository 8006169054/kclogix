package com.kclogix.apps.management.depot.repository;

import static com.kclogix.common.entity.QDepotManagement.depotManagement;
import static com.kclogix.common.entity.QMdmCargo.mdmCargo;
import static com.kclogix.common.entity.QWebsiteTerminalCode.websiteTerminalCode;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kclogix.apps.management.depot.dto.DepotManagementDto;
import com.kclogix.common.dto.SessionDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAUpdateClause;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;

@Repository
public class DepotManagementRepository extends KainosRepositorySupport {

	
	/**
	 * 
	 * @param paramDto
	 * @param session
	 * @throws Exception
	 */
	public void dateExcelupload(DepotManagementDto searchDto, DepotManagementDto.DateExcelUpload excelDto, SessionDto session) throws Exception {
		long cnt = select(depotManagement.uuid.count()).from(depotManagement).where(
				depotManagement.uuid.eq(searchDto.getUuid())
				.and(depotManagement.seq.eq(searchDto.getSeq()))
				.and(depotManagement.hblNo.eq(searchDto.getHblNo()))
				.and(depotManagement.tankNo.eq(searchDto.getTankNo()))
				).fetchFirst();
		
		if(cnt > 0) {
			JPAUpdateClause jpaUpate = update(depotManagement)
					.set(depotManagement.updateUserId, session.getId())
					.set(depotManagement.updateDate, new Date());
			
			if (!KainosStringUtils.isEmpty(excelDto.getCleanedDate())) {
				jpaUpate.set(depotManagement.cleanedDate, excelDto.getCleanedDate());
			}
			if (!KainosStringUtils.isEmpty(excelDto.getOutDate())) {
				jpaUpate.set(depotManagement.outDate, excelDto.getOutDate());
			}
			jpaUpate.where(
					depotManagement.uuid.eq(searchDto.getUuid())
					.and(depotManagement.seq.eq(searchDto.getSeq()))
					.and(depotManagement.hblNo.eq(searchDto.getHblNo()))
					.and(depotManagement.tankNo.eq(searchDto.getTankNo()))
			).execute();
		}
		else {
			searchDto.setCleanedDate(excelDto.getCleanedDate());
			searchDto.setOutDate(excelDto.getOutDate());
			insertDepotManagement(searchDto, session);
		}
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public long countDepotManagement(DepotManagementDto paramDto) throws Exception {
		return select(depotManagement.uuid.count())
		.from(depotManagement)
		.where(
				depotManagement.uuid.eq(paramDto.getUuid())
				.and(depotManagement.seq.eq(paramDto.getSeq()))
				.and(depotManagement.hblNo.eq(paramDto.getHblNo()))
				.and(depotManagement.tankNo.eq(paramDto.getTankNo()))
				)
		.fetchOne();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<DepotManagementDto> selectDepotManagement(DepotManagementDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		where.and(websiteTerminalCode.delFlg.ne("Y").or(websiteTerminalCode.delFlg.isNull()));
		
		if(!KainosStringUtils.isEmpty(paramDto.getHblNo()))
			where.and(websiteTerminalCode.hblNo.upper().like(paramDto.getHblNo().toUpperCase() + "%"));
		
		if(!KainosStringUtils.isEmpty(paramDto.getTankNo()))
			where.and(websiteTerminalCode.tankNo.upper().eq(paramDto.getTankNo().toUpperCase()));
		
		if(!KainosStringUtils.isEmpty(paramDto.getPartner()))
			where.and(websiteTerminalCode.partner.upper().like(paramDto.getPartner().toUpperCase() + "%"));
		
		if(!KainosStringUtils.isEmpty(paramDto.getItem()))
			where.and(websiteTerminalCode.item.upper().like(paramDto.getItem().toUpperCase() + "%"));
		
		if(!KainosStringUtils.isEmpty(paramDto.getReturnDepot()))
			where.and(websiteTerminalCode.returnDepot.upper().like(paramDto.getReturnDepot().toUpperCase() + "%"));
		
		
		if(!KainosStringUtils.isEmpty(paramDto.getReturnDate())) {
			String[] tmp = paramDto.getReturnDate().split("~");
			where.and(websiteTerminalCode.returnDate.goe(tmp[0].trim()).and(websiteTerminalCode.returnDate.loe(tmp[1].trim())));
		}
		
		if(!KainosStringUtils.isEmpty(paramDto.getCleanedDate())) {
			String[] tmp = paramDto.getCleanedDate().split("~");
			where.and(depotManagement.cleanedDate.goe(tmp[0].trim()).and(depotManagement.cleanedDate.loe(tmp[1].trim())));
		}
		
		if(!KainosStringUtils.isEmpty(paramDto.getOutDate())) {
			String[] tmp = paramDto.getOutDate().split("~");
			where.and(depotManagement.outDate.goe(tmp[0].trim()).and(depotManagement.outDate.loe(tmp[1].trim())));
		}
		
		if(!KainosStringUtils.isEmpty(paramDto.getRemark()))
			where.and(depotManagement.remark.upper().like(paramDto.getRemark().toUpperCase() + "%"));
		
		if(!KainosStringUtils.isEmpty(paramDto.getAllocation())) {
			if(paramDto.getAllocation().equalsIgnoreCase("0"))
				where.and(depotManagement.allocation.isEmpty().or(depotManagement.allocation.isNull()));
			else
				where.and(depotManagement.allocation.isNotEmpty().or(depotManagement.allocation.isNotNull()));
		}
		
		return select(Projections.bean(DepotManagementDto.class,
				websiteTerminalCode.uuid,
				websiteTerminalCode.seq,
				websiteTerminalCode.hblNo,
				websiteTerminalCode.tankNo,
				websiteTerminalCode.partner,
				new CaseBuilder().when(mdmCargo.name.isNull()).then(websiteTerminalCode.item.upper()).otherwise(mdmCargo.name.upper()).as("item"),
				websiteTerminalCode.returnDepot,
				websiteTerminalCode.returnDate,
				depotManagement.cleanedDate,
				depotManagement.outDate,
				depotManagement.allocation,
				depotManagement.remark,
				depotManagement.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", depotManagement.createDate, "YYYY-MM-DD").as("createDate"),
				depotManagement.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", depotManagement.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(websiteTerminalCode)
				.leftJoin(depotManagement).on(
						websiteTerminalCode.uuid.eq(depotManagement.uuid)
						.and(websiteTerminalCode.seq.eq(depotManagement.seq))
						.and(websiteTerminalCode.hblNo.eq(depotManagement.hblNo))
				)
				.leftJoin(mdmCargo).on(websiteTerminalCode.item.eq(mdmCargo.code))
				.where(where)
				.orderBy(websiteTerminalCode.uuid.desc(),websiteTerminalCode.seq.asc())
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void insertDepotManagement(DepotManagementDto paramDto, SessionDto session) throws Exception {
		insert(depotManagement)
		.columns(
			depotManagement.uuid,
			depotManagement.seq,
			depotManagement.hblNo,
			depotManagement.tankNo,
			depotManagement.partner,
			depotManagement.item,
			depotManagement.returnDepot,
			depotManagement.returnDate,
			depotManagement.cleanedDate,
			depotManagement.outDate,
			depotManagement.allocation,
			depotManagement.remark,
			depotManagement.createUserId,
			depotManagement.createDate,
			depotManagement.updateUserId,
			depotManagement.updateDate
		).values(
			paramDto.getUuid(),
			paramDto.getSeq(),
			paramDto.getHblNo(),
			paramDto.getTankNo(),
			paramDto.getPartner(),
			paramDto.getItem(),
			paramDto.getReturnDepot(),
			paramDto.getReturnDate(),
			paramDto.getCleanedDate(),
			paramDto.getOutDate(),
			paramDto.getAllocation(),
			paramDto.getRemark(),
			session.getId(),
			new Date(),
			session.getId(),
			new Date()
		).execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void updateDepotManagement(DepotManagementDto paramDto, SessionDto session) throws Exception {
		update(depotManagement)
			.set(depotManagement.cleanedDate, paramDto.getCleanedDate())
			.set(depotManagement.outDate, paramDto.getOutDate())
			.set(depotManagement.allocation, paramDto.getAllocation())
			.set(depotManagement.remark, paramDto.getRemark())
			.set(depotManagement.updateUserId, session.getId())
			.set(depotManagement.updateDate, new Date())
		.where(
				depotManagement.uuid.eq(paramDto.getUuid())
				.and(depotManagement.seq.eq(paramDto.getSeq()))
				.and(depotManagement.hblNo.eq(paramDto.getHblNo()))
				.and(depotManagement.tankNo.eq(paramDto.getTankNo()))
				)
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void deleteDepotManagemen(DepotManagementDto paramDto) throws Exception {
		delete(depotManagement).where(
				depotManagement.uuid.eq(paramDto.getUuid())
				.and(depotManagement.seq.eq(paramDto.getSeq()))
				.and(depotManagement.hblNo.eq(paramDto.getHblNo()))
				.and(depotManagement.tankNo.eq(paramDto.getTankNo()))
				)
		.execute();
	}
	
}
