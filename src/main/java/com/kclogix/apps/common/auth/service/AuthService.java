package com.kclogix.apps.common.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kainos.framework.core.lang.KainosBusinessException;
import kainos.framework.core.session.KainosSessionContext;
import com.kclogix.apps.common.auth.repository.AuthRepository;
import com.kclogix.common.dto.SessionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthRepository repository;
	private final KainosSessionContext kainosSession;
	
	/**
	 * 
	 * @param id
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public SessionDto dbLogin(String id, String password) throws Exception {
		SessionDto session;
		try {
			session = repository.dbLogin(id, password);
			if (session != null) {
				if(session.getActivation().equalsIgnoreCase("N")) {
					throw new KainosBusinessException("common.login.activation");
				}
				kainosSession.openSession(session);
			} else
				throw new KainosBusinessException("common.login.fail");
		} catch (KainosBusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("{}", e);
			throw new KainosBusinessException("common.system.error");
		}
		return session;
	}
	
	public void logout() throws Exception {
		try {
			kainosSession.closeSession();
		} catch (Exception e) {
			log.error("{}", e);
			throw new KainosBusinessException("common.system.error");
		}
	}
	
	
}
