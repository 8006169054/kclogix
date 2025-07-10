var tableName = '#port-table';
$( document ).ready(function() {
   	portTableInit();
    $('input[type="file"]').change(function() { 
    	upload(this);
	});
	
});

async function upload(customFile) {
	try{
		$(tableName).clearGridData();
		var frm = new FormData();
	    frm.append('upload', customFile.files[0]);
	    response = await requestFormDataApi('POST', '/api/management/excel-upload', frm);
		$(tableName).searchData(response.data, {jqFlag: C});
	}catch (error) {
	}finally {
	  document.getElementById("customFile").value=null;
	}
}

function portTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','uuid', 'HBL NO.',  'Tank no.', '매출', '이월 매출', 'A/N&EDI', 'INVOICE', 'CNEE', 'SHIPMENT STATUS', 'PROFIT DATE', '국내매출', '해외매출', "Q'ty", 'Partner', 'Term', 'ITEM', 'Vessel / Voyage', 'Carrier', 'MBL NO.', 'POL', 'POD', 'TERMINAL', 'ETD', 'ETA', 'ATA', '비고', 'F/T', 'DEM RATE', 'END OF F/T', 'ESTIMATE RETURN DATE', 'RETURN DATE', 'RETURN DEPOT', 'DEM STATUS', 'TOTAL DEM', 'DEM BILLING', 'DEM RCVD', 'COMMISSION DEM', 'DEM COMMISSION', 'DEPOT IN DATE(REPO ONLY)', 'REPOSITION 매입'],
	   	colModel: [
	   		{ name: 'jqFlag',				width: 40,		align:'center', 	hidden : true,	frozen:true},
	   		{ name: 'uuid', 				width: 50, 		align:'center',		hidden : true,	frozen:true},
	       	{ name: 'hblNo', 				width: 140, 	align:'center',		rowspan: false, frozen:true},
	       	{ name: 'tankNo', 				width: 140, 	align:'center',		frozen:true},
	       	{ name: 'sales', 				width: 50, 		align:'center',		rowspan: false,	frozen:false},
	       	{ name: 'carryoverSales', 		width: 50, 		align:'center',		rowspan: false,	frozen:false},
	       	{ name: 'arrivalNotice',		width: 70, 		align:'center',		rowspan: false,	frozen:false},
	       	{ name: 'invoice', 				width: 70, 		align:'center',		rowspan: false,	frozen:false},
	    	{ name: 'concine', 				width: 150, 	align:'center',		rowspan: false,	frozen:false},
	    	{ name: 'shipmentStatus', 		width: 80, 		align:'center',		rowspan: false,	frozen:false},
	    	{ name: 'profitDate', 			width: 90, 		align:'center',		rowspan: false,	frozen:false},
	    	{ name: 'domesticSales', 		width: 80, 		align:'center',		rowspan: false,	frozen:false},
	    	{ name: 'foreignSales', 		width: 80, 		align:'center',		rowspan: false,	frozen:false},
	    	{ name: 'quantity', 			width: 50, 		align:'center',		rowspan: false,	frozen:false},
	    	{ name: 'partner', 				width: 120, 	align:'center',		frozen:false},
	    	{ name: 'term', 				width: 80, 		align:'center',		rowspan: false},
	    	{ name: 'item', 				width: 250, 	align:'center',		rowspan: false},
	    	{ name: 'vesselVoyage', 		width: 200, 	align:'center',		rowspan: false},
	    	{ name: 'carrier', 				width: 80, 		align:'center',		rowspan: false},
	    	{ name: 'mblNo', 				width: 140, 	align:'center',		rowspan: false},
	    	{ name: 'pol', 					width: 100, 	align:'center',		rowspan: false},
	    	{ name: 'pod', 					width: 100, 	align:'center'},
	    	{ name: 'terminal', 			width: 150, 	align:'center'},
	    	{ name: 'etd', 					width: 90, 		align:'center'},
	    	{ name: 'eta', 					width: 90, 		align:'center'},
	       	{ name: 'ata', 					width: 90, 		align:'center'},
	       	{ name: 'remark', 				width: 250, 	align:'center',		rowspan: false},
	       	{ name: 'ft', 					width: 70, 		align:'center'},
	       	{ name: 'demRate', 				width: 80, 		align:'center'},
	       	{ name: 'endOfFt', 				width: 90, 		align:'center'},
	       	{ name: 'estimateReturnDate', 	width: 160, 	align:'center'},
	       	{ name: 'returnDate', 			width: 100, 	align:'center', 	editable: false, edittype: "date"},
	       	{ name: 'returnDepot', 			width: 100, 	align:'center'},
	       	{ name: 'demStatus', 			width: 100, 	align:'center'},
	       	{ name: 'totalDem', 			width: 100, 	align:'center'},
	       	{ name: 'demReceived', 			width: 80, 		align:'center'},
	       	{ name: 'demRcvd', 				width: 90, 		align:'center'},
	       	{ name: 'demPrch', 				width: 100, 	align:'center'},
	       	{ name: 'demSales', 			width: 100, 	align:'center'},
	       	{ name: 'depotInDate', 			width: 180, 	align:'center'},
	       	{ name: 'repositionPrch', 		width: 120, 	align:'center'}
	   	],
		height: 530, 
		width: '100%',
		dblEdit : true,
		frozen: false,
		ondblClickRow : function(rowid, iRow, iCol,	e) {
		}
	});
}

async function fileOpen(){
	$('#customFile').click();
}

async function save(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
		await requestApi('POST', '/api/management/upload-port', saveData);
	}
}