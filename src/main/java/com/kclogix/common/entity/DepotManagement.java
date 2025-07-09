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
public class DepotManagement {
	
	@Id
	private String uuid;
	private int seq;
	private String hblNo;
	private String tankNo;
	private String partner;
	private String item;
	private String returnDate;
	private String cleanedDate;
	private String outDate;
	private String allocation;
	private String remark;
	private String createUserId;
	private Date createDate;
	private String updateUserId;
	private Date updateDate;
}
