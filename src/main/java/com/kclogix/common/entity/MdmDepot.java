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
public class MdmDepot {

	@Id
	private String depotCode;
	private String location;
	private String depotNameEn;
	private String depotNameKr;
	private String address;
	private String picName;
	private String picTel;
	private String picEmail;
	private String operationRemark;
	private String createUserId;
	private Date createDate;
	private String updateUserId;
	private Date updateDate;
}
