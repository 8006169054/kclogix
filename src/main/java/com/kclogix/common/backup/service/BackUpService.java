package com.kclogix.common.backup.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kclogix.common.backup.repository.BackUpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BackUpService {

	private final BackUpRepository repository;
	
	@Transactional(readOnly = true)
	@Async
	public void dbBackup() throws Exception {
		repository.dbBackUp();
	}
}
