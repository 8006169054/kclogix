package com.kclogix.common.view;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PagePaths {

	public static Map<String, PageInfo> links = new HashMap<>();
	static {
//		PagePaths.links.put("/HOME", PageInfo.builder().linkPath("/home").auth(true).build());
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PageInfo {
		private String linkPath;
		private boolean auth;
	}
}
