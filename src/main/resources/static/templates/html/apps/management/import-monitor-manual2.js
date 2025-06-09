var tableName = '#port-table';
$( document ).ready(function() {
   	portTableInit();
   	  searchPartnerAutocomplete();
   	  searchCargoAutocomplete();
   	  searchTerminalAutocomplete();
   	  searchCustomerAutocomplete();
});

var partnerList = [];
var carGoList = [];
var terminalList = [];
var customerList = [];
/**
 * 조회
 */
async function search() {
	$(tableName).clearGridData();
	let response = await requestApi('GET', '/api/management/website-terminal-code', {hblNo : $('#shblNo').val(), arrivalNotice : $('#sarrivalNotice').val()});
	$(tableName).searchData(response.data, {editor: true});
	response = null;
}

function portTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','cargo','concine code', 'seq', 'uuid', 'HBL NO.', '매출', '이월 매출', 'A/N&EDI', 'INVOICE', 'CNEE', 'PIC', 'PROFIT DATE', '국내매출', '해외매출', "Q'ty", 'Partner', 'Tank no.', 'Term', 'Name', 'Date', 'Location', 'Vessel / Voyage', 'Carrier', 'MBL NO.', 'POL', 'POD', 'terminalCode', 'Name', 'Link', 'ETD', 'ETA', 'ATA', '비고', 'F/T', 'DEM RATE', 'END OF F/T', 'ESTIMATE RETURN DATE', 'RETURN DATE', 'RETURN DEPOT', 'TOTAL DEM', 'DEM RECEIVED', 'DEM RCVD', 'COMMISSION DEM', 'DEM COMMISSION', 'DEPOT IN DATE(REPO ONLY)', 'REPOSITION 매입'],
	   	colModel: [
	   		{ name: 'jqFlag',				width: 40,		align:'center', 	hidden : false,	frozen:true},
	   		{ name: 'cargo',				width: 100,		align:'center', 	rowspan: true,	editable : true, hidden : true,	frozen:true},
	   		{ name: 'concine', 				width: 70, 		align:'center',		rowspan: true,	editable : true, hidden : true,	frozen:true},
	   		{ name: 'seq', 					width: 50, 		align:'center',		hidden : true,	frozen:true},
	   		{ name: 'uuid', 				width: 50, 		align:'center',		hidden : true,	frozen:true},
	       	{ name: 'hblNo', 				width: 140, 	align:'center',		rowspan: true,	frozen:true},
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
	    	{ name: 'tankNo', 				width: 120, 	align:'center', editable: true},
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
	       	{ name: 'totalDem', 			width: 100, 	align:'center', editable: true},
	       	{ name: 'demReceived', 			width: 80, 		align:'center', editable: true},
	       	{ name: 'demRcvd', 				width: 90, 		align:'center', editable: true},
	       	{ name: 'demPrch', 				width: 100, 	align:'center', editable: true},
	       	{ name: 'demSales', 			width: 100, 	align:'center', editable: true},
	       	{ name: 'depotInDate', 			width: 180, 	align:'center', editable: true, edittype: "date"},
	       	{ name: 'repositionPrch', 		width: 120, 	align:'center', editable: true}
	   	],
		height: 530, 
		width: '100%',
		dblEdit : true,
		frozen: true,
		delselect: true,
//		multiselect: true,
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
}

async function searchPartnerAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/partner/autocomplete');
	if(response.common.status === 'S'){
		partnerList = response.data;
		partnerAutocompleteLoad();
	}
}

async function searchCargoAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/cargo/autocomplete');
	if(response.common.status === 'S'){
		carGoList = response.data;
		itemAutocompleteLoad();
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