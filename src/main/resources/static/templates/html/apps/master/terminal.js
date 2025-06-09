var tableName = '#terminal-table';
$( document ).ready(function() {
   tableInit();

	$('input[type="file"]').change(function() { 
    	upload(this);
	});
	
	search();
});

function tableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','지역','타입','코드명','터미널명','장치장코드','Url','Link','생성자','생성날짜','수정자','수정날짜'],
	   	colModel: [
	   		{ name: 'jqFlag',			width: 40,		align:'center'},
	   		{ name: 'region', 			width: 100, 	align:'left', editable: true},
	       	{ name: 'type', 			width: 100, 	align:'center', editable: true},
	       	{ name: 'code', 			width: 100, 	align:'center', editable: true, editoptions : {pk:true}},
	       	{ name: 'name', 			width: 250, 	align:'left', editable: true},
	       	{ name: 'parkingLotCode', 	width: 80, 		align:'center', editable: true},
	    	{ name: 'homepage', 		width: 540, 	align:'left', editable: true},
	    	{ name: 'popupicon', 		width: 60, 		align:'center', formatter: terminalFn},
	    	{ name: 'createUserId', 	width: 80, 		align:'center'},
	    	{ name: 'createDate', 		width: 80, 		align:'center'},
	    	{ name: 'updateUserId', 	width: 80, 		align:'center'},
	    	{ name: 'updateDate', 		width: 80, 		align:'center'}
	    	
	   	],
		height: 530, 
		width: '100%',
		dblEdit : true,
		delselect: true,
//		multiselect: true,
		ondblClickRow : function(rowid, iRow, iCol,	e) {
//			Object.assign(portData, ComRowData(this.id, iRow));
//			$('#add').click();
//			console.log('ondblClickRow', ComMultiSelectRow(tableName));
		}
	});
	
	$(tableName).jqGrid('setGroupHeaders', {
				useColSpanStyle: true,
				groupHeaders: [
                                {startColumnName:'homepage', numberOfColumns: 2, titleText: 'Homepage' },
                              ]
		});
}

async function upload(customFile) {
	try{
		var frm = new FormData();
	    frm.append('upload', customFile.files[0]);
	    response = await requestFormDataApi('POST', '/api/mdm/terminal/upload', frm);
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

function terminalFn (cellvalue, options, rowObject ){
	if(rowObject.homepage === '' || rowObject.homepage === undefined)
		return '';
	else
		return '<a href="' + rowObject.homepage + '" target="_blank"><img src="/assets/img/popup.png" height="22px"></a>';
}




/**
 * 조회
 */
async function search() {
	$(tableName).clearGridData();
	response = await requestApi('GET', '/api/mdm/terminal', {name : $('#name').val()});
	$(tableName).searchData(response.data, {editor: true});
}

async function save(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
		await requestApi('POST', '/api/mdm/terminal', saveData, {successFn : portSaveFn, errorFn : portSaveFn});
	}
}

function portSaveFn(response){
	if(response.common.status === 'S'){
 		search();
 	}
}

async function fileOpen(){
	$('#customFile').click();
}