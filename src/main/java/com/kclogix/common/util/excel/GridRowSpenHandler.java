package com.kclogix.common.util.excel;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import kainos.framework.core.support.jqgrid.dto.GridRowSpanInfo;
import kainos.framework.core.support.jqgrid.dto.RowSpan;

@Component
public class GridRowSpenHandler {

	public GridRowSpenHandler() {}
	
	/**
	 * 
	 * @param datas
	 * @param clss
	 * @return
	 * @throws Exception
	 */
	public List<?> GenerationRowSpen(List<?> datas, Class<?> clss) throws Exception{
		GridRowSpanInfo rowSpaninfo = gridRowSpanInit(clss);
		for (int i = 0; i < datas.size();) {
			int rowSpanIndex = getRowSpanIndex(i, datas.size(), rowSpaninfo, datas);
			setRowSpan(i, (i+rowSpanIndex), rowSpanIndex, rowSpaninfo, datas);
			i += rowSpanIndex;
		}
		return datas;
		
	}
	
	/**
	 * 
	 * @param startRow
	 * @param endRow
	 * @param rowSpan
	 * @param rowSpaninfo
	 * @param datas
	 * @throws Exception
	 */
	public void setRowSpan(int startRow, int endRow, int rowSpan, GridRowSpanInfo rowSpaninfo, List<?> datas) throws Exception {
		List<Field> rowSpanFieldList = rowSpaninfo.getRowSpanFieldList();
		for (int i = startRow; endRow > i; i++) {
			Field attrSubField = datas.get(i).getClass().getDeclaredField("rowspan");
			attrSubField.setAccessible(true);
			Object attrObj = attrSubField.get(datas.get(i));
			
			for (int j = 0; j < rowSpanFieldList.size(); j++) {
				Field rowSpanField = rowSpanFieldList.get(j);
				com.kclogix.common.util.excel.Field fA = rowSpanField.getAnnotation(com.kclogix.common.util.excel.Field.class);
				Field field = attrObj.getClass().getDeclaredField(rowSpanField.getName());
				field.setAccessible(true);
				if(i == startRow) {
					int mci = index.get(fA.value());
					field.set(attrObj, RowSpan.builder().rowspan(rowSpan).startrow(startRow).endrow(endRow).startcol(mci).endcol(mci).build());
				}
				else {
					field.set(attrObj, RowSpan.builder().display("none").build());
				}
			}
			
		}
	}
	
	/**
	 * 
	 * @param startRow
	 * @param endRow
	 * @param rowSpaninfo
	 * @param datas
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private int getRowSpanIndex(int startRow, int endRow, GridRowSpanInfo rowSpaninfo, List<?> datas) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		int rowSpanIndex = 1;
		List<Field> fields = rowSpaninfo.getOrderFieldList();
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			int loopIndex = 1;
			/** 데이터 lopp */
			Field mainDataField = datas.get(startRow).getClass().getDeclaredField(field.getName());
			mainDataField.setAccessible(true);
			Object mainValue = mainDataField.get(datas.get(startRow));

			/** 하위 데이터 */
			for (int j = (startRow+1); j < datas.size(); j++) {
				Field subDataField = datas.get(j).getClass().getDeclaredField(field.getName());
				subDataField.setAccessible(true);
				Object subValue = mainDataField.get(datas.get(j));
				
				if(mainValue != null && !mainValue.equals(subValue)) {
					break;
				}
				loopIndex++;
			}
			
			if(rowSpanIndex == 1 || rowSpanIndex > loopIndex) rowSpanIndex = loopIndex;
			
		}
		return rowSpanIndex;
	}
	
	/**
	 * 
	 * @param clss
	 * @return
	 */
	private GridRowSpanInfo gridRowSpanInit(Class<?> clss) {
		GridRowSpanInfo rowSpaninfo = GridRowSpanInfo.builder().build();
		Field[] fields = clss.getDeclaredFields();
		for (java.lang.reflect.Field field : fields) {
			if(field.isAnnotationPresent(com.kclogix.common.util.excel.Field.class)) {
				com.kclogix.common.util.excel.Field anno = field.getAnnotation(com.kclogix.common.util.excel.Field.class);
				if(anno.mergeOrder() > -1)
					rowSpaninfo.getOrderFieldList().add(anno.mergeOrder(), field);
				
				if(anno.merge())
					rowSpaninfo.getRowSpanFieldList().add(field);
			}
		}
		return rowSpaninfo;
	}
	
	public static Map<String, Integer> index = new HashMap<>();
	{
		index.put("A",0); 
		index.put("B",1); 
		index.put("C",2); 
		index.put("D",3); 
		index.put("E",4); 
		index.put("F",5); 
		index.put("G",6); 
		index.put("H",7); 
		index.put("I",8); 
		index.put("J",9); 
		index.put("K",10); 
		index.put("L",11); 
		index.put("M",12); 
		index.put("N",13); 
		index.put("O",14); 
		index.put("P",15); 
		index.put("Q",16); 
		index.put("R",17); 
		index.put("S",18); 
		index.put("T",19); 
		index.put("U",20); 
		index.put("V",21); 
		index.put("W",22); 
		index.put("X",23); 
		index.put("Y",24); 
		index.put("Z",25); 
		index.put("AA",26); 
		index.put("AB",27); 
		index.put("AC",28); 
		index.put("AD",29); 
		index.put("AE",30); 
		index.put("AF",31); 
		index.put("AG",32); 
		index.put("AH",33); 
		index.put("AI",34); 
		index.put("AJ",35); 
		index.put("AK",36); 
		index.put("AL",37); 
		index.put("AM",38); 
		index.put("AN",39); 
		index.put("AO",40); 
		index.put("AP",41); 
		index.put("AQ",42); 
		index.put("AR",43); 
		index.put("AS",44); 
		index.put("AT",45); 
		index.put("AU",46); 
		index.put("AV",47); 
		index.put("AW",48); 
		index.put("AX",49); 
		index.put("AY",50); 
		index.put("AZ",51); 
		index.put("BA",52); 
		index.put("BB",53); 
		index.put("BC",54); 
		index.put("BD",55); 
		index.put("BE",56); 
		index.put("BF",57); 
		index.put("BG",58); 
		index.put("BH",59); 
		index.put("BI",60); 
		index.put("BJ",61); 
		index.put("BK",62); 
		index.put("BL",63); 
		index.put("BM",64); 
		index.put("BN",65); 
		index.put("BO",66); 
		index.put("BP",67); 
		index.put("BQ",68); 
		index.put("BR",69); 
		index.put("BS",70); 
		index.put("BT",71); 
		index.put("BU",72); 
		index.put("BV",73); 
		index.put("BW",74); 
		index.put("BX",75); 
		index.put("BY",76); 
		index.put("BZ",77); 
		index.put("KA",100);
		index.put("KB",101);
		index.put("KC",102);
		index.put("KD",103);
		index.put("KE",104);
		index.put("KF",105);
		index.put("KG",106);
		index.put("KH",107);
	}
}
