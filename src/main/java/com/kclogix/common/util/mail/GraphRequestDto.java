package com.kclogix.common.util.mail;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class GraphRequestDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class Email {
		
		private Message message;
		@Builder.Default
		private boolean saveToSentItems = false;
		
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		@ToString
		public static class Message {
			private String subject;
			private Body body;
			private List<Recipient> toRecipients;
			@JsonIgnoreProperties(ignoreUnknown = true)
			private List<Recipient> ccRecipients;
		}
		
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		@ToString
		public static class Body {
			
			private String contentType;
			private String content;
			
		}
		
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		@ToString
		public static class Recipient {
			private EmailAddress emailAddress;
		}
		
		@Data
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		@ToString
		public static class EmailAddress {
			
			private String address;
			@JsonIgnoreProperties(ignoreUnknown = true)
			private String name;
			
		}
		
	}
	
}
