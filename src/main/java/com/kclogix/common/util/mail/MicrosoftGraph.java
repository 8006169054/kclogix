package com.kclogix.common.util.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.mail.internet.InternetAddress;
import kainos.framework.core.model.KainosMailDto;
import kainos.framework.core.support.teams.dto.TeamsResponseDto;
import kainos.framework.core.util.KainosBeanUtils;
import kainos.framework.core.web.client.KainosRestTemplate;

@Configuration
public class MicrosoftGraph {

	@Value("${kainos.microsoft.graph.url}")
	private String url;
	@Value("${kainos.microsoft.graph.id}")
	private String clientId;
	@Value("${kainos.microsoft.graph.username}")
	private String username;
	@Value("${kainos.microsoft.graph.password}")
	private String password;
	@Value("${kainos.microsoft.graph.secret}")
	private String clientSecret;
	@Value("${kainos.microsoft.graph.tenant}")
	private String tenant;
	
	/**
	 * 
	 * @param tramsConfig
	 */
	private void getToken() {
		KainosRestTemplate rest = KainosBeanUtils.getBean(KainosRestTemplate.class);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add(GraphInfo.Key.clientId, clientId);
		parameters.add(GraphInfo.Key.scope, GraphInfo.Value.scope);
		parameters.add(GraphInfo.Key.grantType, GraphInfo.Value.grantType);
		parameters.add(GraphInfo.Key.username, username);
		parameters.add(GraphInfo.Key.password, password);
		parameters.add(GraphInfo.Key.clientSecret, clientSecret);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
		TeamsResponseDto.Token token = rest.exchange(GraphInfo.Url.token.replace(":tenant", tenant), HttpMethod.POST, request, TeamsResponseDto.Token.class).getBody();
		GraphInfo.Token.value = token.getAccessToken();
		GraphInfo.Token.type = token.getTokenType();
	}

	/**
	 * 
	 * @param mailDto
	 * @throws Exception
	 */
	public void sendMail(KainosMailDto mailDto) throws Exception{
		try {
			if(GraphInfo.Token.value == null) getToken();
			send(mailDto);
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode().value() == HttpStatus.SC_UNAUTHORIZED) {
				getToken();
				sendMail(mailDto);
			}
			else {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * @param mailDto
	 */
	private void send(KainosMailDto mailDto) {
		KainosRestTemplate rest = KainosBeanUtils.getBean(KainosRestTemplate.class);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(GraphInfo.Token.value);
		List<GraphRequestDto.Email.Recipient> toRecipients = new ArrayList<>();
		for (int i = 0; i < mailDto.getTo().size(); i++) {
			InternetAddress addr = mailDto.getTo().get(i);
			toRecipients.add(GraphRequestDto.Email.Recipient.builder().emailAddress(GraphRequestDto.Email.EmailAddress.builder().address(addr.getAddress()).name(addr.getPersonal()).build()).build());
		}
		GraphRequestDto.Email.Recipient ccRecipients = GraphRequestDto.Email.Recipient.builder().emailAddress(GraphRequestDto.Email.EmailAddress.builder().address("kcl@kclogix.com").build()).build();
		
		GraphRequestDto.Email.Message message = GraphRequestDto.Email.Message.builder()
				.subject(mailDto.getSubject())
				.body(GraphRequestDto.Email.Body.builder().contentType("HTML").content(mailDto.getMailbody()).build())
				.toRecipients(toRecipients)
				.ccRecipients(Arrays.asList(ccRecipients))
				.build();
		
		GraphRequestDto.Email email = GraphRequestDto.Email.builder().message(message).saveToSentItems(false).build();
		HttpEntity<GraphRequestDto.Email> request = new HttpEntity<>(email, headers);
		rest.exchange(url, HttpMethod.POST, request, String.class).getBody();
	}
	
}
