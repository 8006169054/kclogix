var tableName = '#partner-table';
$( document ).ready(function() {
   partnerTableInit();
   $('input[type="file"]').change(function() { 
    	upload(this);
	});
});

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/mdm/partner', {name : $('#name').val()});
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: true});
}

function partnerTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','Code','Name','Company','PIC','e-mail', 'import-moniter-role','Code','Update User','Update Date'],
	   	colModel: [
			{ name: 'jqFlag', 				width: 50, 		align:'center', hidden : false},
	       	{ name: 'code', 				width: 100, 	align:'left', hidden : true, editable : false},
	       	{ name: 'name', 				width: 300, 	align:'left', editable : true},
	       	{ name: 'company', 				width: 400, 	align:'left', editable : true},
	       	{ name: 'pic', 					width: 100, 	align:'center', editable : true},
	    	{ name: 'representativeEml',	width: 200, 	align:'center', editable : true},
	    	{ name: 'importMoniterRoleName',width: 300, 	align:'center'},
	    	{ name: 'importMoniterRoleCode',width: 100, 	align:'center', hidden : true, statuscheck: true},
	    	{ name: 'updateUserId', 		width: 100, 	align:'center'},
	    	{ name: 'updateDate',			width: 140,		align:'center'}
	   	],
		height: 500, 
		width: '100%',
		delselect: true,
		//multiselect: true,
		ondblClickRow : function (rowid, iRow, iCol, e){
			if(iCol === 7){
				roleSelect.iRow = iRow;
				roleSelect.importMoniterCode = ComRowData(tableName, iRow).importMoniterRoleCode;
				$('#role').click();
			}
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
	    response = await requestFormDataApi('POST', '/api/mdm/cargo/partner', frm);
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
		await requestApi('POST', '/api/mdm/partner', saveData, {successFn : saveFn, errorFn : saveFn});
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