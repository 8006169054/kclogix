package com.kclogix.common.util.mail;

import java.util.Date;

public class GraphInfo {

	public static class Token {
		public static String value = null;
		public static String type = null;
		public static Date expiresIn = null;
	}
	
	public static class Key {
		public static String clientId = "client_id";
		public static String scope = "scope";
		public static String grantType = "grant_type";
		public static String username = "username";
		public static String password = "password";
		public static String clientSecret = "client_secret";
	}

	public static class Value {
		public static String scope = "https://graph.microsoft.com/.default";
		public static String grantType = "password";
	}
	
	public static class Url {
		public static String token = "https://login.microsoftonline.com/:tenant/oauth2/v2.0/token";
	}
	
}
