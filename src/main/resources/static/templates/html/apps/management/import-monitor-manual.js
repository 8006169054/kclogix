var tableName = '#port-table';
let prevColIndex = null;
let prevRowId = null;
var gridHeight = 700;
//var gridHeight = 580;
$( document ).ready(function() {
   	portTableInit();
	searchPartnerAutocomplete();
   	searchCargoAutocomplete();
   	searchTerminalAutocomplete();
   	searchCustomerAutocomplete();
});

$('#sreturnDate').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  startDate: moment().subtract(30, 'days').format('YYYY-MM-DD'),
  drops: 'down',
  opens: 'right'
});

$('#sata').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  autoUpdateInput: false,
  drops: 'down',
  opens: 'right'
});

// 날짜 선택 시 input 값 수동으로 설정
$('#sata').on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$('#serd').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  startDate: moment().subtract(30, 'days').format('YYYY-MM-DD'),
  drops: 'down',
  opens: 'right'
});

$('#sprofitDate').daterangepicker({
	startDate: moment().subtract(30, 'days').format('YYYY-MM-DD'),
  locale: {format: 'YYYY-MM-DD'},
  drops: 'down',
  opens: 'right'
});

//$('#sdemRcvd').daterangepicker({
//  locale: {format: 'YYYY-MM-DD'},
//  startDate: moment().subtract(30, 'days').format('YYYY-MM-DD'),
//  drops: 'down',
//  opens: 'right'
//});

//$('#sdemRcvd').daterangepicker({
//  locale: {format: 'YYYY-MM-DD'},
//  autoUpdateInput: false,
//  drops: 'down',
//  opens: 'right'
//});

var partnerList = [];
var carGoList = [];
var terminalList = [];
var customerList = [];
/**
 * 조회
 */
async function search() {
	$(tableName).clearGridData();
	let response = await requestApi('GET', '/api/management/website-terminal-code', $('#searchFrom').serializeObject());
	$(tableName).searchData(response.data, {editor: true, nodatamsg: true});
	response = null;
}

async function portTableInit(){
	$(tableName).jqGrid({
		url: '/api/management/website-terminal-code-init',  
		mtype: 'GET',
	   	datatype: "json",
	   	colNames: ['','cargo','concine code', 'seq', 'uuid', 'HBL NO.', 'Tank no.', '매출', '이월 매출', 'A/N&EDI', 'INVOICE', 'CNEE', 'PIC', 'SHIPMENT STATUS', 'PROFIT DATE', '국내매출', '해외매출', "Q'ty", 'Partner', 'Term', 'Name', 'Date', 'Location', 'Vessel / Voyage', 'Carrier', 'MBL NO.', 'POL', 'POD', 'terminalCode', 'Name', 'Link', 'ETD', 'ETA', 'ATA', '비고', 'F/T', 'DEM RATE', 'END OF F/T', 'ESTIMATE RETURN DATE', 'RETURN DATE', 'RETURN DEPOT', 'DEM STATUS', 'TOTAL DEM', 'DEM BILLING', 'DEM RCVD', 'COMMISSION DEM', 'DEM COMMISSION', 'DEPOT IN DATE(REPO ONLY)', 'REPOSITION 매입'],
	   	colModel: [
	   		{ name: 'jqFlag',				width: 40,		align:'center', 	hidden : false,	frozen:true},
	   		{ name: 'cargo',				width: 100,		align:'center', 	rowspan: true,	editable : true, hidden : true,	frozen:true},
	   		{ name: 'concine', 				width: 70, 		align:'center',		rowspan: true,	editable : true, hidden : true,	frozen:true},
	   		{ name: 'seq', 					width: 50, 		align:'center',		hidden : true,	frozen:true},
	   		{ name: 'uuid', 				width: 50, 		align:'center',		hidden : true,	frozen:true},
	       	{ name: 'hblNo', 				width: 140, 	align:'center',		rowspan: true,	frozen:true},
	       	{ name: 'tankNo', 				width: 120, 	align:'center', 	editable: true,	frozen:true},
	       	{ name: 'sales', 				width: 50, 		align:'center',		rowspan: true,	editable: true},
	       	{ name: 'carryoverSales', 		width: 50, 		align:'center',		rowspan: true,	editable: true},
	       	{ name: 'arrivalNotice',		width: 70, 		align:'center',		rowspan: true},
	       	{ name: 'invoice', 				width: 70, 		align:'center',		rowspan: true},
	    	{ name: 'concineName',			width: 150, 	align:'center',		rowspan: true, editable: true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						source: customerList,
						delay: 100,
						autoFocus: true,
						minChars: 0,
						minLength: 0,
				        select: function (event, ui) {
							ComSetCellData(tableName, ComSelectIndex(tableName), 'concine', ui.item.code, true);
							ComSetCellData(tableName, ComSelectIndex(tableName), 'concinePic', ui.item.pic, true);
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        });
				}
			}},
			{ name: 'concinePic', 			width: 80, 		align:'center',		rowspan: true},
			{ name: 'shipmentStatus', 		width: 80, 		align:'center',		rowspan: true, editable: true, formatter:'select', edittype:'select', editoptions : {value: 'Y:ON;N:OFF'}},
	    	{ name: 'profitDate', 			width: 90, 		align:'center',		rowspan: true, editable: true, edittype: "date"},
	    	{ name: 'domesticSales', 		width: 80, 		align:'center',		rowspan: true, editable: true},
	    	{ name: 'foreignSales', 		width: 80, 		align:'center',		rowspan: true, editable: true},
	    	{ name: 'quantity', 			width: 50, 		align:'center',		rowspan: true, editable: true},
	    	{ name: 'partner',				width: 100, 	align:'center', 	rowspan: false, editable : true, editable : true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						source: partnerList,
						delay: 100,
						autoFocus: true,
						minChars: 0,
						minLength: 0,
				        select: function (event, ui) {
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        });
				}
			}},
	    	{ name: 'term', 				width: 80, 		align:'center',		rowspan: true, editable: true},
	    	{ name: 'item',					width: 220, 	align:'center', 	rowspan: true, editable : true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						source: carGoList,
						delay: 100,
						autoFocus: true,
						minChars: 0,
						minLength: 0,
				        select: function (event, ui) {
							ComSetCellData(tableName, ComSelectIndex(tableName), 2, ui.item.code, true);
							ComSetCellData(tableName, ComSelectIndex(tableName), 'cargoDate', ui.item.cargoDate, true);
							ComSetCellData(tableName, ComSelectIndex(tableName), 'location', ui.item.location, true);
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        });
				}
			}},
			{ name: 'cargoDate', 			width: 80, 		align:'center',		rowspan: true},
			{ name: 'location', 			width: 100, 	align:'center',		rowspan: true},
	    	{ name: 'vesselVoyage', 		width: 200, 	align:'center',		rowspan: true, editable: true},
	    	{ name: 'carrier', 				width: 80, 		align:'center',		rowspan: true, editable: true},
	    	{ name: 'mblNo', 				width: 140, 	align:'center',		rowspan: true, editable: true},
	    	{ name: 'pol', 					width: 100, 	align:'center',		rowspan: true, editable: true},
	    	{ name: 'pod', 					width: 100, 	align:'center'},
	    	{ name: 'terminalCode', 		width: 100, 	align:'center', 	hidden : true,},
	    	{ name: 'terminalName', 		width: 150, 	align:'center',		editable : true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						source: terminalList,
						delay: 100,
						autoFocus: true,
						minChars: 0,
						minLength: 0,
				        select: function (event, ui) {
							ComSetCellData(tableName, ComSelectIndex(tableName), 'terminalCode', ui.item.code, true);
							ComSetCellData(tableName, ComSelectIndex(tableName), 'pod', ui.item.region, true);
							ComSetCellData(tableName, ComSelectIndex(tableName), 'terminalHomepage', ui.item.homepage, true);
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        });
				}
			}},
	    	{ name: 'terminalHomepage', 	width: 60, 	align:'center', formatter: terminalFn},
	    	{ name: 'etd', 					width: 90, 		align:'center', editable: true, edittype: "date"},
	    	{ name: 'eta', 					width: 90, 		align:'center', editable: true, edittype: "date"},
	       	{ name: 'ata', 					width: 90, 		align:'center', editable: true},
	       	{ name: 'remark', 				width: 250, 	align:'center', editable: true,	rowspan: true, edittype: 'textarea'},
	       	{ name: 'ft', 					width: 70, 		align:'center', editable: true},
	       	{ name: 'demRate', 				width: 80, 		align:'center', editable: true},
	       	{ name: 'endOfFt', 				width: 90, 		align:'center', editable: true, edittype: "date"},
	       	{ name: 'estimateReturnDate', 	width: 160, 	align:'center', editable: true, edittype: "date"},
	       	{ name: 'returnDate', 			width: 100, 	align:'center', editable: true, edittype: "date"},
	       	{ name: 'returnDepot', 			width: 100, 	align:'center', editable: true},
	       	{ name: 'demStatus', 			width: 100, 	align:'center', editable: true, formatter:'select', edittype:'select', editoptions : {value: 'Y:Y;N:N'}},
	       	{ name: 'totalDem', 			width: 100, 	align:'center', editable: true},
	       	{ name: 'demReceived', 			width: 80, 		align:'center', editable: true},
	       	{ name: 'demRcvd', 				width: 90, 		align:'center', editable: true},
	       	{ name: 'demPrch', 				width: 100, 	align:'center', editable: true},
	       	{ name: 'demSales', 			width: 100, 	align:'center', editable: true},
	       	{ name: 'depotInDate', 			width: 180, 	align:'center', editable: true, edittype: "date"},
	       	{ name: 'repositionPrch', 		width: 120, 	align:'center', editable: true}
	   	],
		height: gridHeight,
		width: '100%',
		dblEdit : true,
		frozen: true,
		delselect: true,
//		multiselect: true,
		onCellSelect: function (rowId, iCol, cellContent, event) {
//    		const grid = $(tableName);
//	    	const colModel = grid.jqGrid("getGridParam", "colModel");
//	    	const colName = colModel[iCol].name;
//	    	// 모든 행 ID 가져오기
//	    	const rowIds = grid.getDataIDs();
//	
//			// 이전 컬럼 색상 원복
//		    if (prevColIndex !== null) {
//		      const prevColName = colModel[prevColIndex].name;
//		      rowIds.forEach(id => {
//		        	grid.jqGrid("setCell", id, prevColName, "", { background: "" });
//		      });
//		    }
//		    $("#" + prevRowId).css("background-color", "");
//		    
//	    	// 현재 선택된 컬럼 색상 적용
//		    rowIds.forEach(id => {
//		      grid.jqGrid("setCell", id, colName, "", {
//		        background: "#d4edda" // 연한 녹색
//		      });
//		    });
//		       
//		    // 현재 행 전체 색상 적용
//    		$("#" + rowId).css("background-color", "#d4edda");
//
//			// 고정 컬럼 영역도 함께 강조
//			$(tableName).closest(".ui-jqgrid").find(".frozen-bdiv")
//			  .find("tr[id='" + rowId + "']")
//			  .css("background-color", "#d4edda");
//  
//		    // 📌 현재 선택 상태 저장
//		    prevColIndex = iCol;
//		    prevRowId = rowId;
//		    
//		    console.log($(tableName));
		    
		},
		afterSaveCell : function(rowid, cellname, value, iRow, iCol) {
			var changeVal = false;
			if('terminalName' === cellname){
				if(value === ''){
					ComSetCellData(tableName, iRow, 'terminalCode', '', true);
					ComSetCellData(tableName, iRow, 'pod', '', true);
					ComSetCellData(tableName, iRow, 'terminalHomepage', '', true);
				}else{
					for (let terminal of terminalList) {
						if(terminal.value === value){
							changeVal = true;
							return false;
						}
					}
				}
			}else if('partner' === cellname){
				if(value != ''){
					for (let partner of partnerList) {
						if(partner.value === value){
							changeVal = true;
							return false;
						}
					}
				}
			}else if('item' === cellname){
				if(value === ''){
					ComSetCellData(tableName, iRow, 'cargo', '', true);
					ComSetCellData(tableName, iRow, 'cargoDate', '', true);
					ComSetCellData(tableName, iRow, 'location', '', true);
				}else{
					for (let carGo of carGoList) {
						if(carGo.value === value){
							changeVal = true;
							return false;
						}
					}
				}
			}else if('concineName' === cellname){
				if(value === ''){
					ComSetCellData(tableName, iRow, 'concine', '', true);
					ComSetCellData(tableName, iRow, 'concinePic', '', true);
				}else{
					for (let customer of customerList) {
						if(customer.value === value){
							changeVal = true;
							return false;
						}
					}
				}
			}
			
			if(!changeVal) $(tableName).jqGrid('dataRecovery', rowid, cellname);
		}
	});
	
	$(tableName).jqGrid('setGroupHeaders', {
				useColSpanStyle: true,
				groupHeaders: [
                                {startColumnName:'item', numberOfColumns: 3, titleText: 'Item' },
                                {startColumnName:'pod', numberOfColumns: 4, titleText: 'Terminal' },
                                {startColumnName:'concineName', numberOfColumns: 2, titleText: 'Customer' }
                                
                              ]
		});
		
//	let response = await requestApi('GET', '/api/management/website-terminal-code-init');
//	$(tableName).searchData(response.data, {editor: true});
//	response = null;
}

async function searchPartnerAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/partner/autocomplete');
	if(response.common.status === 'S'){
		partnerList = response.data;
		partnerAutocompleteLoad();
		
	$("#spartner").autocomplete({
		source: partnerList,
		delay: 100,
		autoFocus: true,
		minChars: 0,
		minLength: 0,
		open: function(){
	        $(this).autocomplete('widget').css('z-index', 1100);
	        return false;
	    },
	    select: function (event, ui) {
	    },
	    close : function (event, ui) {

	        return false;
	    }
	}).focus(function() {
	    $(this).autocomplete("search", $(this).val());
	});
	
	}
}

async function searchCargoAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/cargo/autocomplete');
	if(response.common.status === 'S'){
		carGoList = response.data;
		itemAutocompleteLoad();
		
		$("#sitem").autocomplete({
		source: carGoList,
		delay: 100,
		autoFocus: true,
		minChars: 0,
		minLength: 0,
		open: function(){
	        $(this).autocomplete('widget').css('z-index', 1100);
	        return false;
	    },
	    select: function (event, ui) {
	    	$('#scargo').val(ui.item.code);
	    },
	    close : function (event, ui) {
	        return false;
	    }
	}).focus(function() {
	    $(this).autocomplete("search", $(this).val());
	});
		
	}
}

async function searchTerminalAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/terminal/autocomplete');
	if(response.common.status === 'S'){
		terminalList = response.data;
		terminalAutocompleteLoad();
	}
}

async function searchCustomerAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/customer/autocomplete');
	if(response.common.status === 'S'){
		customerList = response.data;
		customerAutocompleteLoad();
	}
}


function terminalFn (cellvalue, options, rowObject ){
	if(emptyChange(rowObject.terminalHomepage) === '')
		return '';
	else
		return '<a href="' + rowObject.terminalHomepage + '" target="_blank"><img src="/assets/img/popup.png" height="22px"></a>';
}

async function save(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
		await requestApi('POST', '/api/management/save-port', saveData, {successFn : portSaveFn, errorFn : portSaveFn});
	}
}

function portSaveFn(response){
	if(response.common.status === 'S'){
 		search();
 	}
}

function frozenCelHide(){
	var frozenCelNotVal = [];
	var frozenCelVal = $('#frozenCel').val();
	$("#frozenCel > option").each(function() {
		frozenCelNotVal.push(this.value);
	});
	frozenCelNotVal = frozenCelNotVal.filter(x => !frozenCelVal.includes(x));
	$(tableName).hideCol(frozenCelVal);
	$(tableName).showCol(frozenCelNotVal);
	$(tableName).refreshFrozen();
}

function collapse(){
	if ($('#collapseExample').hasClass('collapse show')) {
//		setTimeout(function() {
			$(tableName).jqGrid("setGridHeight", gridHeight);
//		}, 50); 
	} else {
		$(tableName).jqGrid("setGridHeight", gridHeight - 125);
	}
}

function demRcvdSelectOnchange(){
	if($('#sdemRcvdSelect').val() === '1'){
		$('#sdemRcvd1').hide();
		$('#sdemRcvd').show();
		$('#sdemRcvd-calendar').show();
	}else if($('#sdemRcvdSelect').val() === '2'){
		$('#sdemRcvd1').show();
		$('#sdemRcvd').hide();
		$('#sdemRcvd-calendar').hide();
		$('#sdemRcvd1').val('N/A');
	}else if($('#sdemRcvdSelect').val() === '3' || $('#sdemRcvdSelect').val() === '0'){
		$('#sdemRcvd1').show();
		$('#sdemRcvd').hide();
		$('#sdemRcvd-calendar').hide();
		$('#sdemRcvd1').val('');
	}
}