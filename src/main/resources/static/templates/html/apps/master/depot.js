var tableName = '#table';
$( document ).ready(function() {
   tableInit();
});

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/mdm/depot', $('#searchFrom').serializeObject());
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: true, nodatamsg: true});
}

function tableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','DEPOT CODE','LOCATION','DEPOT NAME(EN)','DEPOT NAME(KOR)', 'ADDRESS', 'PIC', 'TEL', 'E-MAIL', 'OPERATION REMARK', 'Update User','Update Date'],
	   	colModel: [
			{ name: 'jqFlag', 			width: 30, 		align:'center'},
	       	{ name: 'depotCode', 		width: 100, 	align:'center', 	editable : true, editoptions : {pk:true}},
	       	{ name: 'location', 		width: 150, 	align:'center', 	editable : true},
	       	{ name: 'depotNameEn', 		width: 200, 		align:'center', 	editable : true},
	       	{ name: 'depotNameKr', 		width: 200, 		align:'center', 	editable : true},
	       	{ name: 'address', 			width: 300, 		align:'center', 	editable : true},
	       	{ name: 'picName', 			width: 100, 		align:'center', 	editable : true},
	       	{ name: 'picTel', 			width: 100, 		align:'center', 	editable : true},
	       	{ name: 'picEmail', 		width: 120, 		align:'center', 	editable : true},
	       	{ name: 'operationRemark', 	width: 300, 		align:'center', 	editable : true},
	    	{ name: 'updateUserId', 	width: 100, 	align:'center'},
	    	{ name: 'updateDate',		width: 140,		align:'center'}
	   	],
		height: 600, 
		width: '100%',
		delselect: true,
		dblEdit : true
	});
}

async function add(){
	$(tableName).addRow();
}

async function save(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
		await requestApi('POST', '/api/mdm/depot', saveData, {successFn : saveFn, errorFn : saveFn});
	}
}

function saveFn(response){
	if(response.common.status === 'S'){
 		search();
 	}
}