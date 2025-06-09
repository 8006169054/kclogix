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
public class MdmCargo {

	@Id
	private String code;
	private String name;
	private String location;
	private String cargoDate;
	private String depot;
	private String cleaningCost;
	private String difficultLevel;
	private String remark1;
	private String remark2;
	private String createUserId;
	private Date createDate;
	private String updateUserId;
	private Date updateDate;
	
}
