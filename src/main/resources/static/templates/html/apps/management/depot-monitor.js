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
	// 기존 헤더 숨기기 (CSS로 처리)
//	$(".ui-jqgrid-htable thead tr:nth-child(2)").hide();
//	$(".ui-jqgrid-labels jqg-third-row-header thead tr:nth-child(2)").hide();
//	setTimeout(function(){
		$(".ui-jqgrid-htable thead tr:nth-child(3)").hide();
//	},100);

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

//	console.log("홀수 컬럼 합계:", oddSum);
//	console.log("짝수 컬럼 합계:", evenSum);
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