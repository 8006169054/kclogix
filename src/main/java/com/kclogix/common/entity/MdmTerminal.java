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
public class MdmTerminal {

	@Id
	private String code;
	private String name;
	private String region;
	private String type;
	private String parkingLotCode;
	private String homepage;
	private String createUserId;
	private Date createDate;
	private String updateUserId;
	private Date updateDate;
	
}
