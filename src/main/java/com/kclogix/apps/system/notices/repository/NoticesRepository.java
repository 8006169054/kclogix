package com.kclogix.apps.system.notices.repository;

import static com.kclogix.common.entity.QComNotices.comNotices;

import java.util.Date;
import java.util.List;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.stereotype.Repository;

import com.kclogix.apps.system.notices.dto.NoticesDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosDateUtil;
import kainos.framework.utils.KainosStringUtils;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NoticesRepository extends KainosRepositorySupport {

	
	
	
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<NoticesDto> selectNotices(NoticesDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getTitle()))
			where.and(comNotices.title.contains(paramDto.getTitle()));
		
		if(!KainosStringUtils.isEmpty(paramDto.getUse()))
			where.and(comNotices.use.contains(paramDto.getUse()));
		
		
		return select(Projections.bean(NoticesDto.class,
				comNotices.id,
				comNotices.title,
				Expressions.stringTemplate("to_char({0}, {1})", comNotices.viewStartDate, "YYYY-MM-DD")
				.concat(" ~ ")
				.concat(Expressions.stringTemplate("to_char({0}, {1})", comNotices.viewEndDate, "YYYY-MM-DD")).as("noticesDate"),
				comNotices.use,
				comNotices.contentBody,
				comNotices.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", comNotices.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(comNotices)
				.where(where)
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param userId
	 * @throws Exception
	 */
	public void insertNotices(NoticesDto paramDto, String userId) throws Exception {
		insert(comNotices)
		.columns(
			comNotices.id,
			comNotices.title,
			comNotices.use,
			comNotices.contentBody,
			comNotices.viewStartDate,
			comNotices.viewEndDate,
			comNotices.createUserId,
			comNotices.createDate,
			comNotices.updateUserId,
			comNotices.updateDate
		).values(
			KainosDateUtil.getCurrentDay("yyyyMMddHHmmssSSS") + new RandomDataGenerator().nextSecureHexString(3),
			paramDto.getTitle(),
			paramDto.getUse(),
			paramDto.getContentBody(),
			KainosDateUtil.string2Date(paramDto.getViewStartDate()),
			KainosDateUtil.string2Date(paramDto.getViewEndDate()),
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
	public void updateNotices(NoticesDto paramDto, String userId) throws Exception {
		update(comNotices)
			.set(comNotices.title, paramDto.getTitle())
			.set(comNotices.use, paramDto.getUse())
			.set(comNotices.contentBody, paramDto.getContentBody())
			.set(comNotices.viewStartDate, KainosDateUtil.string2Date(paramDto.getViewStartDate()))
			.set(comNotices.viewEndDate, KainosDateUtil.string2Date(paramDto.getViewEndDate()))
			.set(comNotices.updateUserId, userId)
			.set(comNotices.updateDate, new Date())
		.where(comNotices.id.eq(paramDto.getId()))
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void deleteNotices(NoticesDto paramDto) throws Exception {
		delete(comNotices).where(comNotices.id.eq(paramDto.getId())).execute();
	}
}
