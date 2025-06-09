package com.kclogix.common.util;

import java.util.Date;

public class CodeGenerationUtil {

	/**
	 * 
	 * @param prefix
	 * @return
	 */
	public static String createCode(String prefix) {
		return prefix + new Date().getTime();
	}
}
