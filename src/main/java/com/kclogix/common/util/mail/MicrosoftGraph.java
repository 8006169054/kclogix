package com.kclogix.common.util.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.models.Attachment;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.FileAttachment;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.graph.users.item.sendmail.SendMailPostRequestBody;

import jakarta.mail.internet.InternetAddress;
import kainos.framework.core.model.KainosMailDto;

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
//	private void getToken() {
//		KainosRestTemplate rest = KainosBeanUtils.getBean(KainosRestTemplate.class);
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//		parameters.add(GraphInfo.Key.clientId, clientId);
//		parameters.add(GraphInfo.Key.scope, GraphInfo.Value.scope);
//		parameters.add(GraphInfo.Key.grantType, GraphInfo.Value.grantType);
//		parameters.add(GraphInfo.Key.username, username);
//		parameters.add(GraphInfo.Key.password, password);
//		parameters.add(GraphInfo.Key.clientSecret, clientSecret);
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
//		TeamsResponseDto.Token token = rest.exchange(GraphInfo.Url.token.replace(":tenant", tenant), HttpMethod.POST, request, TeamsResponseDto.Token.class).getBody();
//		GraphInfo.Token.value = token.getAccessToken();
//		GraphInfo.Token.type = token.getTokenType();
//	}

//	/**
//	 * 
//	 * @param mailDto
//	 * @throws Exception
//	 */
//	public void sendMail(KainosMailDto mailDto) throws Exception{
//		try {
//			if(GraphInfo.Token.value == null) getToken();
//			send(mailDto);
//		} catch (HttpClientErrorException e) {
//			if(e.getStatusCode().value() == HttpStatus.SC_UNAUTHORIZED) {
//				getToken();
//				sendMail(mailDto);
//			}
//			else {
//				throw e;
//			}
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	/**
	 * 
	 * @param mailDto
	 * @throws Exception 
	 */
//	private void send(KainosMailDto mailDto) throws Exception {
//		KainosRestTemplate rest = KainosBeanUtils.getBean(KainosRestTemplate.class);
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.setBearerAuth(GraphInfo.Token.value);
//		List<GraphRequestDto.Email.Recipient> toRecipients = new ArrayList<>();
//		for (int i = 0; i < mailDto.getTo().size(); i++) {
//			InternetAddress addr = mailDto.getTo().get(i);
//			toRecipients.add(GraphRequestDto.Email.Recipient.builder().emailAddress(GraphRequestDto.Email.EmailAddress.builder().address(addr.getAddress()).name(addr.getPersonal()).build()).build());
//		}
//		GraphRequestDto.Email.Recipient ccRecipients = GraphRequestDto.Email.Recipient.builder().emailAddress(GraphRequestDto.Email.EmailAddress.builder().address("jis400@daum.net").build()).build();
//		
//		List<GraphRequestDto.Email.FileAttachment> attachments = new ArrayList<>();
//		for (int i = 0; i < mailDto.getAttachment().size(); i++) {
//			KainosMailDto.FileAttachment attachment = mailDto.getAttachment().get(i);
//			byte[] fileBytes =((MultipartFile)attachment.getFile()).getBytes();
//			GraphRequestDto.Email.FileAttachment geAttachment = GraphRequestDto.Email.FileAttachment.builder().name(attachment.getFileName()).contentBytes(Base64.getEncoder().encodeToString(fileBytes)).build();
//			attachments.add(geAttachment);
//		}
//		
//		GraphRequestDto.Email.Message message = GraphRequestDto.Email.Message.builder()
//				.subject(mailDto.getSubject())
//				.body(GraphRequestDto.Email.Body.builder().contentType("HTML").content("AAAAAA").build())
//				.toRecipients(toRecipients)
//				.ccRecipients(Arrays.asList(ccRecipients))
//				.attachments(attachments)
//				.build();
//		
//		GraphRequestDto.Email email = GraphRequestDto.Email.builder().message(message).saveToSentItems(false).build();
//		System.out.println(KainosJsonUtil.ToString(email));
//		HttpEntity<GraphRequestDto.Email> request = new HttpEntity<>(email, headers);
//		rest.exchange(url, HttpMethod.POST, request, String.class).getBody();
//	}
	
	/**
	 * 
	 * @param mailDto
	 * @throws Exception
	 */
	public void sendMail(KainosMailDto mailDto) throws Exception{
		ClientSecretCredential credential = new ClientSecretCredentialBuilder()
			    .clientId(clientId)
			    .clientSecret(clientSecret)
			    .tenantId(tenant)
			    .build();
		
		GraphServiceClient graphClient = new GraphServiceClient(credential);
		
		Message message = new Message();
		message.setSubject(mailDto.getSubject());
		ItemBody body = new ItemBody();
		body.setContentType(BodyType.Html);
		body.setContent(mailDto.getMailbody());
		message.setBody(body);
		
		/* To */
		List<Recipient> toRecipients = new ArrayList<>();
		for (int i = 0; i < mailDto.getTo().size(); i++) {
			InternetAddress addr = mailDto.getTo().get(i);
			Recipient recipient = new Recipient();
			EmailAddress emailAddress = new EmailAddress();
			emailAddress.setAddress(addr.getAddress());
			recipient.setEmailAddress(emailAddress);;
			toRecipients.add(recipient);
		}
		message.setToRecipients(toRecipients);
		
		/* CC */
		Recipient ccRecipient = new Recipient();
		EmailAddress ccAddress = new EmailAddress();
		ccAddress.setAddress("kcl@kclogix.com");;
		ccRecipient.setEmailAddress(ccAddress);
		message.setCcRecipients(Arrays.asList(ccRecipient));
		
		/* 파일 첨부 */
		List<Attachment> attachments = new ArrayList<>();
		for (int i = 0; i < mailDto.getAttachment().size(); i++) {
			KainosMailDto.FileAttachment attachment = mailDto.getAttachment().get(i);
			FileAttachment file = new FileAttachment();
			file.setName(attachment.getFileName());;
			file.setContentType(((MultipartFile)attachment.getFile()).getContentType());;
			file.setContentBytes(((MultipartFile)attachment.getFile()).getBytes());
			file.setOdataType("#microsoft.graph.fileAttachment");
			attachments.add(file);
		}
		
		if(attachments.size() > 0) {
			message.setAttachments(attachments);
		}
		
		// 요청 바디 구성
		SendMailPostRequestBody requestBody = new SendMailPostRequestBody();
		requestBody.setMessage(message);
		requestBody.setSaveToSentItems(false);
		
		graphClient.users().byUserId(username).sendMail().post(requestBody);
	}
	
}
