package com.kclogix.common.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorLog {

	@Id
	private Integer id;
	private String url;
	private String log;
	private String createUserId;
	private Date createDate;

}
