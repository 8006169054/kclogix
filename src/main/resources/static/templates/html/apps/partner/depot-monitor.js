var tableName = '#table';
var hideCol = [];
$( document ).ready(function() {
   	tableInit();
   	
	document.querySelectorAll('.dropdown-item').forEach(function(item) {
  		item.addEventListener('click', function(e) {
    		e.preventDefault(); // 링크 이동 방지
   		 	const action = this.getAttribute('data-action');
    		noDataShowAndHidden(action);
  		});
	});
	
});

/**
 * 조회
 */
async function search() {
	response = await requestApi('GET', '/api/partner/management/depotmonitor', $('#searchFrom').serializeObject());
	$(tableName).clearGridData();
	$(tableName).searchData(response.data);
	
	/** 2번째 행 머지 */
	colGroup();

	/** 1번쨰 행 합계 */
	totalToTotal();
	
	dataTotal();
}

function tableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: colNamesList,
	   	colModel: colModelList,
		height: 600, 
		width: '100%'
	});
	
	$(tableName).jqGrid('setGroupHeaders', {
	  useColSpanStyle: true,
	  groupHeaders: groupHeaderList
	});
	$(".ui-jqgrid-htable thead tr:nth-child(3)").hide();

}

function totalToTotal(){
	const $row = $("tr#1"); // ID가 1인 행 선택
	const $tds = $row.find("td");
	
	let oddSum = 0;
	let evenSum = 0;

	$tds.each(function(index) {
	  const value = $(this).text().trim();
	  const num = parseFloat(value.replace(/,/g, '')); // 쉼표 제거 후 숫자 변환
	  if (!isNaN(num)) {
	    if ((index + 1) % 2 === 1) {
	      oddSum += num; // 홀수 컬럼 (1, 3, 5, ...)
	    } else {
	      evenSum += num; // 짝수 컬럼 (2, 4, 6, ...)
	    }
	  }
	});

	const oddSumIndex = $tds.length - 3;
	const evenSumIndex = $tds.length - 2;
	
	$(tableName).jqGrid("setCell", 1, oddSumIndex, oddSum);
	$(tableName).jqGrid("setCell", 1, evenSumIndex, evenSum);

}

function dataTotal(){
	$(tableName + " tr:gt(3)").each(function(rowIndex) {
		const $tds = $(this).find("td");
		let oddSum = 0;
		let evenSum = 0;
  		$tds.each(function(index) {
    		const value = $(this).text().trim();
    		const num = parseFloat(value.replace(/,/g, ''));

    		if (!isNaN(num)) {
      			if ((index + 1) % 2 === 1) {
        			oddSum += num; // 홀수 번째 컬럼
      			} else {
        			evenSum += num; // 짝수 번째 컬럼
      			}
    		}
  		});
  		const oddSumIndex = $tds.length - 3;
		const evenSumIndex = $tds.length - 2;
	
		$(tableName).jqGrid("setCell", rowIndex+4, oddSumIndex, oddSum);
		$(tableName).jqGrid("setCell", rowIndex+4, evenSumIndex, evenSum);
	});
}


function colGroup(){
	const $tr = $("tr#2"); // ID가 2인 tr 선택
	const $tds = $tr.find("td");
	
	let i = 0;
	while (i < $tds.length) {
	  if (i === 0) {
	    i++; // 첫 번째 td는 그대로 유지
	    continue;
	  }
	
	  const $td1 = $tds.eq(i);
	  const $td2 = $tds.eq(i + 1);
	
	  if ($td2.length) {
	    $td1.attr("colspan", 2);
	    $td1.text($td1.text()); // 내용 병합 (선택사항)
	    $td2.remove(); // 다음 td 제거
	  }
	  i += 2; // 다음 묶음으로 이동
	}
}

function noDataShowAndHidden(event){
	if(event === 'show'){
		$('#dropdownMenuButton').html('NoData(Show)');
		$(tableName).jqGrid("showCol", hideCol);
		hideCol = [];
	}
	else if(event === 'hidden'){
		$('#dropdownMenuButton').html('NoData(Hidden)');
		var rowData = $(tableName).jqGrid('getRowData', 1);
		for (var key in rowData) {
  			if (rowData[key] === '0') {
    			// 해당 컬럼을 숨김 처리
    			hideCol.push(parseInt(key));
  			}
		}
		$(tableName).jqGrid("hideCol", hideCol);
	}
	$(tableName).jqGrid('setGroupHeaders', {
		useColSpanStyle: true,
		groupHeaders: groupHeaderList
	});
	$(".ui-jqgrid-htable thead tr:nth-child(3)").hide();
}