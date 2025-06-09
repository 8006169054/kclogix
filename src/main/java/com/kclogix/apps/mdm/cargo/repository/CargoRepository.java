package com.kclogix.apps.mdm.cargo.repository;

import static com.kclogix.common.entity.QMdmCargo.mdmCargo;
import static com.kclogix.common.entity.QMdmCargoHistory.mdmCargoHistory;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import com.kclogix.apps.mdm.cargo.dto.CargoDto;
import com.kclogix.common.dto.SelectBoxDto;
import com.kclogix.common.util.CodeGenerationUtil;

@Repository
public class CargoRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param name
	 * @param cargoDate
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public String selectCargoCode(String name, String cargoDate, String location)  throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(name))
			where.and(mdmCargo.name.eq(name.trim().toUpperCase()));
		if(!KainosStringUtils.isEmpty(cargoDate))
			where.and(mdmCargo.cargoDate.upper().eq(cargoDate.trim().toUpperCase()));
		if(!KainosStringUtils.isEmpty(location))
			where.and(mdmCargo.location.upper().eq(location.trim().toUpperCase()));
		
		List<String> list = select(mdmCargo.code).from(mdmCargo).where(where).fetch();
		return list != null ? list.get(0) : null;
	}
	
	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<CargoDto> selectCargo(CargoDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getName()))
			where.and(mdmCargo.name.contains(paramDto.getName()));
		if(!KainosStringUtils.isEmpty(paramDto.getCargoDate()))
			where.and(mdmCargo.cargoDate.contains(paramDto.getCargoDate()));
		if(!KainosStringUtils.isEmpty(paramDto.getLocation()))
			where.and(mdmCargo.location.contains(paramDto.getLocation()));
		
		return select(Projections.bean(CargoDto.class,
				mdmCargo.code,
				mdmCargo.location,
				mdmCargo.name,
				mdmCargo.cargoDate,
				mdmCargo.depot,
				mdmCargo.cleaningCost,
				mdmCargo.difficultLevel,
				mdmCargo.remark1,
				mdmCargo.remark2,
				mdmCargo.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmCargo.createDate, "YYYY-MM-DD").as("createDate"),
				mdmCargo.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmCargo.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(mdmCargo)
				.where(where)
				.fetch();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SelectBoxDto.CarGoAutoComplete> selectAutocomplete() throws Exception {
		return select(Projections.bean(SelectBoxDto.CarGoAutoComplete.class,
				mdmCargo.code,
				mdmCargo.cargoDate,
				mdmCargo.location,
				mdmCargo.name.upper().as("value"),
				mdmCargo.name.upper().concat(" | ").concat(mdmCargo.cargoDate).upper().concat(" | ").concat(mdmCargo.location).upper().as("label")
				))
				.from(mdmCargo)
				.fetch();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param userId
	 * @throws Exception
	 */
	public void insertCargoHistory(CargoDto paramDto, String userId) throws Exception {
		insert(mdmCargoHistory)
		.columns(
				mdmCargoHistory.name,
				mdmCargoHistory.location,
				mdmCargoHistory.cargoDate,
				mdmCargoHistory.depot,
				mdmCargoHistory.cleaningCost,
				mdmCargoHistory.difficultLevel,
				mdmCargoHistory.remark1,
				mdmCargoHistory.remark2,
				mdmCargoHistory.createUserId,
				mdmCargoHistory.createDate,
				mdmCargoHistory.updateUserId,
				mdmCargoHistory.updateDate
		).values(
			paramDto.getName().trim(),
			paramDto.getLocation().trim(),
			paramDto.getCargoDate().trim(),
			paramDto.getDepot().trim(),
			paramDto.getCleaningCost().trim(),
			paramDto.getDifficultLevel().trim(),
			paramDto.getRemark1().trim(),
			paramDto.getRemark2().trim(),
			userId,
			new Date(),
			userId,
			new Date()
		).execute();
	}
	
	public void insertCargo(CargoDto paramDto, String userId) throws Exception {
		insert(mdmCargo)
		.columns(
			mdmCargo.code,
			mdmCargo.name,
			mdmCargo.location,
			mdmCargo.cargoDate,
			mdmCargo.depot,
			mdmCargo.cleaningCost,
			mdmCargo.difficultLevel,
			mdmCargo.remark1,
			mdmCargo.remark2,
			mdmCargo.createUserId,
			mdmCargo.createDate,
			mdmCargo.updateUserId,
			mdmCargo.updateDate
		).values(
			CodeGenerationUtil.createCode("CG"),
			paramDto.getName(),
			paramDto.getLocation(),
			paramDto.getCargoDate(),
			paramDto.getDepot(),
			paramDto.getCleaningCost(),
			paramDto.getDifficultLevel(),
			paramDto.getRemark1(),
			paramDto.getRemark2(),
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
	public void updateCargo(CargoDto paramDto, String userId) throws Exception {
		update(mdmCargo)
			.set(mdmCargo.depot, paramDto.getDepot())
//			.set(mdmCargo.cargoDate, paramDto.getCargoDate())
//			.set(mdmCargo.location, paramDto.getLocation())
			.set(mdmCargo.cleaningCost, paramDto.getCleaningCost())
			.set(mdmCargo.difficultLevel, paramDto.getDifficultLevel())
			.set(mdmCargo.remark1, paramDto.getRemark1())
			.set(mdmCargo.remark2, paramDto.getRemark2())
			.set(mdmCargo.updateUserId, userId)
			.set(mdmCargo.updateDate, new Date())
		.where(mdmCargo.code.eq(paramDto.getCode()))
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @param userId
	 * @throws Exception
	 */
	public void uploadUpdateCargo(CargoDto paramDto, String userId) throws Exception {
		update(mdmCargo)
			.set(mdmCargo.depot, paramDto.getDepot())
			.set(mdmCargo.cleaningCost, paramDto.getCleaningCost())
			.set(mdmCargo.difficultLevel, paramDto.getDifficultLevel())
			.set(mdmCargo.remark1, paramDto.getRemark1())
			.set(mdmCargo.remark2, paramDto.getRemark2())
			.set(mdmCargo.updateUserId, userId)
			.set(mdmCargo.updateDate, new Date())
		.where(mdmCargo.cargoDate.eq(paramDto.getCargoDate()).and(mdmCargo.location.eq(paramDto.getLocation())).and(mdmCargo.name.eq(paramDto.getName())))
		.execute();
	}
	
	/**
	 * 
	 * @param paramDto
	 * @throws Exception
	 */
	public void deleteCargo(CargoDto paramDto) throws Exception {
		delete(mdmCargo).where(mdmCargo.code.eq(paramDto.getCode())).execute();
	}
	
}
