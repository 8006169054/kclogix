package com.kclogix.apps.history.repository;

import static com.kclogix.common.entity.QMdmCargoHistory.mdmCargoHistory;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosStringUtils;
import com.kclogix.apps.history.dto.HistoryDto;

@Repository
public class HistoryRepository extends KainosRepositorySupport {

	/**
	 * 
	 * @param paramDto
	 * @return
	 * @throws Exception
	 */
	public List<HistoryDto.CargoDto> selectCargo(HistoryDto.CargoDto paramDto) throws Exception {
		BooleanBuilder where = new BooleanBuilder();
		if(!KainosStringUtils.isEmpty(paramDto.getName()))
			where.and(mdmCargoHistory.name.contains(paramDto.getName()));
		
		return select(Projections.bean(HistoryDto.CargoDto.class,
				mdmCargoHistory.location,
				mdmCargoHistory.name,
				mdmCargoHistory.cargoDate,
				mdmCargoHistory.depot,
				mdmCargoHistory.cleaningCost,
				mdmCargoHistory.difficultLevel,
				mdmCargoHistory.remark1,
				mdmCargoHistory.remark2,
				mdmCargoHistory.createUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmCargoHistory.createDate, "YYYY-MM-DD").as("createDate"),
				mdmCargoHistory.updateUserId,
				Expressions.stringTemplate("to_char({0}, {1})", mdmCargoHistory.updateDate, "YYYY-MM-DD").as("updateDate")
				))
				.from(mdmCargoHistory)
				.where(where)
				.orderBy(mdmCargoHistory.updateDate.desc())
				.fetch();
	}
	
}
