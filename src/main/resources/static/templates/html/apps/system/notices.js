/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/system/user', {name : $('#name').val()});
	$(tableName).clearGridData();
	$(tableName).searchData(response.data, {editor: true});
}

function tableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['','key','제목','적용날짜','Use','Update User','Update Date'],
	   	colModel: [
			{ name: 'jqFlag', 		width: 50, 		align:'center', hidden : false},
	       	{ name: 'id', 			width: 100, 	align:'center', hidden : false},
	       	{ name: '제목', 			width: 150, 	align:'center'},
	       	{ name: '적용날짜', 		width: 150, 	align:'center'},
	       	{ name: 'Use', 			width: 150, 	align:'center'},
	    	{ name: 'updateUserId', width: 100, 	align:'center'},
	    	{ name: 'updateDate',	width: 140,		align:'center'}
	   	],
		height: 500, 
		width: '100%',
		delselect: true
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

var tableName = '#notices-table';
$( document ).ready(function() {
   tableInit();
   
    let snoticesDate = $('#snoticesDate').data('daterangepicker');
   	snoticesDate.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
	snoticesDate.setEndDate(moment().format('YYYY-MM-DD'));
	snoticesDate.element.trigger('apply.daterangepicker', snoticesDate);
	
	let noticesDate = $('#noticesDate').data('daterangepicker');
   	noticesDate.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
	noticesDate.setEndDate(moment().format('YYYY-MM-DD'));
//	noticesDate.element.trigger('apply.daterangepicker', noticesDate);
	
});