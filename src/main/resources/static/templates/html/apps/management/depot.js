var tableName = '#table';
var fileTypeUrl = null;
$( document ).ready(function() {
	searchItemAutocomplete();
	searchPartnerAutocomplete();
   	tableInit();
   	$('input[type="file"]').change(function() { 
    	upload(this);
	});
});

async function upload(file) {
	try{
		var fullJsonData = $(tableName).jqGrid("getRowData");
		var frm = new FormData();
	    frm.append('file', file.files[0]);
	    const jsonBlob = new Blob([JSON.stringify(fullJsonData)], { type: "application/json" });
		frm.append("jsonData", jsonBlob);
	    var response = await requestFormDataApi('POST', fileTypeUrl, frm);
		if(response.common.status === 'S'){
 			$(tableName).clearGridData();
			$(tableName).searchData(response.data, {editor: true, nodatamsg: true});
 		}
	}catch (error) {
		console.log('error', error);
	}finally {
	  document.getElementById("customFile").value=null;
	  fileTypeUrl = null;
	}
}

async function fileOpen(type){
	if(type === 1){
		fileTypeUrl = '/api/management/depot/date-upload';
	}
	else if(type === 2){
		fileTypeUrl = '/api/management/depot/allocation-upload';
	}
	$('#customFile').click();
}

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
			{ name: 'jqFlag', 			width: 30, 		align:'center',		hidden : false, formatter: jqFlagFn},
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

/**  국내매출 */
function jqFlagFn (cellvalue, options, rowObject ){
	return cellvalue === 'U' ? U : cellvalue;
}

async function save(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
		await requestApi('POST', '/api/management/depot', saveData, {successFn : saveFn, errorFn : saveFn});
	}
}

async function confirmSave(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
//		message, type, title, callFn
		var del = 0;
		var up = 0;
		for(var i=0; i<saveData.length; i++){
			if(saveData[i].jqFlag === 'U') up++;
			else if(saveData[i].jqFlag === 'D') del++;
		}
		var msg = 'There are ' + del + ' deletions and ' + up + ' modifications. Save changes?'
		confirmMessage(msg, 'info', 'Save', save);
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
var itemList = [];
var partnerList = [];

async function searchItemAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/cargo/autocomplete');
	if(response.common.status === 'S'){
		itemList = response.data;
		$("#itemName").autocomplete({
			source: function(request, response) {
			    const results = $.ui.autocomplete.filter(itemList, request.term);
			    if (results.length === 0) {
			      results.push({
			        label: "No results found",
			        value: ""
			      });
			    }
			    response(results);
			},
			delay: 100,
			autoFocus: true,
			minLength: 0,
			maxShowItems: 10,
			open: function(){
		        $(this).autocomplete('widget').css('z-index', 1100);
		        return false;
		    },
		    select: function (event, ui) {
		    	$('#item').val(ui.item.code);
		    },
		    close : function (event, ui) {
		        return false;
		    }
		}).focus(function() {
		    $(this).autocomplete("search", $(this).val());
		});
		
	}
}

async function searchPartnerAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/partner/autocomplete');
	if(response.common.status === 'S'){
		partnerList = response.data;
		$("#partner").autocomplete({
			source: function(request, response) {
			    const results = $.ui.autocomplete.filter(partnerList, request.term);
			    if (results.length === 0) {
			      results.push({
			        label: "No results found",
			        value: ""
			      });
			    }
			    response(results);
			},
			delay: 100,
			autoFocus: true,
			minLength: 0,
			maxShowItems: 10,
			open: function(){
		        $(this).autocomplete('widget').css('z-index', 1100);
		        return false;
		    },
		    select: function (event, ui) {
		    },
		    close : function (event, ui) {
		        return false;
		    }
		}).focus(function() {
		    $(this).autocomplete("search", $(this).val());
		});
	}
}