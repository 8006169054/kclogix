package com.kclogix.common.backup;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kclogix.common.backup.service.BackUpService;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class BackUpSchedule {

	private final BackUpService service;
	
	@Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
	public void dbBackup() throws Exception {
		service.dbBackup();
	}
}
