package com.kclogix.apps.mdm.depot.repository;

import static com.kclogix.common.entity.QMdmDepot.mdmDepot;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kclogix.apps.mdm.depot.dto.DepotDto;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;

@Repository
public class DepotRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<DepotDto> selectDepot(DepotDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getLocation()))
			where.and(mdmDepot.location.upper().like(paramDto.getLocation().toUpperCase() + "%"));
		
		if(!KainosStringUtils.isEmpty(paramDto.getDepotNameEn()))
			where.and(
					mdmDepot.depotNameEn.upper().like(paramDto.getDepotNameEn().toUpperCase() + "%")
					.or(mdmDepot.depotNameKr.upper().like(paramDto.getDepotNameEn().toUpperCase() + "%"))
					);
		
		if(!KainosStringUtils.isEmpty(paramDto.getPicName()))
			where.and(mdmDepot.picName.upper().like(paramDto.getPicName().toUpperCase() + "%"));
		
		if(!KainosStringUtils.isEmpty(paramDto.getPicTel()))
			where.and(mdmDepot.picTel.upper().like(paramDto.getPicTel().toUpperCase() + "%"));
		
		if(!KainosStringUtils.isEmpty(paramDto.getPicEmail()))
			where.and(mdmDepot.picEmail.upper().like(paramDto.getPicEmail().toUpperCase() + "%"));
		
		return select(Projections.bean(DepotDto.class,
				mdmDepot.depotCode,
				mdmDepot.location,
				mdmDepot.depotNameEn,
				mdmDepot.depotNameKr,
				mdmDepot.address,
				mdmDepot.picName,
				mdmDepot.picTel,
				mdmDepot.picEmail,
				mdmDepot.operationRemark,
				mdmDepot.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmDepot.createDate, "YYYY-MM-DD").as("createDate"),
				mdmDepot.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmDepot.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(mdmDepot)
				.where(where)
				.orderBy(mdmDepot.depotCode.asc())
				.fetch();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SelectBoxDto.DepotAutocomplete> selectDepotAutocomplete() throws Exception {
		return select(Projections.bean(SelectBoxDto.DepotAutocomplete.class,
				mdmDepot.depotCode,
				mdmDepot.depotCode.as("value"),
				mdmDepot.depotCode.as("label")
				))
				.from(mdmDepot)
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void insertDepot(DepotDto paramDto, SessionDto session) throws Exception {
		insert(mdmDepot)
		.columns(
				mdmDepot.depotCode,
				mdmDepot.location,
				mdmDepot.depotNameEn,
				mdmDepot.depotNameKr,
				mdmDepot.address,
				mdmDepot.picName,
				mdmDepot.picTel,
				mdmDepot.picEmail,
				mdmDepot.operationRemark,
				mdmDepot.createUserId,
				mdmDepot.createDate,
				mdmDepot.updateUserId,
				mdmDepot.updateDate
		).values(
			paramDto.getDepotCode(),
			paramDto.getLocation(),
			paramDto.getDepotNameEn(),
			paramDto.getDepotNameKr(),
			paramDto.getAddress(),
			paramDto.getPicName(),
			paramDto.getPicTel(),
			paramDto.getPicEmail(),
			paramDto.getOperationRemark(),
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
	public void updateDepot(DepotDto paramDto, SessionDto session) throws Exception {
		update(mdmDepot)
			.set(mdmDepot.location, paramDto.getLocation())
			.set(mdmDepot.depotNameEn, paramDto.getDepotNameEn())
			.set(mdmDepot.depotNameKr, paramDto.getDepotNameKr())
			.set(mdmDepot.address, paramDto.getAddress())
			.set(mdmDepot.picName, paramDto.getPicName())
			.set(mdmDepot.picTel, paramDto.getPicTel())
			.set(mdmDepot.picEmail, paramDto.getPicEmail())
			.set(mdmDepot.operationRemark, paramDto.getOperationRemark())
			.set(mdmDepot.updateUserId, session.getId())
			.set(mdmDepot.updateDate, new Date())
		.where(mdmDepot.depotCode.eq(paramDto.getDepotCode()))
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void deleteDepot(DepotDto paramDto) throws Exception {
		delete(mdmDepot).where(mdmDepot.depotCode.eq(paramDto.getDepotCode())).execute();
	}
	
}
