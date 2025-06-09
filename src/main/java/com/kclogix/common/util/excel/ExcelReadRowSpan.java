package com.kclogix.common.util.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelReadRowSpan {

	private int rowIndex;
	private int ColumnIndex;
	private String ColumnValue;
	private int rowspanCnt;
//	private int firstRow;
//	private int lastRow;
	private int startRowNum;
	
}
