package com.kclogix.apps.system.user.repository;

import static com.kclogix.common.entity.QComUser.comUser;
import static com.kclogix.common.entity.QMdmPartner.mdmPartner;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import com.kclogix.apps.mdm.partner.repository.PartnerRepository;
import com.kclogix.apps.system.user.dto.ComUserDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository extends KainosRepositorySupport {

	private final PartnerRepository partnerRepo;
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<ComUserDto> selectComUser(ComUserDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getName()))
			where.and(comUser.name.contains(paramDto.getName()));
		
		return select(Projections.bean(ComUserDto.class,
				comUser.id,
				comUser.name,
				comUser.password,
				comUser.mail,
				comUser.activation,
				comUser.type,
				mdmPartner.name.as("partnerCode"),
				comUser.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", comUser.createDate, "YYYY-MM-DD").as("createDate"),
				comUser.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", comUser.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(comUser)
				.leftJoin(mdmPartner).on(comUser.partnerCode.eq(mdmPartner.code))
				.where(where)
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param userId
	 * @throws Exception
	 */
	public void insertComUser(ComUserDto paramDto, String userId) throws Exception {
		insert(comUser)
		.columns(
			comUser.id,
			comUser.name,
			comUser.password,
			comUser.mail,
			comUser.activation,
			comUser.type,
			comUser.partnerCode,
			comUser.createUserId,
			comUser.createDate,
			comUser.updateUserId,
			comUser.updateDate
		).values(
			paramDto.getId(),
			paramDto.getName(),
			paramDto.getPassword(),
			paramDto.getMail(),
			paramDto.getActivation(),
			paramDto.getType(),
			paramDto.getPartnerCode(),
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
	public void updateComUser(ComUserDto paramDto, String userId) throws Exception {
		update(comUser)
			.set(comUser.name, paramDto.getName())
			.set(comUser.mail, paramDto.getMail())
			.set(comUser.activation, paramDto.getActivation())
			.set(comUser.type, paramDto.getType())
			.set(comUser.partnerCode, partnerRepo.selectPartnerCode(paramDto.getPartnerCode()))
			.set(comUser.updateUserId, userId)
			.set(comUser.updateDate, new Date())
		.where(comUser.id.eq(paramDto.getId()))
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void deleteComUser(ComUserDto paramDto) throws Exception {
		delete(comUser).where(comUser.id.eq(paramDto.getId())).execute();
	}
}
