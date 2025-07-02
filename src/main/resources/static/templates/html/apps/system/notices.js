var tableName = '#jqtable';
/**
 * 조회
 */
async function search() {
	clearSaveBox();
	response = await requestApi('GET', '/api/system/notices', $('#searchFrom').serializeObject());
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: false, nodatamsg: true});
}

async function save(){
	var saveFrom = $('#saveFrom').serializeObject()
	// 벨리데이션
	var response = await requestApi('POST', '/api/system/notices', saveFrom);
	if(response.common.status === 'S'){
		// 데이터 초기화 하고 조회
 		search();
 	}
}

function clearSaveBox(){
	$('#title, #noticesDate, #id').val('');
	$('#contentBody').summernote('code', '');
	$('#use').val('Y').trigger('change');
}
		
function tableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['', 'key','제목','적용날짜','Use','Update User','Update Date'],
	   	colModel: [
			{ name: 'contentBody', 			width: 100, 	align:'center', hidden : true},
	       	{ name: 'id', 			width: 100, 	align:'center', hidden : true},
	       	{ name: 'title', 			width: 350, 	align:'left'},
	       	{ name: 'noticesDate', 		width: 150, 	align:'center'},
	       	{ name: 'use', 			width: 70, 	align:'center'},
	    	{ name: 'updateUserId', width: 100, 	align:'center'},
	    	{ name: 'updateDate',	width: 140,		align:'center'}
	   	],
		height: 550, 
		width: '100%',
		ondblClickRow: function(rowid, iRow, iCol) {
		    const rowData = $(tableName).jqGrid("getRowData", rowid);
		    $('#id').val(rowData.id);
		    $('#title').val(rowData.title);
		    $('#noticesDate').val(rowData.noticesDate);
		    $('#use').val(rowData.use).trigger('change');
		    $('#contentBody').summernote('code', rowData.contentBody);
		  }
	});
}

$('#snoticesDate').daterangepicker({
  	locale: {format: 'YYYY-MM-DD'},
	autoUpdateInput: false,
  	drops: 'down',
  	opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$('#noticesDate').daterangepicker({
  	locale: {format: 'YYYY-MM-DD'},
	autoUpdateInput: false,
  	drops: 'down',
  	opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$( document ).ready(function() {
   tableInit();
	
});