var tableName = '#customer-table';
$( document ).ready(function() {
   customerTableInit();
   $('input[type="file"]').change(function() { 
    	upload(this);
	});
});

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/mdm/customer', {name : $('#name').val()});
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: true});
}

function customerTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','Code','Name','Pic', 'E-mail', 'Tel','Update User','Update Date'],
	   	colModel: [
			{ name: 'jqFlag', 			width: 50, 		align:'center', hidden : false},
	       	{ name: 'code', 			width: 120, 	align:'center', editable : true, editoptions : {pk:true}},
	       	{ name: 'name', 			width: 300, 	align:'left', 	editable : true},
	       	{ name: 'pic', 				width: 100, 	align:'left', editable : true},
	    	{ name: 'email',			width: 200, 	align:'left', editable : true},
	    	{ name: 'tel',				width: 300, 	align:'left'},
	    	{ name: 'updateUserId', 	width: 100, 	align:'center'},
	    	{ name: 'updateDate',		width: 140,		align:'center'}
	   	],
		height: 500, 
		width: '100%',
		delselect: true,
		//multiselect: true,
		ondblClickRow : function (rowid, iRow, iCol, e){
		},
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
	    response = await requestFormDataApi('POST', '/api/mdm/customer/upload', frm);
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
		await requestApi('POST', '/api/mdm/customer', saveData, {successFn : saveFn, errorFn : saveFn});
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