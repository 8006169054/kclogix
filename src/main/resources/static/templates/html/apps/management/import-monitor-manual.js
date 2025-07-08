var tableName = '#port-table';
let prevColIndex = null;
let prevRowId = null;
var gridHeight = 700;
//var gridHeight = 580;

$('#sreturnDate').daterangepicker({
  	locale: {format: 'YYYY-MM-DD'},
	autoUpdateInput: false,
  	drops: 'down',
  	opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$('#sata').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  autoUpdateInput: false,
  drops: 'down',
  opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$('#serd').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  autoUpdateInput: false,
  drops: 'down',
  opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$('#sprofitDate').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  autoUpdateInput: false,
  drops: 'down',
  opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

var partnerList = [];
var carGoList = [];
var terminalList = [];
var terminalCodeList = [];
var customerList = [];

/**
 * 조회
 */
async function search() {
	$("#initSearch").val("false");
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
	   	colNames: ['','cargo','concine code', 'seq', 'uuid', 'HBL NO.', 'Tank no.', '매출', '이월 매출', 'A/N&EDI', 'INVOICE', 'CNEE', 'PIC', 'SHIPMENT STATUS', 'PROFIT DATE', '국내매출', '해외매출', "Q'ty", 'Partner', 'Term', 'Name', 'Date', 'Location', 'Vessel / Voyage', 'Carrier', 'MBL NO.', 'POL', 'POD', 'Code1', 'Code', 'Name', 'Link', 'ETD', 'ETA', 'ATA', '비고', 'F/T', 'DEM RATE', 'END OF F/T', 'ESTIMATE RETURN DATE', 'RETURN DATE', 'RETURN DEPOT', 'DEM STATUS', 'TOTAL DEM', 'DEM BILLING', 'DEM RCVD', 'DEM(USD) COMMISSION', 'DEM COMMISSION', 'DEPOT IN DATE(REPO ONLY)', 'REPOSITION 매입'],
	   	colModel: [
	   		{ name: 'jqFlag',				width: 40,		index: 1,align:'center', 	hidden : false,	frozen:true},
	   		{ name: 'cargo',				width: 100,		index: 2,align:'center', 		hidden : true, rowspan: true,	editable : true, frozen:true},
	   		{ name: 'concine', 				width: 70, 		index: 3,align:'center',		hidden : true, rowspan: true,	editable : true, frozen:true},
	   		{ name: 'seq', 					width: 50, 		index: 4,align:'center',		hidden : true,	frozen:true},
	   		{ name: 'uuid', 				width: 50, 		index: 5,align:'center',		hidden : true,	frozen:true},
	       	{ name: 'hblNo', 				width: 140, 	index: 6,align:'center',		hidden : false, rowspan: true,	frozen:true}, /**여기서부터 히든처리 */
	       	{ name: 'tankNo', 				width: 120, 	index: 7,align:'center', 	hidden : false, editable: true,	frozen:true},
	       	{ name: 'sales', 				width: 50, 		index: 8,align:'center',		hidden : false, rowspan: true,	editable: true},
	       	{ name: 'carryoverSales', 		width: 50, 		index: 9,align:'center',		hidden : false, rowspan: true,	editable: true},
	       	{ name: 'arrivalNotice',		width: 70, 		index: 10,align:'center',		hidden : false, rowspan: true},
	       	{ name: 'invoice', 				width: 70, 		index: 11,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'concineName',			width: 150, 	index: 12,align:'center',		hidden : false, rowspan: true, editable: true, editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						delay: 100,
						autoFocus: true,
						minLength: 0,
						source: function(request, response) {
						    const results = $.ui.autocomplete.filter(customerList, request.term);
						    if (results.length === 0) {
						      results.push({
						        label: "No results found",
						        value: ""
						      });
						    }
						    response(results);
						},
				        select: function (event, ui) {
							ComSetCellData(tableName, ComSelectIndex(tableName), 3, ui.item.code, true);
							ComSetCellData(tableName, ComSelectIndex(tableName), 13, ui.item.pic, true);
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        }).on("paste", async function() {
						var text = await navigator.clipboard.readText();
						$(elem).val(text);
					});
				}
			}},
			{ name: 'concinePic', 			width: 80, 		index: 13,align:'center',		hidden : false, rowspan: true},
			{ name: 'shipmentStatus', 		width: 80, 		index: 14,align:'center',		hidden : false, rowspan: true, editable: true, formatter:'select', edittype:'select', editoptions : {value: 'Y:IN PROGRES;N:CLOSED'}},
	    	{ name: 'profitDate', 			width: 90, 		index: 15,align:'center',		hidden : false, rowspan: true, editable: true, edittype: "date"},
	    	{ name: 'domesticSales', 		width: 80, 		index: 16,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'foreignSales', 		width: 80, 		index: 17,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'quantity', 			width: 50, 		index: 18,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'partner',				width: 100, 	index: 19,align:'center', 	hidden : false, rowspan: false, editable : true, editable : true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						delay: 100,
						autoFocus: true,
						minLength: 0,
						source: function(request, response) {
						    const results = $.ui.autocomplete.filter(partnerList, request.term);
						    if (results.length === 0) {
						      results.push({
						        label: "No results found",
						        value: ""
						      });
						    }
						    response(results);
						},
				        select: function (event, ui) {
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        }).on("paste", async function() {
						var text = await navigator.clipboard.readText();
						$(elem).val(text);
					});
				}
			}},
	    	{ name: 'term', 				width: 80, 		index: 20,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'item',					width: 220, 	index: 21,align:'center', 	hidden : false, rowspan: true, editable : true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						delay: 100,
						autoFocus: true,
						minLength: 0,
						source: function(request, response) {
						    const results = $.ui.autocomplete.filter(carGoList, request.term);
						    if (results.length === 0) {
						      results.push({
						        label: "No results found",
						        value: ""
						      });
						    }
						    response(results);
						},
				        select: function (event, ui) {
							ComSetCellData(tableName, ComSelectIndex(tableName), 2, ui.item.code, true);
							ComSetCellData(tableName, ComSelectIndex(tableName), 22, ui.item.cargoDate, true);
							ComSetCellData(tableName, ComSelectIndex(tableName), 23, ui.item.location, true);
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        }).on("paste", async function() {
						var text = await navigator.clipboard.readText();
						$(elem).val(text);
					});
				}
			}},
			{ name: 'cargoDate', 			width: 80, 		index: 22,align:'center',		hidden : true, rowspan: true},
			{ name: 'location', 			width: 100, 	index: 23,align:'center',		hidden : false, rowspan: true},
	    	{ name: 'vesselVoyage', 		width: 200, 	index: 24,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'carrier', 				width: 80, 		index: 25,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'mblNo', 				width: 140, 	index: 26,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'pol', 					width: 100, 	index: 27,align:'center',		hidden : false, rowspan: true, editable: true},
	    	{ name: 'pod', 					width: 100, 	index: 28,align:'center',		hidden : false, rowspan: true},
	    	{ name: 'terminalCode', 		width: 100, 	index: 29,align:'center', 		hidden : true, rowspan: true},
	    	{ name: 'parkingLotCode', 		width: 80, 		index: 30,align:'center', 		hidden : false, rowspan: true,	editable : true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						delay: 100,
						autoFocus: true,
						minLength: 0,
						source: function(request, response) {
						    const results = $.ui.autocomplete.filter(terminalCodeList, request.term);
						    if (results.length === 0) {
						      results.push({
						        label: "No results found",
						        value: ""
						      });
						    }
						    response(results);
						},
				        select: function (event, ui) {
							if(ui.item.code != undefined){
								ComSetCellData(tableName, ComSelectIndex(tableName), 28, ui.item.region, true);
								ComSetCellData(tableName, ComSelectIndex(tableName), 29, ui.item.code, true);
								ComSetCellData(tableName, ComSelectIndex(tableName), 31, ui.item.name, true);
								ComSetCellData(tableName, ComSelectIndex(tableName), 32, ui.item.homepage, true);
							}
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        }).on("paste", async function() {
						var text = await navigator.clipboard.readText();
						$(elem).val(text);
					});
				}
			}},
	    	{ name: 'terminalName', 		width: 150, 	index: 31,align:'center',		hidden : false, rowspan: true, editable : true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						delay: 100,
						autoFocus: true,
						minLength: 0,
						source: function(request, response) {
						    const results = $.ui.autocomplete.filter(terminalList, request.term);
						    if (results.length === 0) {
						      results.push({
						        label: "No results found",
						        value: ""
						      });
						    }
						    response(results);
						},
				        select: function (event, ui) {
							if(ui.item.code != undefined){
								ComSetCellData(tableName, ComSelectIndex(tableName), 28, ui.item.region, true);
								ComSetCellData(tableName, ComSelectIndex(tableName), 29, ui.item.code, true);
								ComSetCellData(tableName, ComSelectIndex(tableName), 30, ui.item.parkingLotCode, true);
								ComSetCellData(tableName, ComSelectIndex(tableName), 32, ui.item.homepage, true);
							}
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        }).on("paste", async function() {
						var text = await navigator.clipboard.readText();
						$(elem).val(text);
					});
				}
			}},
	    	{ name: 'terminalHomepage', 	width: 60, 		index: 32,align:'center', hidden : false, rowspan: true, formatter: terminalFn},
	    	{ name: 'etd', 					width: 90, 		index: 33,align:'center', editable: true, edittype: "date"},
	    	{ name: 'eta', 					width: 90, 		index: 34,align:'center', editable: true, edittype: "date"},
	       	{ name: 'ata', 					width: 90, 		index: 35,align:'center', editable: true, edittype: "date"},
	       	{ name: 'remark', 				width: 250, 	index: 36,align:'center', editable: true,	rowspan: true, edittype: 'textarea'},
	       	{ name: 'ft', 					width: 70, 		index: 37,align:'center', editable: true},
	       	{ name: 'demRate', 				width: 80, 		index: 38,align:'center', editable: true},
	       	{ name: 'endOfFt', 				width: 90, 		index: 39,align:'center', editable: true, editoptions : {pk:true}, edittype: "date"},
	       	{ name: 'estimateReturnDate', 	width: 160, 	index: 40,align:'center', editable: true, edittype: "date"},
	       	{ name: 'returnDate', 			width: 100, 	index: 41,align:'center', editable: true, edittype: "date"},
	       	{ name: 'returnDepot', 			width: 100, 	index: 42,align:'center', editable: true},
	       	{ name: 'demStatus', 			width: 100, 	index: 43,align:'center', editable: false, formatter:'select', edittype:'select', editoptions : {value: 'Y:Y;N:N'}},
	       	{ name: 'totalDem', 			width: 100, 	index: 44,align:'center', editable: true, editoptions : {pk:true}},
	       	{ name: 'demReceived', 			width: 80, 		index: 45,align:'center', editable: true},
	       	{ name: 'demRcvd', 				width: 90, 		index: 46,align:'center', editable: true},
	       	{ name: 'demPrch', 				width: 100, 	index: 47,align:'center', editable: true, editoptions : {pk:true}},
	       	{ name: 'demSales', 			width: 100, 	index: 48,align:'center', editable: true, editoptions : {pk:true}},
	       	{ name: 'depotInDate', 			width: 180, 	index: 49,align:'center', editable: true, edittype: "date"},
	       	{ name: 'repositionPrch', 		width: 120, 	index: 50,align:'center', editable: true}
	   	],
		height: gridHeight,
		width: '100%',
		dblEdit : true,
		frozen: true,
		delselect: true,
		onCellSelect: function (rowId, iCol, cellContent, event) {
    		const grid = $(tableName);
	    	const colModel = grid.jqGrid("getGridParam", "colModel");
	    	const colName = colModel[iCol].name;
	    	// 모든 행 ID 가져오기
	    	const rowIds = grid.getDataIDs();
	
			// 이전 컬럼 색상 원복
		    if (prevColIndex !== null) {
		      const prevColName = colModel[prevColIndex].name;
		      rowIds.forEach(id => {
		        	grid.jqGrid("setCell", id, prevColName, "", { background: "" });
		      });
		    }
		    $("#" + prevRowId).css("background-color", "");
		    
	    	// 현재 선택된 컬럼 색상 적용
		    rowIds.forEach(id => {
		      grid.jqGrid("setCell", id, colName, "", {
		        background: "#d4edda" // 연한 녹색
		      });
		    });
		       
		    // 현재 행 전체 색상 적용
    		$("#" + rowId).css("background-color", "#d4edda");

			// 고정 컬럼 영역도 함께 강조
			$(tableName).closest(".ui-jqgrid").find(".frozen-bdiv")
			  .find("tr[id='" + rowId + "']")
			  .css("background-color", "#d4edda");
  
		    // 현재 선택 상태 저장
		    prevColIndex = iCol;
		    prevRowId = rowId;
		},
		afterSaveCell : function(rowid, cellname, value, iRow, iCol) {
			console.log('afterSaveCell', rowid, cellname, value, iRow, iCol);
			if('terminalName' === cellname || 'parkingLotCode' === cellname || 'partner' === cellname || 'item' === cellname || 'concineName' === cellname){
				var changeVal = false;
				if('terminalName' === cellname || 'parkingLotCode' === cellname){
					if(value === ''){
//						ComSetCellData(tableName, iRow, 'terminalCode', '', true);
//						ComSetCellData(tableName, iRow, 'parkingLotCode', '', true);
//						ComSetCellData(tableName, iRow, 'pod', '', true);
//						ComSetCellData(tableName, iRow, 'terminalHomepage', '', true);
						ComSetCellData(tableName, iRow, 28, value, true);
						ComSetCellData(tableName, iRow, 29, value, true);
						ComSetCellData(tableName, iRow, 30, value, true);
						ComSetCellData(tableName, iRow, 31, value, true);
						ComSetCellData(tableName, iRow, 32, value, true);
					}else{
						console.log('value', value);
						for (let terminal of terminalList) {
							if(terminal.value === value || terminal.parkingLotCode === value){
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
			}else if('demStatus' === cellname && value === 'N'){
				confirmMessage('TOTAL DEM, DEM BILLING, DEM RCVD, DEM(USD) COMMISSION, DEM COMMISSION 5개 컬럼 값이 N/A 변경됩니다.', 'info', '', demStatusChang);
			}else if('shipmentStatus' === cellname && value === 'N'){
				var cellValue = $(tableName).jqGrid('getCell', iRow, 'profitDate');
				if(isEmpty(cellValue)){
					alertMessage('Please enter the PROFIT DATE before setting SHIPMENT STATUS to OFF.', 'warning');
					$(tableName).jqGrid('dataRecovery', rowid, cellname);
				}
			}
			else if('returnDate' === cellname || 'ata' === cellname || 'ft' === cellname){
				if('ata' === cellname || 'ft' === cellname){
					var ata = $(tableName).jqGrid('getCell', iRow, 'ata');
					var ft = $(tableName).jqGrid('getCell', iRow, 'ft');
					ComSetCellData(tableName, iRow, 'endOfFt', endOfFt(ata, ft));
				}
				
				multiFn(iRow);
			}
		}
	});
	
	$(tableName).jqGrid('setGroupHeaders', {
				useColSpanStyle: true,
				groupHeaders: [
                                {startColumnName:'item', numberOfColumns: 3, titleText: 'Item' },
                                {startColumnName:'pod', numberOfColumns: 5, titleText: 'Terminal' },
                                {startColumnName:'concineName', numberOfColumns: 2, titleText: 'Customer' }
                                
                              ]
		});
		
//		let response = await requestApi('GET', '/api/management/website-terminal-code-init');
//		$(tableName).searchData(response.data, {editor: true});
//		response = null;
}

async function demStatusChang(selection){
	if(selection){
		var iRow = ComSelectIndex(tableName);
		ComSetCellData(tableName, iRow, 'totalDem', 'N/A');
		ComSetCellData(tableName, iRow, 'demReceived', 'N/A');
		ComSetCellData(tableName, iRow, 'demRcvd', 'N/A');
		ComSetCellData(tableName, iRow, 'demPrch', 'N/A');
		ComSetCellData(tableName, iRow, 'demSales', 'N/A');
	}
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
	
	response = await requestApi('GET', '/api/mdm/terminal/autocompleteParkingLotCode');
	if(response.common.status === 'S'){
		terminalCodeList = response.data;
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

async function confirmSave(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
//		message, type, title, callFn
		var del = 0;
		var up = 0;
		for(var i=0; i<saveData.length; i++){
			if(saveData[i].jqFlag === 'U') up++;
			else if(saveData[i].jqFlag === 'D') del++;
		}
		var msg = 'There are ' + del + ' deletions and ' + up + ' modifications. Save changes?'
		confirmMessage(msg, 'info', 'Save', save);
	}
}

async function save(selection){
	if(selection){
		var saveData = $(tableName).saveGridData();
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

function clearSearchBox(){
	$('#searchFrom')[0].reset();
//	let sreturnDate = $('#sreturnDate').data('daterangepicker');
//   	sreturnDate.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
//	sreturnDate.setEndDate(moment().format('YYYY-MM-DD'));
//	sreturnDate.element.trigger('apply.daterangepicker', sreturnDate);
//	
//	let serd = $('#serd').data('daterangepicker');
//   	serd.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
//	serd.setEndDate(moment().format('YYYY-MM-DD'));
//	serd.element.trigger('apply.daterangepicker', serd);
//	
//	let sprofitDate = $('#sprofitDate').data('daterangepicker');
//   	sprofitDate.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
//	sprofitDate.setEndDate(moment().format('YYYY-MM-DD'));
//	sprofitDate.element.trigger('apply.daterangepicker', sprofitDate);
	
	$('#sshipmentStatus').val('Y').trigger('change');
	$('#sdemRcvdSelect').val('0').trigger('change');
}

$( document ).ready(function() {
   	portTableInit();
	searchPartnerAutocomplete();
   	searchCargoAutocomplete();
   	searchTerminalAutocomplete();
   	searchCustomerAutocomplete();
   	
//   	let sreturnDate = $('#sreturnDate').data('daterangepicker');
//   	sreturnDate.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
//	sreturnDate.setEndDate(moment().format('YYYY-MM-DD'));
//	sreturnDate.element.trigger('apply.daterangepicker', sreturnDate);
//	
//	let serd = $('#serd').data('daterangepicker');
//   	serd.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
//	serd.setEndDate(moment().format('YYYY-MM-DD'));
//	serd.element.trigger('apply.daterangepicker', serd);
//	
//	let sprofitDate = $('#sprofitDate').data('daterangepicker');
//   	sprofitDate.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
//	sprofitDate.setEndDate(moment().format('YYYY-MM-DD'));
//	sprofitDate.element.trigger('apply.daterangepicker', sprofitDate);
});

function calculate(){
	if(prevRowId === null) alertMessage(getMessage('0000'), 'info');
	multiFn(prevRowId);
}

/**
 * 
 */
function multiFn(iRow){
	var returnDate = $(tableName).jqGrid('getCell', iRow, 'returnDate');
	var endOfFt = $(tableName).jqGrid('getCell', iRow, 'endOfFt');
	var demRate = $(tableName).jqGrid('getCell', iRow, 'demRate');
	var totalDem = calculateDemurrage(returnDate, endOfFt, demRate);
	
	ComSetCellData(tableName, iRow, 'totalDem', formatToTwoDecimalPlaces(totalDem));
	
	var demPrch = calculateTotalUsdDem(totalDem);
	ComSetCellData(tableName, iRow, 'demPrch', formatToTwoDecimalPlaces(demPrch));
	
	var demSales = 	calculateTotalDem(totalDem);
	ComSetCellData(tableName, iRow, 'demSales', formatToTwoDecimalPlaces(demSales));
	$(tableName).saveGridData();
}

/**
 * 
 */
function calculateDemurrage(returnDate, endOfFT, demRate) {
  	var returnData = "N/A";
  	const returnDt = new Date(returnDate);
  	const endFT = new Date(endOfFT);
  	if (returnDt > endFT) {
   	 	const diffTime = returnDt - endFT; // 밀리초 차이
    	const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // 일 수로 변환
    	return diffDays * demRate;
  	}
  	return returnData;
}

function calculateTotalUsdDem(totalDem) {
  try {
    if (totalDem > 0) {
      return totalDem * 0.9;
    } else {
      return "N/A";
    }
  } catch (error) {
    return "N/A";
  }
}

function calculateTotalDem(totalDem) {
  try {
    if (totalDem > 0) {
      return totalDem * 0.1;
    } else {
      return "N/A";
    }
  } catch (error) {
    return "N/A";
  }
}

/**
 * 수식 ETA or F/T 데이터 변경 시 실행한다.
 * ETA , F/T 데이터 변경 시  ((ETA+F/T)-1,"N/A") = END OF F/T
 */
function endOfFt(etaOrAtadate, ft){
	var returnData = "N/A";
	try{
		if(parseInt(ft) > -1){
			// 현재 날짜를 가져옵니다
			const toDay = new Date(etaOrAtadate);
			// ft 일을 더합니다
			const plusThree = new Date(toDay);
			plusThree.setDate(toDay.getDate() + parseInt(ft));
			const result = new Date(plusThree);
			result.setDate(plusThree.getDate() - 1);
			returnData = result;
		}
	} catch (error) {
		console.log(error);
	}
	if(returnData != 'N/A')
		return formatDateToYYYYMMDD(returnData);
	else
		return returnData;
}


async function excelDown(){
	var date = new Date().toString("yyyymmddhhmm");
	await requestFileDownload('GET', '/api/management/website-terminal-code-exceldown', $('#searchFrom').serializeObject(), 'kclogix-' + date +'.xlsx');
}