package com.kclogix.apps.mdm.partner.repository;

import static com.kclogix.common.entity.QMdmPartner.mdmPartner;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import com.kclogix.apps.mdm.partner.dto.PartnerDto;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.util.CodeGenerationUtil;

@Repository
public class PartnerRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param partnerName
	 * @return
	 * @throws Exception
	 */
	public List<String> selectMonitorColNames(String partnerName) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(partnerName)) where.and(mdmPartner.name.eq(partnerName));
		return select(mdmPartner.name)
				.from(mdmPartner)
				.where(where)
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param eq
	 * @return
	 * @throws Exception
	 */
	public List<PartnerDto> selectPartner(PartnerDto paramDto, boolean eq) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getName()))
			if(!eq)
				where.and(mdmPartner.name.contains(paramDto.getName()));
			else
				where.and(mdmPartner.name.eq(paramDto.getName()));
		
		return select(Projections.bean(PartnerDto.class,
				mdmPartner.code,
				mdmPartner.name,
				mdmPartner.company,
				mdmPartner.pic,
				mdmPartner.representativeEml,
				mdmPartner.importMoniterRoleCode,
				mdmPartner.importMoniterRoleName,
				mdmPartner.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmPartner.createDate, "YYYY-MM-DD").as("createDate"),
				mdmPartner.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmPartner.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(mdmPartner)
				.where(where)
				.orderBy(mdmPartner.code.asc())
				.fetch();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String selectPartnerCode(String name) throws Exception {
		return select(
				mdmPartner.code
				)
				.from(mdmPartner)
				.where(mdmPartner.name.eq(name))
				.fetchOne();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SelectBoxDto.PartnerAutocomplete> selectPartnerAutocomplete() throws Exception {
		return select(Projections.bean(SelectBoxDto.PartnerAutocomplete.class,
				mdmPartner.name.as("value"),
				mdmPartner.name.as("label")
				))
				.from(mdmPartner)
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void insertPartner(PartnerDto paramDto) throws Exception {
		insert(mdmPartner)
		.columns(
				mdmPartner.code,
				mdmPartner.name,
				mdmPartner.company,
				mdmPartner.pic,
				mdmPartner.representativeEml,
				mdmPartner.importMoniterRoleCode,
				mdmPartner.importMoniterRoleName,
				mdmPartner.createUserId,
				mdmPartner.createDate,
				mdmPartner.updateUserId,
				mdmPartner.updateDate
		).values(
			CodeGenerationUtil.createCode("PA"),
			paramDto.getName().trim(),
			paramDto.getCompany().trim(),
			paramDto.getPic().trim(),
			paramDto.getRepresentativeEml().trim(),
			paramDto.getImportMoniterRoleCode(),
			paramDto.getImportMoniterRoleName(),
			paramDto.getCreateUserId(),
			new Date(),
			paramDto.getUpdateUserId(),
			new Date()
		).execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void updatePartner(PartnerDto paramDto) throws Exception {
		update(mdmPartner)
			.set(mdmPartner.name, paramDto.getName())
			.set(mdmPartner.company, paramDto.getCompany())
			.set(mdmPartner.pic, paramDto.getPic())
			.set(mdmPartner.representativeEml, paramDto.getRepresentativeEml())
			.set(mdmPartner.importMoniterRoleCode, paramDto.getImportMoniterRoleCode())
			.set(mdmPartner.importMoniterRoleName, paramDto.getImportMoniterRoleName())
			.set(mdmPartner.updateUserId, paramDto.getUpdateUserId())
			.set(mdmPartner.updateDate, new Date())
		.where(mdmPartner.code.eq(paramDto.getCode()))
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void deletePartner(PartnerDto paramDto) throws Exception {
		delete(mdmPartner).where(mdmPartner.code.eq(paramDto.getCode())).execute();
	}
	
}
