package com.kclogix.apps.common.auth.repository;

import static com.kclogix.common.entity.QComUser.comUser;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import com.kclogix.common.dto.SessionDto;

@Repository
public class AuthRepository extends KainosRepositorySupport {

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
