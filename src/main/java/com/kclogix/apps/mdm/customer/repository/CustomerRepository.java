package com.kclogix.apps.mdm.customer.repository;

import static com.kclogix.common.entity.QMdmCustomer.mdmCustomer;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import com.kclogix.apps.mdm.customer.dto.CustomerDto;
import com.kclogix.common.dto.SelectBoxDto;

@Repository
public class CustomerRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param paramDto
	 * @param eq
	 * @return
	 * @throws Exception
	 */
	public List<CustomerDto> selectCustomer(CustomerDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getName()))
				where.and(mdmCustomer.name.contains(paramDto.getName()));
		
		if(!KainosStringUtils.isEmpty(paramDto.getCode()))
			where.and(mdmCustomer.code.contains(paramDto.getCode()));
		
		return select(Projections.bean(CustomerDto.class,
				mdmCustomer.code,
				mdmCustomer.name,
				mdmCustomer.pic,
				mdmCustomer.email,
				mdmCustomer.tel,
				mdmCustomer.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmCustomer.createDate, "YYYY-MM-DD").as("createDate"),
				mdmCustomer.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmCustomer.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(mdmCustomer)
				.where(where)
				.orderBy(mdmCustomer.updateDate.desc())
				.fetch();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SelectBoxDto.CustomerAutoComplete> selectCustomerAutocomplete() throws Exception {
		return select(Projections.bean(SelectBoxDto.CustomerAutoComplete.class,
				mdmCustomer.name.as("value"),
				mdmCustomer.name.as("label"),
				mdmCustomer.pic,
				mdmCustomer.code,
				mdmCustomer.tel,
				mdmCustomer.email
				))
				.from(mdmCustomer)
				.where(mdmCustomer.pic.isNotEmpty())
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void insertCustomer(CustomerDto paramDto) throws Exception {
		insert(mdmCustomer)
		.columns(
			mdmCustomer.code,
			mdmCustomer.name,
			mdmCustomer.email,
			mdmCustomer.pic,
			mdmCustomer.tel,
			mdmCustomer.createUserId,
			mdmCustomer.createDate,
			mdmCustomer.updateUserId,
			mdmCustomer.updateDate
		).values(
			paramDto.getCode().trim(),
			paramDto.getName().trim(),
			paramDto.getEmail().trim(),
			paramDto.getPic().trim(),
			paramDto.getTel(),
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
	public void updateCustomer(CustomerDto paramDto) throws Exception {
		update(mdmCustomer)
			.set(mdmCustomer.name, paramDto.getName())
			.set(mdmCustomer.email, paramDto.getEmail())
			.set(mdmCustomer.pic, paramDto.getPic())
			.set(mdmCustomer.tel, paramDto.getTel())
			.set(mdmCustomer.updateUserId, paramDto.getUpdateUserId())
			.set(mdmCustomer.updateDate, new Date())
		.where(mdmCustomer.code.eq(paramDto.getCode()))
		.execute();
	}
	
	/**
	 * 
	 * @param dto
	 * @throws Exception
	 */
	public void deleteCustomer(CustomerDto dto) throws Exception {
		delete(mdmCustomer).where(mdmCustomer.code.eq(dto.getCode())).execute();
	}
	
}
