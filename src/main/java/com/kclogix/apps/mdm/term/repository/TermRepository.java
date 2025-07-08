package com.kclogix.apps.mdm.term.repository;

import static com.kclogix.common.entity.QMdmTerm.mdmTerm;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kclogix.apps.mdm.term.dto.TermDto;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.dto.SessionDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;

@Repository
public class TermRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<TermDto> selectTerm(TermDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getName()))
			where.and(mdmTerm.name.eq(paramDto.getName()));
			where.and(mdmTerm.use.eq("Y"));
		return select(Projections.bean(TermDto.class,
				mdmTerm.id,
				mdmTerm.name,
				mdmTerm.use,
				mdmTerm.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmTerm.createDate, "YYYY-MM-DD").as("createDate"),
				mdmTerm.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmTerm.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(mdmTerm)
				.where(where)
				.orderBy(mdmTerm.id.asc())
				.fetch();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SelectBoxDto.TermAutocomplete> selectTermAutocomplete() throws Exception {
		return select(Projections.bean(SelectBoxDto.TermAutocomplete.class,
				mdmTerm.id,
				mdmTerm.name.as("value"),
				mdmTerm.name.as("label")
				))
				.from(mdmTerm)
				.where(mdmTerm.use.eq("Y"))
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void insertTerm(TermDto paramDto, SessionDto session) throws Exception {
		insert(mdmTerm)
		.columns(
				mdmTerm.id,
				mdmTerm.name,
				mdmTerm.use,
				mdmTerm.createUserId,
				mdmTerm.createDate,
				mdmTerm.updateUserId,
				mdmTerm.updateDate
		).values(
			paramDto.getId(),
			paramDto.getName().trim(),
			"Y",
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
	public void updateTerm(TermDto paramDto, SessionDto session) throws Exception {
		update(mdmTerm)
			.set(mdmTerm.name, paramDto.getName())
			.set(mdmTerm.use, paramDto.getUse())
			.set(mdmTerm.updateUserId, session.getId())
			.set(mdmTerm.updateDate, new Date())
		.where(mdmTerm.id.eq(paramDto.getId()))
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void deleteTerm(TermDto paramDto) throws Exception {
		delete(mdmTerm).where(mdmTerm.id.eq(paramDto.getId())).execute();
	}
	
}
