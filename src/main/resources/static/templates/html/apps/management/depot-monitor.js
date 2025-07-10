var tableName = '#table';
$( document ).ready(function() {
   	tableInit();
});

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/management/depotmonitor', $('#searchFrom').serializeObject());
	$(tableName).clearGridData();
	$(tableName).searchData(response.data);
}

function tableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['LOCATION', 'BUSAN NEW PORT', 'BUSAN OLD PORT', 'YANGSAN', 'YANGSAN', 'YANGSAN', 'YANGSAN', 'KWANGYANG', 'YEOSU', 'DANGJIN', 'ULSAN', 'TOTAL'],
	   	colModel: [
	       	{ name: 'location', 		width: 100, 	align:'center'},
	       	{ name: 'busanNewPort', 	width: 150, 	align:'center'},
	       	{ name: 'busanOldPort', 	width: 140, 	align:'center'},
	       	{ name: 'yangsan1', 		width: 120, 	align:'center'},
	       	{ name: 'yangsan2', 		width: 100, 	align:'center'},
	       	{ name: 'yangsan3', 		width: 220, 	align:'center'},
	       	{ name: 'yangsan4', 		width: 100, 	align:'center'},
	       	{ name: 'kwangyang', 		width: 100, 	align:'center'},
	       	{ name: 'yeosu', 			width: 100, 	align:'center'},
	       	{ name: 'dangjin', 			width: 100, 	align:'center'},
	       	{ name: 'ulsan', 			width: 200, 	align:'center'},
	       	{ name: 'total', 			width: 350, 	align:'center'}
	   	],
		height: 600, 
		width: '100%'
	});
}