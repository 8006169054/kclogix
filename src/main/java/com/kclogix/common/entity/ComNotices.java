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
public class ComNotices {

	@Id
	private String id;
	private String title;
	private Date viewStartDate;
	private Date viewEndDate;
	private String use;
	private String contentBody;
	private String createUserId;
	private Date createDate;
	private String updateUserId;
	private Date updateDate;
}
