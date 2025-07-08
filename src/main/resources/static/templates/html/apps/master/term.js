var tableName = '#table';
$( document ).ready(function() {
   tableInit();
});

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/mdm/term', $('#searchFrom').serializeObject());
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: true});
}

function tableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','id','Name','Use','Update User','Update Date'],
	   	colModel: [
			{ name: 'jqFlag', 			width: 50, 		align:'center', 	hidden : false},
	       	{ name: 'id', 				width: 100, 	align:'left', 		hidden : true},
	       	{ name: 'name', 			width: 300, 	align:'left', 		editable : true},
	       	{ name: 'use', 				width: 80, 		align:'center', 	hidden : true, editable: true, formatter:'select', edittype:'select', editoptions : {value: 'Y:Y;N:N'}},
	    	{ name: 'updateUserId', 	width: 100, 	align:'center'},
	    	{ name: 'updateDate',		width: 140,		align:'center'}
	   	],
		height: 500, 
		width: '100%',
		delselect: true,
		dblEdit : true
//		afterEditCell: function (rowId, cellName, value, indexRow, indexCol){
//			if(cellName == 'code')
//			console.log(rowId, cellName, value, indexRow, indexCol);
//			
//			 return true;
//		}
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
		await requestApi('POST', '/api/mdm/term', saveData, {successFn : saveFn, errorFn : saveFn});
	}
}

function saveFn(response){
	if(response.common.status === 'S'){
 		search();
 	}
}