package com.kclogix.common.util.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import kainos.framework.core.support.excel.write.MakeCell;
import kainos.framework.core.support.jqgrid.dto.RowSpan;

public class KainosExcelWriteHandler {

	private int startRowNum = 0;
	private Workbook workbook;
	private Sheet sheet;
	private int addIndex = 0;
	private List<Object> rowspanList;
	
	public KainosExcelWriteHandler(String templateFile, int startRowNum) {
		this.startRowNum = startRowNum;
		this.workbook = getWorkbook(templateFile);
		this.sheet = this.workbook.getSheetAt(0);
		this.rowspanList = new ArrayList<>();
	}

	/**
	 * 
	 * @param dataRows
	 * @return
	 * @throws IOException
	 */
	public KainosExcelWriteHandler writeALL(List<?> dataRows) throws Exception {
		if (dataRows != null && !dataRows.isEmpty()) {
			for (int i = 0; i < dataRows.size(); i++) {
				makeContents(dataRows.get(i), createRow(startRowNum + i));
			}
		}
		return KainosExcelWriteHandler.this;
	}

	public void writeADD(Object dataRow) {
		makeContents(dataRow, createRow(startRowNum + addIndex++));
	}
	
	/**
	 * 
	 * @param obj
	 * @param row
	 */
	private void makeContents(Object obj, Row row) {
		makeCellAndFillValue(obj, null, row);
		flush();
	}
	
	public byte[] writeFlush() throws Exception {
		/* 머지 */
		addMergedRegion();
		
		ByteArrayOutputStream outputByte = new ByteArrayOutputStream();
		this.workbook.write(outputByte);
		return outputByte.toByteArray();
	}
	
	private void addMergedRegion() {
		for (Iterator<Object> iterator = rowspanList.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			java.lang.reflect.Field[] fields = object.getClass().getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				try {
					field.setAccessible(true);
					RowSpan span = (RowSpan)field.get(object);
					if(span.getRowspan() > 0) {
//						System.out.println(span.getStartrow()+startRowNum + " , " + span.getEndrow()+startRowNum + " : " + span.getStartcol() + " , " + span.getEndcol());
						sheet.addMergedRegion(new CellRangeAddress(span.getStartrow()+startRowNum,span.getEndrow()+startRowNum-1,span.getStartcol(),span.getEndcol()));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private void flush() {
		if ( this.workbook instanceof SXSSFWorkbook ) {
			try {
				((SXSSFSheet)this.sheet).flushRows(10000);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}
	private void makeCellAndFillValue(Object obj, MakeCell makeCell, Row row) {
		java.lang.reflect.Field[] fields = obj.getClass().getDeclaredFields();
		for (java.lang.reflect.Field field : fields) {
			int cellIndex = -1;
			field.setAccessible(true);
			
			if(field.getName().equalsIgnoreCase("rowspan")) {
				try {
					rowspanList.add(field.get(obj));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			if(field.isAnnotationPresent(Field.class)) {
				Field anno = field.getAnnotation(Field.class);
				cellIndex = CellReference.convertColStringToIndex(anno.value());
			}
			
			if(cellIndex >= 0) {
				if ( makeCell == null ) {
					makeCell = new MakeCell(obj, this.sheet, row, cellIndex, startRowNum);
				}
				else {
					makeCell.changeCell(obj, this.sheet, row, cellIndex, startRowNum);
				}
				makeCell.fillValue(field, row);
			}
		}
	}
	
	/**
	 * 해더 정보 설정
	 */
//	private void headerInit() {
//		headerRow = this.sheet.getRow(0);
//		if (headerRow.getRowStyle() != null)
//			headerCellStyle = headerRow.getRowStyle();
//	}
	
	/**
	 * 
	 * @param rowNum
	 * @return
	 */
	private Row createRow(int rowNum) {
		Row tempRow = this.sheet.getRow(rowNum);
		if(tempRow == null) {
			tempRow = sheet.createRow(rowNum);
		}
		return tempRow;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private Workbook getWorkbook(String fileName) {
		try {
			InputStream inputStream = new ClassPathResource(fileName).getInputStream();
			if (isXls(fileName))
				return new HSSFWorkbook(inputStream);
			if (isXlsx(fileName))
				return new XSSFWorkbook(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not find Excel file");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not find Excel file");
		}
		return null;
	}

	private boolean isXls(String fileName) {
		return fileName.toUpperCase().endsWith(".XLS");
	}

	private boolean isXlsx(String fileName) {
		return fileName.toUpperCase().endsWith(".XLSX");
	}

	public static Builder builder() {
		return new KainosExcelWriteHandler.Builder();
	}

	public static class Builder {
		private int startRowNum = 0;
		private String templateFile;

		public int getStartRowNum() {
			return startRowNum;
		}

		public Builder startRowNum(int startRowNum) {
			this.startRowNum = startRowNum;
			return Builder.this;
		}

		public String getTemplateFile() {
			return templateFile;
		}

		public Builder templateFile(String templateFile) {
			this.templateFile = templateFile;
			return Builder.this;
		}

		public KainosExcelWriteHandler build() {
			return new KainosExcelWriteHandler(this.templateFile, this.startRowNum);
		}
	}

}
