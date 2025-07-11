package com.kclogix.common.backup.repository;

import static com.kclogix.common.entity.QComNotices.comNotices;
import static com.kclogix.common.entity.QComUser.comUser;
import static com.kclogix.common.entity.QDepotManagement.depotManagement;
import static com.kclogix.common.entity.QMdmCargo.mdmCargo;
import static com.kclogix.common.entity.QMdmCustomer.mdmCustomer;
import static com.kclogix.common.entity.QMdmDepot.mdmDepot;
import static com.kclogix.common.entity.QMdmPartner.mdmPartner;
import static com.kclogix.common.entity.QMdmTerm.mdmTerm;
import static com.kclogix.common.entity.QMdmTerminal.mdmTerminal;
import static com.kclogix.common.entity.QWebsiteTerminalCode.websiteTerminalCode;

import org.springframework.stereotype.Repository;

import kainos.framework.data.querydsl.support.repository.KainosRepositorySupport;
import kainos.framework.utils.KainosJsonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BackUpRepository extends KainosRepositorySupport {

	public void dbBackUp() throws Exception {
		
		/* MDM */
		log.error("=================== MDM CARGO ===================");
		selectFrom(mdmCargo).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		log.error("=================== MDM CUSTOMER ===================");
		selectFrom(mdmCustomer).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		log.error("=================== MDM TERMINAL ===================");
		selectFrom(mdmTerminal).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		log.error("=================== MDM DEPOT ===================");
		selectFrom(mdmDepot).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		log.error("=================== MDM TERM ===================");
		selectFrom(mdmTerm).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		log.error("=================== MDM PARNER ===================");
		selectFrom(mdmPartner).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
		/* COM */
		log.error("=================== COM NOTICES ===================");
		selectFrom(comNotices).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		log.error("=================== COM USER ===================");
		selectFrom(comUser).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
		/* 그외 */
		log.error("=================== DEPOT MANAGEMENT ===================");
		selectFrom(depotManagement).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		log.error("=================== WEB SITE TERMINAL CODE ===================");
		selectFrom(websiteTerminalCode).stream().forEach( tableData -> {
			try {
				log.error(KainosJsonUtil.ToString(tableData));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
	}
}
