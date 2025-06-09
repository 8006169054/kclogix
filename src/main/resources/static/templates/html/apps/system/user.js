var tableName = '#user-table';
$( document ).ready(function() {
   userTableInit();
   searchPartnerAutocomplete();
});

var partnerList = [];

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/system/user', {name : $('#name').val()});
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: true});
}

function userTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','ID','Password','Name','e-Mail','Use','Type','Partner', 'Update User','Update Date'],
	   	colModel: [
			{ name: 'jqFlag', 			width: 50, 		align:'center', hidden : false},
	       	{ name: 'id', 				width: 100, 	align:'center', editable : true, editoptions : {pk:true}},
	       	{ name: 'password', 		width: 150, 	align:'center', editable : true},
	       	{ name: 'name', 			width: 150, 	align:'center', editable : true},
	       	{ name: 'mail', 			width: 150, 	align:'center', editable : true},
	    	{ name: 'activation',		width: 80, 		align:'center', editable : true, formatter:'select', edittype:'select', editoptions : {value: 'Y:Y;N:N'}},
	    	{ name: 'type',				width: 100, 	align:'center', editable : true, formatter:'select', edittype:'select', editoptions : {value: 'M:관리자;P:파트너'}},
	    	{ name: 'partnerCode',		width: 300, 	align:'center', editable : true, edittype: 'text', editoptions: {
				dataInit:function(elem) {
					$(elem).autocomplete({
						source: partnerList,
						delay: 100,
						autoFocus: true,
						minChars: 0,
						minLength: 0,
				        select: function (event, ui) {
				        },
				        close : function (event, ui) {
				            $(tableName).delay(2000).focus();
				            return false;
				        }
					}).focus(function() {
			            $(this).autocomplete("search", $(this).val());
			        });
				}
			}},
	    	{ name: 'updateUserId', 	width: 100, 	align:'center'},
	    	{ name: 'updateDate',		width: 140,		align:'center'}
	   	],
		height: 500, 
		width: '100%',
		delselect: true,
//		multiselect : true, // 그리드 왼쪽부분에 체크 박스가 생겨 다중선택이 가능해진다.
// 		multiboxonly : true, // 다중선택을 단일 선택으로 제한
		dblEdit : true
//		afterEditCell: function (rowId, cellName, value, indexRow, indexCol){
//			if(cellName == 'code')
//			console.log(rowId, cellName, value, indexRow, indexCol);
//			
//			 return true;
//		}
	});
}

function activationFn(cellvalue, options, rowObject ){
	if(cellvalue === 'Y') return '사용';
	else return '미사용';
}

function typeFn(cellvalue, options, rowObject ){
	if(cellvalue === 'M') return '관리자';
	else return '파트너';
}

async function add(){
	$(tableName).addRow();
}

async function save(){
	var saveData = $(tableName).saveGridData();
	if(saveData.length === 0)
		alertMessage(getMessage('0001'), 'info');
	else{
		await requestApi('POST', '/api/system/user', saveData, {successFn : saveFn, errorFn : saveFn});
	}
}

async function searchPartnerAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/partner/autocomplete');
	if(response.common.status === 'S'){
		partnerList = response.data;
	}
}

function saveFn(response){
	if(response.common.status === 'S'){
 		search();
 	}
}