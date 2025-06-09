package com.kclogix.apps.common.error.repository;

import static com.kclogix.common.entity.QErrorLog.errorLog;
import java.util.Date;
import org.springframework.stereotype.Repository;
import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import com.kclogix.common.entity.ErrorLog;

@Repository
public class ErrorRepository extends KainosRepositorySupport {

	/**
	 * 에러로그 등록
	 * @param paramData
	 * @throws Exception
	 */
	public void insertErrorLog(ErrorLog paramData) throws Exception {
		int newId = select(errorLog.id.max().coalesce(0)).from(errorLog).fetchFirst();
		paramData.setId(newId+1);
		paramData.setCreateDate(new Date());
		insert(paramData);
	}
	
}
