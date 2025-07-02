package com.kclogix.apps.common.auth.repository;

import static com.kclogix.common.entity.QComNotices.comNotices;
import static com.kclogix.common.entity.QComUser.comUser;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kclogix.apps.common.auth.dto.OpenNoticesDto;
import com.kclogix.common.dto.SessionDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;

@Repository
public class AuthRepository extends KainosRepositorySupport {

	public List<OpenNoticesDto> selectOpenNotices() throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		where.and(comNotices.use.eq("Y"));
		where.and(comNotices.viewStartDate.loe(new Date()).and(comNotices.viewEndDate.goe(new Date())));
		
		return select(Projections.bean(OpenNoticesDto.class,
				comNotices.title,
				comNotices.contentBody
				))
				.from(comNotices)
				.where(where)
				.fetch();
	}
	
	/**
	 * 
	 * @param id
	 * @param pw
	 * @return
	 * @throws Exception
	 */
	public SessionDto dbLogin(String id, String password) throws Exception {
		return select(Projections.bean(SessionDto.class
				, comUser.id
				, comUser.name
				, comUser.type
				, comUser.activation
				, comUser.partnerCode
				, comUser.mail
				))
				.from(comUser)
				.where(comUser.id.eq(id).and(comUser.password.eq(password)))
				.fetchOne();
	}
//	
//	/**
//	 * 
//	 * @param id
//	 * @param pw
//	 * @return
//	 * @throws Exception
//	 */
//	public SessionDto imlogin(String id, String pw) throws Exception {
//		return null;
//	}
}
