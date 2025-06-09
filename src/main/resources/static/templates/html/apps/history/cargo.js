var tableName = '#cargo-table';
$( document ).ready(function() {
   cargoTableInit();

});

/**
 * 조회
 */
async function search() {
	var item = $('#name').val();
	if(isEmpty(item)){
		alertMessage(getMessage('0003'), 'error');
	}
	else{
		response = await requestApi('GET', '/api/history/cargo', {name : item});
		$(tableName).clearGridData();
		$(tableName).searchData(response.data);
	}
}

function cargoTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['Date','Item','Location','Depot','CleaningCost','Level','Remark','SubRemark', 'Update User','Update Date'],
	   	colModel: [
	       	{ name: 'cargoDate', 		width: 80, 		align:'center'},
	       	{ name: 'name', 			width: 450, 	align:'left'},
	       	{ name: 'location', 		width: 100, 	align:'center'},
	       	{ name: 'depot', 			width: 80, 		align:'center'},
	    	{ name: 'cleaningCost',		width: 100, 	align:'center'},
	    	{ name: 'difficultLevel',	width: 80, 		align:'center'},
	    	{ name: 'remark1',			width: 500, 	align:'center'},
	    	{ name: 'remark2',			width: 400, 	align:'center'},
	    	{ name: 'updateUserId', 	width: 100, 	align:'center'},
	    	{ name: 'updateDate',		width: 140,		align:'center'}
	   	],
		height: 500, 
		width: '100%'
		//multiselect: true,
//		afterEditCell: function (rowId, cellName, value, indexRow, indexCol){
//			if(cellName == 'code')
//			console.log(rowId, cellName, value, indexRow, indexCol);
//			
//			 return true;
//		}
	});
}
