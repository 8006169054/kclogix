var tableName = '#port-table';
var hiddenCelVal = [];
var gridHeight = 600;

$( document ).ready(function() {
	gridColsearch();
	searchPartnerAutocomplete();
   	searchCargoAutocomplete();
});

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

async function gridColsearch() {
	let response = await requestApi('GET', '/api/partner/home/website-terminal-code-grid-col');
	if(response.data != undefined){
		if(response.data.indexOf(",") > 0){
			hiddenCelVal = response.data.split(",");
		}else{
			hiddenCelVal.push(response.data);
		}
	}
	response = null;
	portTableInit();
}

/**
 * 조회
 */
async function search() {	
	$(tableName).clearGridData();
	let response = await requestApi('GET', '/api/partner/home/website-terminal-code', $('#searchFrom').serializeObject());
	$(tableName).searchData(response.data, {nodatamsg: true});
	response = null;
}

function portTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['', 'HBL NO.', "Q'ty", 'Partner', 'Tank no.', 'Term', 'SHIPMENT STATUS', 'PROFIT DATE', 'Item', 'Vessel / Voyage', 'Carrier', 'MBL NO.', 'POL', 'POD', 'ETD', 'ETA', 'F/T', 'DEM RATE', 'END OF F/T', 'RETURN DATE', 'RETURN DEPOT', 'TOTAL DEM', 'COMMISSION DEM', 'DEM COMMISSION', 'DEPOT IN DATE(REPO ONLY)'],
	   	colModel: [
	   		{ name: 'jqFlag',				width: 40,		align:'center', 	hidden : true,	frozen:true},
	       	{ name: 'hblNo', 				width: 140, 	align:'center',		rowspan: true,	frozen:true},
	    	{ name: 'quantity', 			width: 50, 		align:'center',		rowspan: true},
	    	{ name: 'partner',				width: 100, 	align:'center'},
	    	{ name: 'tankNo', 				width: 120, 	align:'center'},
	    	{ name: 'term', 				width: 80, 		align:'center',		rowspan: true},
	    	{ name: 'shipmentStatus', 		width: 80, 		align:'center',		rowspan: true, formatter:'select', edittype:'select', editoptions : {value: 'Y:IN PROGRES;N:CLOSED'}},
	    	{ name: 'profitDate', 			width: 90, 		align:'center',		rowspan: true},
	    	{ name: 'item',					width: 220, 	align:'center', 	rowspan: true},
	    	{ name: 'vesselVoyage', 		width: 200, 	align:'center',		rowspan: true},
	    	{ name: 'carrier', 				width: 80, 		align:'center',		rowspan: true},
	    	{ name: 'mblNo', 				width: 140, 	align:'center',		rowspan: true},
	    	{ name: 'pol', 					width: 100, 	align:'center',		rowspan: true},
	    	{ name: 'pod', 					width: 100, 	align:'center'},
	    	{ name: 'etd', 					width: 90, 		align:'center'},
	    	{ name: 'eta', 					width: 90, 		align:'center'},
	       	{ name: 'ft', 					width: 70, 		align:'center'},
	       	{ name: 'demRate', 				width: 80, 		align:'center'},
	       	{ name: 'endOfFt', 				width: 90, 		align:'center'},
	       	{ name: 'returnDate', 			width: 100, 	align:'center'},
	       	{ name: 'returnDepot', 			width: 100, 	align:'center'},
	       	{ name: 'totalDem', 			width: 100, 	align:'center', formatter: totalDemFn},
	       	{ name: 'demPrch', 				width: 100, 	align:'center', formatter: demPrchFn},
	       	{ name: 'demSales', 			width: 100, 	align:'center', formatter: demSalesFn},
	       	{ name: 'depotInDate', 			width: 180, 	align:'center'}
	   	],
		height: gridHeight, 
		width: '100%',
		frozen: true
	});
	
	for(var i=0; i < hiddenCelVal.length; i++){
		$(tableName).hideCol(hiddenCelVal[i]);
	}
	$(tableName).refreshFrozen();
	
}

/**  TOTAL DEM */
function totalDemFn (cellvalue, options, rowObject ){
	if(cellvalue !== 'N/A' && cellvalue !== ''){
		return usMoneyConversion('US$', cellvalue, '');
	} 
	return cellvalue;
}

/**  DEM(USD)-매입 */
function demPrchFn (cellvalue, options, rowObject ){
	if(cellvalue !== 'N/A' && cellvalue !== ''){
		return usMoneyConversion('US$', cellvalue, '');
	} 
	return cellvalue;
}

/**  DEM 매출 */
function demSalesFn (cellvalue, options, rowObject ){
	if(cellvalue !== 'N/A' && cellvalue !== ''){
		return usMoneyConversion('US$', cellvalue, '');
	} 
	return cellvalue;
}

async function excelDown(){
	var date = new Date().toString("yyyymmddhhmm");
	await requestFileDownload('GET', '/api/partner/home/website-terminal-code-exceldown', $('#searchFrom').serializeObject(), 'kclogix-' + date +'.xlsx');
}

async function searchPartnerAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/partner/autocomplete');
	if(response.common.status === 'S'){
		$("#spartner").autocomplete({
			source: response.data,
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
		$("#sitem").autocomplete({
		source: response.data,
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

function clearSearchBox(){
	$('#searchFrom')[0].reset();
	$('#sshipmentStatus').val('Y').trigger('change');
	$('#sdemRcvdSelect').val('0').trigger('change');
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