package com.kclogix.apps.mdm.terminal.repository;

import static com.kclogix.common.entity.QMdmTerminal.mdmTerminal;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import com.kclogix.apps.mdm.terminal.dto.TerminalDto;
import com.kclogix.common.dto.SelectBoxDto;

@Repository
public class TerminalRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<TerminalDto> selectTerminal(TerminalDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getCode()))
			where.and(mdmTerminal.code.contains(paramDto.getCode()));
		if(!KainosStringUtils.isEmpty(paramDto.getName()))
			where.and(mdmTerminal.name.contains(paramDto.getName()));
		if(!KainosStringUtils.isEmpty(paramDto.getRegion()))
			where.and(mdmTerminal.region.contains(paramDto.getRegion()));
		
		return select(Projections.bean(TerminalDto.class,
				mdmTerminal.code,
				mdmTerminal.name,
				mdmTerminal.region,
				mdmTerminal.type,
				mdmTerminal.parkingLotCode,
				mdmTerminal.homepage,
				mdmTerminal.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmTerminal.createDate, "YYYY-MM-DD").as("createDate"),
				mdmTerminal.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmTerminal.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(mdmTerminal)
				.where(where)
				.fetch();
		
//		return selectFrom(terminal).where(where).fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param userId
	 * @throws Exception
	 */
	public void insertTerminal(TerminalDto paramDto, String userId) throws Exception {
		insert(mdmTerminal)
		.columns(
			mdmTerminal.code,
			mdmTerminal.name,
			mdmTerminal.region,
			mdmTerminal.type,
			mdmTerminal.parkingLotCode,
			mdmTerminal.homepage,
			mdmTerminal.createUserId,
			mdmTerminal.createDate,
			mdmTerminal.updateUserId,
			mdmTerminal.updateDate
		).values(
			paramDto.getCode(),
			paramDto.getName(),
			paramDto.getRegion(),
			paramDto.getType(),
			paramDto.getParkingLotCode().trim(),
			paramDto.getHomepage(),
			userId,
			new Date(),
			userId,
			new Date()
		).execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param userId
	 * @throws Exception
	 */
	public void updateTerminal(TerminalDto paramDto, String userId) throws Exception {
		update(mdmTerminal)
			.set(mdmTerminal.name, paramDto.getName())
			.set(mdmTerminal.region, paramDto.getRegion())
			.set(mdmTerminal.type, paramDto.getType())
			.set(mdmTerminal.parkingLotCode, paramDto.getParkingLotCode().trim())
			.set(mdmTerminal.homepage, paramDto.getHomepage())
			.set(mdmTerminal.updateUserId, userId)
			.set(mdmTerminal.updateDate, new Date())
		.where(mdmTerminal.code.eq(paramDto.getCode()))
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void deleteTerminal(TerminalDto paramDto) throws Exception {
		delete(mdmTerminal).where(mdmTerminal.code.eq(paramDto.getCode())).execute();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SelectBoxDto.TerminalAutoComplete> selectAutocomplete() throws Exception {
		return select(Projections.bean(SelectBoxDto.TerminalAutoComplete.class,
				mdmTerminal.code,
				mdmTerminal.region,
				mdmTerminal.homepage,
				mdmTerminal.name.upper().as("value"),
				mdmTerminal.name.upper().as("label")
				))
				.from(mdmTerminal)
				.fetch();
	}
}
