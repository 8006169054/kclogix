var tableName = '#table';
$( document ).ready(function() {
   tableInit();
});

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/management/depot', $('#searchFrom').serializeObject());
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: true, nodatamsg: true});
}

function tableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','uuid', 'seq', 'HBL NO','TANK NO','PARTNER','ITEM', 'DEPOT', 'RETURN DATE', 'CLEANED DATE', 'OUT DATE', 'ALLOCATION', 'REMARK', 'Update User','Update Date'],
	   	colModel: [
			{ name: 'jqFlag', 			width: 30, 		align:'center',		hidden : false},
	       	{ name: 'uuid', 			width: 100, 	align:'center',		hidden : true},
	       	{ name: 'seq', 				width: 150, 	align:'center',		hidden : true},
	       	{ name: 'hblNo', 			width: 140, 	align:'center'},
	       	{ name: 'tankNo', 			width: 120, 	align:'center'},
	       	{ name: 'partner', 			width: 100, 	align:'center'},
	       	{ name: 'item', 			width: 220, 	align:'center'},
	       	{ name: 'returnDepot', 		width: 100, 	align:'center'},
	       	{ name: 'returnDate', 		width: 100, 	align:'center'},
	       	{ name: 'cleanedDate', 		width: 100, 	align:'center', 	editable : true,	edittype: "date"},
	       	{ name: 'outDate', 			width: 100, 	align:'center', 	editable : true,	edittype: "date"},
	       	{ name: 'allocation', 		width: 200, 	align:'center', 	editable : true},
	       	{ name: 'remark', 			width: 350, 	align:'center', 	editable : true},
	    	{ name: 'updateUserId', 	width: 100, 	align:'center'},
	    	{ name: 'updateDate',		width: 140,		align:'center'}
	   	],
		height: 600, 
		width: '100%',
		delselect: true,
		dblEdit : true
	});
}

async function save(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
		await requestApi('POST', '/api/management/depot', saveData, {successFn : saveFn, errorFn : saveFn});
	}
}

function saveFn(response){
	if(response.common.status === 'S'){
 		search();
 	}
}

//////////////////////// 달력 /////////////////////////////
$('#returnDate').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  autoUpdateInput: false,
  drops: 'down',
  opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$('#cleanedDate').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  autoUpdateInput: false,
  drops: 'down',
  opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$('#outDate').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  autoUpdateInput: false,
  drops: 'down',
  opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

//

var partnerList = [];
var carGoList = [];