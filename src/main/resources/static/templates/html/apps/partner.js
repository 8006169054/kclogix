var tableName = '#port-table';
var hiddenCelVal = [];
$( document ).ready(function() {
	gridColsearch();
});

async function gridColsearch() {
	let response = await requestApi('GET', '/api/partner/home/website-terminal-code-grid-col');
	if(response.data != ''){
		let loopCol = [];
		if(response.data.indexOf(",") > 0){
			loopCol = response.data.split(",");
		}else{
			loopCol.push(response.data);
		}
		for(var i=0; i < loopCol.length; i++){
			hiddenCelVal[loopCol[i]] = true;
		}
	}
	loopCol = null;
	response = null;
	portTableInit();
}

/**
 * 조회
 */
async function search() {
	$(tableName).clearGridData();
	let response = await requestApi('GET', '/api/partner/home/website-terminal-code', {hblNo : $('#hblNo').val()});
	$(tableName).searchData(response.data);
	response = null;
}

function portTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['', 'HBL NO.', "Q'ty", 'Partner', 'Tank no.', 'Term', 'Item', 'Vessel / Voyage', 'Carrier', 'MBL NO.', 'POL', 'POD', 'ETD', 'ETA', 'F/T', 'DEM RATE', 'END OF F/T', 'RETURN DATE', 'RETURN DEPOT', 'TOTAL DEM', 'COMMISSION DEM', 'DEM COMMISSION', 'DEPOT IN DATE(REPO ONLY)'],
	   	colModel: [
	   		{ name: 'jqFlag',				width: 40,		align:'center', 	hidden : true,	frozen:true},
	       	{ name: 'hblNo', 				width: 140, 	align:'center',		rowspan: true,	frozen:true},
	    	{ name: 'quantity', 			width: 50, 		align:'center',		rowspan: true},
	    	{ name: 'partner',				width: 100, 	align:'center'},
	    	{ name: 'tankNo', 				width: 120, 	align:'center'},
	    	{ name: 'term', 				width: 80, 		align:'center',		rowspan: true},
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
		height: 530, 
		width: '100%',
		frozen: true
	});
	
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
	await requestFileDownload('GET', '/api/partner/home/website-terminal-code-exceldown', {hblNo : $('#hblNo').val()}, 'kclogix-' + date +'.xlsx');
}
