package com.kclogix.common.util.excel;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@SuppressWarnings("resource")
public class KainosExcelReadHandler {

	@Builder.Default
	private List<Map<String, String>> rows = new ArrayList<>();
	@Builder.Default
    private int startRowNum = 0;   
    private InputStream excel;
    @Builder.Default
    private boolean rowSpan = false;
    @Builder.Default
    private List<ExcelReadRowSpan> rowSpanList = new ArrayList<>();
    @Builder.Default
	public Map<Integer, String> indexCell = new HashMap<>();
    
    /**
     * 
     * @param is
     * @param startRow
     * @return
     * @throws Exception
     */
	public KainosExcelReadHandler readExcel() throws Exception {
		IOUtils.setByteArrayMaxOverride(1000000000);
		indexCellSetting();
        try {
        	OPCPackage opc = OPCPackage.open(excel);
            XSSFWorkbook wb = new XSSFWorkbook(opc);
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = startRowNum; i <= sheet.getLastRowNum(); i++) {
            	Row row = sheet.getRow(i);
            	Map<String, String> rowMap = new HashMap<>();
            	if(row != null) {
                   	Iterator<Cell> cellIterator = row.cellIterator();
                	while (cellIterator.hasNext()) {
                		Cell cell = cellIterator.next();
                		String value = null;
    					value = getExcelCellValue(wb, cell, value);
    					rowMap.put(indexCell.get(cell.getColumnIndex()), value);
                	}
                	rows.add(rowMap);
            	}
            }
                        
            if(rowSpan) {
            	for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
    				CellRangeAddress region = sheet.getMergedRegion(i);
    				int colIndex = region.getFirstColumn();
 	                int rowNum = region.getFirstRow(); 
 	                Row row = sheet.getRow(rowNum);
 	                Cell cell = row.getCell(colIndex);
 	                if (rowNum == cell.getRowIndex() && colIndex == cell.getColumnIndex()) {
	                	String value = null;
	                	switch (cell.getCellType()) {
							case NUMERIC: // 숫자
							value = numericAndDateValue(cell);
								break;
							default:
								value = cell.toString();
								break;
	                	}
	                	rowSpanList.add(ExcelReadRowSpan.builder()
	                			.ColumnIndex(colIndex)
	                			.rowIndex(rowNum)
	                			.ColumnValue(value)
	                			.startRowNum(region.getFirstRow() - startRowNum)
	                			.rowspanCnt(region.getLastRow() - region.getFirstRow())
	                			.build());
	                }
    			}
            }
            opc.close();
        } catch (Exception e) {
            throw e;
        } finally {
        	excel.close();
		}
        return KainosExcelReadHandler.this;
    }

	/**
	 * 
	 * @param wb
	 * @param cell
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	private String getExcelCellValue(XSSFWorkbook wb, Cell cell, String value) throws ParseException {
		switch (cell.getCellType()) {
			case STRING: // 텍스트
				value = cell.getStringCellValue();
				break;
			case NUMERIC: // 숫자
			value = numericAndDateValue(cell);
				break;
			case FORMULA: // = 붙은 계산식 처리
				FormulaEvaluator formulaEval = wb.getCreationHelper().createFormulaEvaluator();
				CellValue cellValue =  formulaEval.evaluate(cell);
				switch (cell.getCachedFormulaResultType()) {
					case NUMERIC:
					value = numericAndDateValue(cell);
				 		break;
				 	default:
						value = cellValue.getStringValue();
					break;
				 }
				break;
			case BLANK: // 빈칸
				value = "" ;
				break;
			case ERROR: // 에러난 처리 
				value = cell.getErrorCellValue() +"";
				break;
			default:
				break;
		}
		return value;
	}

	/**
	 * 날짜 데이터인지 numeric 인지 체크해서 데이터 리턴
	 * @param cell
	 * @return
	 * @throws ParseException
	 */
	private String numericAndDateValue(Cell cell) throws ParseException {
		String value;
		if(DateUtil.isCellDateFormatted(cell)) {
			cell.getCellStyle().setDataFormat((short)14);
			value = cell.getDateCellValue().toString();
			SimpleDateFormat recvSimpleFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
			SimpleDateFormat tranSimpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
			Date data = recvSimpleFormat.parse(value);
			value = tranSimpleFormat.format(data);
		}else {
			value = cell.getNumericCellValue() + "";
		}
		return value;
	}
	
	public List<Map<String, String>> getRows() {
		return rows;
	}
	
	/**
	 * 데이터 바인딩
	 * @param <T>
	 * @param dateRow
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T objectCoyp(Map<String, String> dateRow, Class<?> clazz) throws Exception {
		Object t = createResultInstance(clazz);
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for (java.lang.reflect.Field field : fields) {
			field.setAccessible(true);
			Field anno = field.getAnnotation(Field.class);
			if ( anno == null ) continue;
			String index = anno.value();
			if(dateRow != null) {
				String value = dateRow.get(index.toUpperCase());
				field.set(t, value);
			}
		}
		return (T) t;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void objectCoypClose() throws Exception {
		rows.clear();
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private Object createResultInstance(Class<?> clazz) throws InvocationTargetException, NoSuchMethodException {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param excelData
	 * @throws Exception
	 */
	public <T> void rowSapnCoyp(List<T> excelData) throws Exception {
		try {
			for (Iterator<ExcelReadRowSpan> iterator = rowSpanList.iterator(); iterator.hasNext();) {
				ExcelReadRowSpan excelReadRowSpan = (ExcelReadRowSpan) iterator.next();
				int roopCnt = excelReadRowSpan.getStartRowNum() + excelReadRowSpan.getRowspanCnt();
				for (int i = excelReadRowSpan.getStartRowNum(); i <= roopCnt; i++) {
					Object excel = excelData.get(i);
					java.lang.reflect.Field[] fields = excel.getClass().getDeclaredFields();
					for (java.lang.reflect.Field field : fields) {
						if(field.isAnnotationPresent(com.kclogix.common.util.excel.Field.class)) {
							com.kclogix.common.util.excel.Field anno = field.getAnnotation(com.kclogix.common.util.excel.Field.class);
							String cell = indexCell.get(excelReadRowSpan.getColumnIndex());
							if(cell.equalsIgnoreCase(anno.value())) {
								field.setAccessible(true);
								String value = excelReadRowSpan.getColumnValue();
								field.set(excel, value);
								break;
							}
						}
					}
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			rowSpanList.clear();
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param excelData
	 * @throws Exception
	 */
	public <T> void customFunctionCall(List<T> excelData) throws Exception {
		for (int i = 0; i < excelData.size(); i++) {
			Object excel = excelData.get(i);
			java.lang.reflect.Field[] fields = excel.getClass().getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				if(field.isAnnotationPresent(com.kclogix.common.util.excel.Field.class)) {
					com.kclogix.common.util.excel.Field anno = field.getAnnotation(com.kclogix.common.util.excel.Field.class);
					String value = null;
					if(!anno.stringFormat().equals("")) {
						value = stringFormat(anno, value);
					}
					
					if(!anno.function().equals("")) {
						Method method = excel.getClass().getDeclaredMethod(anno.function(), excel.getClass());
						value = (String) method.invoke(excel, excel);
					}
					
					if(value != null) {
						field.setAccessible(true);
						field.set(excel, value);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param anno
	 * @param value
	 * @return
	 */
	private String stringFormat(com.kclogix.common.util.excel.Field anno, String value) {
		try {
			value = String.format(anno.stringFormat(), Double.valueOf(value));
			if(!anno.prefix().equals("")) value = anno.prefix() + value;
		}catch (Exception e) {
//			log.error("stringFormat Error : " + anno.stringFormat() + " : " +  value);
		}
		return value;
	}
	
	public void rowSpan(boolean rowSpan) {
		this.rowSpan = rowSpan;
	}
	
	public void indexCellSetting(){
    	indexCell.put(0,"A"); 
		indexCell.put(1,"B"); 
		indexCell.put(2,"C"); 
		indexCell.put(3,"D"); 
		indexCell.put(4,"E"); 
		indexCell.put(5,"F"); 
		indexCell.put(6,"G"); 
		indexCell.put(7,"H"); 
		indexCell.put(8,"I"); 
		indexCell.put(9,"J"); 
		indexCell.put(10,"K"); 
		indexCell.put(11,"L"); 
		indexCell.put(12,"M"); 
		indexCell.put(13,"N"); 
		indexCell.put(14,"O"); 
		indexCell.put(15,"P"); 
		indexCell.put(16,"Q"); 
		indexCell.put(17,"R"); 
		indexCell.put(18,"S"); 
		indexCell.put(19,"T"); 
		indexCell.put(20,"U"); 
		indexCell.put(21,"V"); 
		indexCell.put(22,"W"); 
		indexCell.put(23,"X"); 
		indexCell.put(24,"Y"); 
		indexCell.put(25,"Z"); 
		indexCell.put(26,"AA"); 
		indexCell.put(27,"AB"); 
		indexCell.put(28,"AC"); 
		indexCell.put(29,"AD"); 
		indexCell.put(30,"AE"); 
		indexCell.put(31,"AF"); 
		indexCell.put(32,"AG"); 
		indexCell.put(33,"AH"); 
		indexCell.put(34,"AI"); 
		indexCell.put(35,"AJ"); 
		indexCell.put(36,"AK"); 
		indexCell.put(37,"AL"); 
		indexCell.put(38,"AM"); 
		indexCell.put(39,"AN"); 
		indexCell.put(40,"AO"); 
		indexCell.put(41,"AP"); 
		indexCell.put(42,"AQ"); 
		indexCell.put(43,"AR"); 
		indexCell.put(44,"AS"); 
		indexCell.put(45,"AT"); 
		indexCell.put(46,"AU"); 
		indexCell.put(47,"AV"); 
		indexCell.put(48,"AW"); 
		indexCell.put(49,"AX"); 
		indexCell.put(50,"AY"); 
		indexCell.put(51,"AZ"); 
		indexCell.put(52,"BA"); 
		indexCell.put(53,"BB"); 
		indexCell.put(54,"BC"); 
		indexCell.put(55,"BD"); 
		indexCell.put(56,"BE"); 
		indexCell.put(57,"BF"); 
		indexCell.put(58,"BG"); 
		indexCell.put(59,"BH"); 
		indexCell.put(60,"BI"); 
		indexCell.put(61,"BJ"); 
		indexCell.put(62,"BK"); 
		indexCell.put(63,"BL"); 
		indexCell.put(64,"BM"); 
		indexCell.put(65,"BN"); 
		indexCell.put(66,"BO"); 
		indexCell.put(67,"BP"); 
		indexCell.put(68,"BQ"); 
		indexCell.put(69,"BR"); 
		indexCell.put(70,"BS"); 
		indexCell.put(71,"BT"); 
		indexCell.put(72,"BU"); 
		indexCell.put(73,"BV"); 
		indexCell.put(74,"BW"); 
		indexCell.put(75,"BX"); 
		indexCell.put(76,"BY"); 
		indexCell.put(77,"BZ"); 
    }
	
}
