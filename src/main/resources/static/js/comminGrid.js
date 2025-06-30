var selectGridData = {	gridname : '',	rowid : '',	iCol: ''};

/**
 * 선택된 그리드 정보 jquery.jqGrid.js 호출 - editCell : function (iRow,iCol, ed, event, excel){
 */
function ComSelectGridData(gridname, rowid, iCol){
	selectGridData.gridname = gridname;
	selectGridData.rowid = rowid;
	selectGridData.iCol = iCol;
}

// ========================================= 기존 
var cellEditRow;

function jqFlagFn (cellvalue, options, rowObject ){
	if(cellvalue === C)
		return CIMG;
	else if(cellvalue === U)
		return UIMG;
	else if(cellvalue === D)
		return DIMG;
	else
		return '';
}

function ComSelectIndex(gridName){
	return $(gridName).getGridParam('selrow');
}

/**
 * 
 */
function ComSetCellData(gridName, rowId, col, value, rowspan){
	if(rowspan === undefined || rowspan === false)
		$(gridName).setCell(rowId, col, value);
	else if(rowspan === true){
		$(gridName).setCellRowSpan(rowId, col, value);
	}
}

function ComGetCellData(gridName, rowId, col){
	 $(gridName).getCell(rowId, col);
}

function ComSaveCell(gridName, rowId, col){
	$(gridName).jqGrid("saveCell", rowid, col); 
}

/**
 * 
 * @param gridName  
 * @param rowId
 */
function ComRowData(gridName, rowId){
	var rowData = $(gridName).getRowData(rowId);
	return rowData;
}

/**
 * 
 * @param gridName
 * @returns
 */
function ComGridLength(gridName){
	return $(gridName).getDataIDs().length;
}

/**
 * Grid DATA SetTing
 * @param gridName
 * @param jsonData
 */
function ComGridAction(gridName, jsonData, callBack){
	$(gridName).clearGridData();
	if(jsonData == null || jsonData.length == 0){
		$.jqGridLoadComplete($(gridName)[0]);
		return;
	}
	for ( var i = 0 ; i <= jsonData.length ; i++ ) {
		$(gridName).addRowData(i+1,jsonData[i]);
	}
	if ( callBack != undefined ) {
		callBack(gridName);
	}
}

/**
 * change GridData
 * @param gridName
 * @param prefix
 * @returns {Array}
 */
function ComGridData(gridName){

	var rows = $(gridName).jqGrid('getRowData');
	var gridData = new Array();

	for ( var i = 0 ; i < rows.length ; i++ ) {
		if(rows[i].jqFlag !== 'R'){
			gridData.push(rows[i]);
		} 
	}
	return gridData;
}
//function ComGridData(gridName, prefix){
//
//	if ( prefix == undefined ) {prefix = "";}
//
//	var rows = $(gridName).jqGrid('getRowData');
//	var gridData = "";
//	var paras = new Array();
//
//	for ( var i = 0 ; i < rows.length ; i++ ) {
//		paras.push($.param(rows[i]));
//	}
//
//	for ( var int = 0; int < paras.length; int++) {
//		gridData = gridData + paras[int] + "&";
//	}
//	
//	var arrParams = gridData.substring(0, gridData.length -1).split("&");
//	var result    = "";
//
//	for ( var i = 0 ; i < arrParams.length ; i++ ) {
//		var p = arrParams[i];
//		var arrP = p.split("=");
//
//		result = result + "&" + prefix + arrP[0] + "=" + arrP[1];
//	}
//
//	return result;
//}

function ComGridMultiSelectData(gridName, rows, prefix){
	if ( prefix == undefined ) {prefix = "";}
	var rowDate = $(gridName).jqGrid('getRowData');
	var gridData = "";
	var paras = new Array();

	for ( var i = 0 ; i < rows.length ; i++ ) {
			paras.push($.param(rowDate[i]));
	}

	for ( var int = 0; int < paras.length; int++) {
		gridData = gridData + paras[int] + "&";
	}
	
	var arrParams = gridData.substring(0, gridData.length -1).split("&");
	var result    = "";

	for ( var i = 0 ; i < arrParams.length ; i++ ) {
		var p = arrParams[i];
		var arrP = p.split("=");

		result = result + "&" + prefix + arrP[0] + "=" + arrP[1];
	}

	if(result.indexOf("%26nbsp%3B") > -1)
		result = result.split("%26nbsp%3B").join("");
	
	return result;
}

/**
 * 
 * @param gridId
 */
function ComAddRow(gridName){
	$(gridName).jqGrid('editGridRow',"new",{height:280,reloadAfterSubmit:false});
}

/**
 * �좏깮���됱쓽 DATA 瑜��섍꺼以뚮땲��
 * @param gridName
 * @returns
 */
function ComSelectRow(gridName){
	var ret;
	var id = $(gridName).getGridParam('selrow');
	if(id){
		ret = $(gridName).getRowData(id);
	}else{
		alert("Please Choose A Row");
	}
	return ret;
}

/**
 * 
 */
function ComMultiSelectRow(gridName){
	const ret = [];
	var ids = $(gridName).getGridParam('selarrrow');
	for ( var i = 0; i < ids.length; i++) {
		ret.push($(gridName).getRowData(ids[i]));
	}
	return ret;
}

/**
 * grid edit input
 * @param gridName
 * @param rowId
 * @param colIndex
 */
//function ComRowEdit(gridName, rowId, colIndex, callBack){
function ComRowEdit(gridName, rowId, callBack){

	if(cellEditRow !== null){
		$(gridName).saveRow(cellEditRow, false, 'clientArray');
	}
	//var colModel = $(gridName).getGridParam('colModel');
	//if(colModel[colIndex].editable){
	if ( callBack == undefined )
		$(gridName).editRow(rowId, true);
	else
		$(gridName).editRow(rowId, true, callBack);
	//}

	cellEditRow = rowId;
}

function ComRowEdit2(gridName, rowId, callBack){
	if ( callBack == undefined )
		$(gridName).editRow(rowId, true);
	else
		$(gridName).editRow(rowId, true, callBack);
}

function ComAllRowEdit(gridName){

	var rows = $(gridName).getDataIDs();

	for ( var i = 0; i < rows.length; i++) {
		$(gridName).editRow(rows[i], false);
	}
}

function ComAllRowSave(gridName){

	var rows = $(gridName).getDataIDs();

	for ( var i = 0; i < rows.length; i++) {
		$(gridName).saveRow(rows[i], false, 'clientArray');
	}
}

/**
 * grid save input
 * @param gridName
 * @param rowId
 */
function ComRowSave(gridName, rowId){
	$(gridName).saveRow(rowId, false, 'clientArray');
}

/**
 * grid restore input
 * @param gridName
 * @param rowId
 */
function ComRowRestore(gridName, rowId){
	$(girdName).restoreRow(rowId);
}

/**
 * Set grid combo data
 * @param gridName
 * @param colName
 * @param comboData
 */
function ComSetColProp(gridName, colName, comboData){
	$(gridName).setColProp(colName, { editoptions: { value: comboData}});
}


/**
 * change array object
 * @param arrObj
 * @param prefix
 * @returns {Array}
 */
function ComArrObjData(arrObj, prefix){

	if ( prefix == undefined ) {prefix = "";}

	var gridData = "";
	var paras = new Array();

	for ( var i = 0 ; i < arrObj.length ; i++ ) {
		paras.push($.param(arrObj[i]));
	}

	for ( var int = 0; int < paras.length; int++) {
		gridData = gridData + paras[int] + "&";
	}
	
	var arrParams = gridData.substring(0, gridData.length -1).split("&");
	var result    = "";

	for ( var i = 0 ; i < arrParams.length ; i++ ) {
		var p = arrParams[i];
		var arrP = p.split("=");

		result = result + "&" + prefix + arrP[0] + "=" + arrP[1];
	}

	return result;
}

/**
 * local data sort
 * @param gridName
 * @param sortType
 */
function ComLocalDataSort(gridName, sortType){
	var localData = $(gridName).jqGrid('getRowData');
	$(gridName).setGridParam({datatype: "local", sortorder:sortType});
	ComGridAction(gridName, localData);
	$(gridName).trigger("reloadGrid");
}

function ComPager(result){
	
}