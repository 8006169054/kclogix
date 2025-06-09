var tableName = '#cargo-table';
$( document ).ready(function() {
   cargoTableInit();
   	$('input[type="file"]').change(function() { 
    	upload(this);
	});
});

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/mdm/cargo', {name : $('#name').val()});
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: true});
}

function cargoTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','Code','Date','Item','Location','Depot','CleaningCost','Level','Remark','SubRemark', 'Update User','Update Date'],
	   	colModel: [
			{ name: 'jqFlag', 			width: 50, 		align:'center', hidden : false},
	       	{ name: 'code', 			width: 100, 	align:'left', 	hidden : true, editable : false},
	       	{ name: 'cargoDate', 		width: 80, 		align:'center', editable : true, editoptions : {pk:true}, edittype: "date"},
	       	{ name: 'name', 			width: 450, 	align:'left', 	editable : true, editoptions : {pk:true}},
	       	{ name: 'location', 		width: 100, 	align:'center', editable : true, editoptions : {pk:true}},
	       	{ name: 'depot', 			width: 80, 		align:'center', editable : true},
	    	{ name: 'cleaningCost',		width: 100, 	align:'center', editable : true},
	    	{ name: 'difficultLevel',	width: 80, 		align:'center', editable : true},
	    	{ name: 'remark1',			width: 500, 	align:'center', editable : true},
	    	{ name: 'remark2',			width: 400, 	align:'center', editable : true},
	    	{ name: 'updateUserId', 	width: 100, 	align:'center'},
	    	{ name: 'updateDate',		width: 140,		align:'center'}
	   	],
		height: 500, 
		width: '100%',
		delselect: true,
		//multiselect: true,
		dblEdit : true
//		afterEditCell: function (rowId, cellName, value, indexRow, indexCol){
//			if(cellName == 'code')
//			console.log(rowId, cellName, value, indexRow, indexCol);
//			
//			 return true;
//		}
	});
}

async function upload(customFile) {
	try{
		var frm = new FormData();
	    frm.append('upload', customFile.files[0]);
	    response = await requestFormDataApi('POST', '/api/mdm/cargo/upload', frm);
	   	if(response.common.status === 'S'){
 			search();
 		}
	}catch (error) {
	}finally {
	  document.getElementById("customFile").value=null;
	}
}

async function add(){
	$(tableName).addRow();
}

async function save(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
		await requestApi('POST', '/api/mdm/cargo', saveData, {successFn : saveFn, errorFn : saveFn});
	}
}

function saveFn(response){
	if(response.common.status === 'S'){
 		search();
 	}
}

async function fileOpen(){
	$('#customFile').click();
}