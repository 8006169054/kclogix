var tableName = '#an-table';

/**
 * 조회
 */
async function search() {
	$(tableName).clearGridData();
	let response = await requestApi('GET', '/api/management/arrivalnotice', $('#searchFrom').serializeObject());
	$(tableName).searchData(response.data, {editor: true});
	response = null;
}

function portTableInit(){
	$(tableName).jqGrid({
	   	datatype: "json",
	   	colNames: ['', 'uuid', 'A/N', 'HBL NO.','CNEE', 'PIC', "e-mail", 'SHIPMENT STATUS', "Q'ty", 'Tank no.', 'Term', 'Name', 'Date', 'Location', 'Vessel / Voyage', 'Carrier', 'MBL NO.', 'POL', 'POD', 'ETD', 'ETA', 'F/T', 'DEM RATE', 'END OF F/T'],
	   	colModel: [
			{ name: 'jqFlag',				width: 40,		align:'center', 	hidden : true,  frozen:true},
	   		{ name: 'uuid', 				width: 50, 		align:'center',		hidden : true, 	frozen:true},
	       	{ name: 'arrivalNotice',		width: 70, 		align:'center',		rowspan: true,	frozen:true, formatter: arrivalNoticeFn},
	       	{ name: 'hblNo', 				width: 140, 	align:'center',		rowspan: true,	frozen:true},
	       	{ name: 'concineName', 			width: 140, 	align:'center',		rowspan: true,	frozen:true},
	    	{ name: 'concinePic', 			width: 100,		align:'center',		rowspan: true, editable: true},
	    	{ name: 'concineEmail', 		width: 300,		align:'center',		rowspan: true, editable: true},
	    	{ name: 'shipmentStatus', 		width: 80, 		align:'center',		rowspan: true, formatter:'select', edittype:'select', editoptions : {value: 'Y:IN PROGRES;N:CLOSED'}},
	    	{ name: 'quantity', 			width: 50, 		align:'center',		rowspan: true},
	    	{ name: 'tankNo', 				width: 150, 	align:'center'},
	    	{ name: 'term', 				width: 80, 		align:'center',		rowspan: true},
	    	{ name: 'item',					width: 250, 	align:'center',		rowspan: true},
			{ name: 'cargoDate', 			width: 80, 		align:'center',		rowspan: true},
			{ name: 'location', 			width: 100, 	align:'center',		rowspan: true},
	    	{ name: 'vesselVoyage', 		width: 200, 	align:'center',		rowspan: true},
	    	{ name: 'carrier', 				width: 80, 		align:'center',		rowspan: true},
	    	{ name: 'mblNo', 				width: 140, 	align:'center',		rowspan: true},
	    	{ name: 'pol', 					width: 100, 	align:'center',		rowspan: true},
	    	{ name: 'pod', 					width: 100, 	align:'center'},
	    	{ name: 'etd', 					width: 90, 		align:'center'},
	    	{ name: 'eta', 					width: 90, 		align:'center'},
	       	{ name: 'ft', 					width: 70, 		align:'center'},
	       	{ name: 'demRate', 				width: 80, 		align:'center'},
	       	{ name: 'endOfFt', 				width: 90, 		align:'center'}
	   	],
		height: 530, 
		width: '100%',
		dblEdit : true,
		frozen: true
//		multiselect : true, // 그리드 왼쪽부분에 체크 박스가 생겨 다중선택이 가능해진다.
// 		multiboxonly : true // 다중선택을 단일 선택으로 제한
	});
	
	$(tableName).jqGrid('setGroupHeaders', {
				useColSpanStyle: true,
				groupHeaders: [
                                {startColumnName:'item', numberOfColumns: 3, titleText: 'Item' }
                                
                              ]
		});
}

function arrivalNoticeFn (cellvalue, options, rowObject ){
	if(emptyChange(rowObject.arrivalNotice) === '')
		return '<input type="radio" name="anRadio" id="anRadio" value="' + options.rowId + '" />';
	else
		return rowObject.arrivalNotice;
}

async function searchCargoAutocomplete(){
	var response = await requestApi('GET', '/api/mdm/cargo/autocomplete');
	if(response.common.status === 'S'){
		carGoList = response.data;
		$("#sitem").autocomplete({
			source: carGoList,
			delay: 100,
			autoFocus: true,
			minChars: 0,
			minLength: 0,
			open: function(){
		        $(this).autocomplete('widget').css('z-index', 1100);
		        return false;
		    },
		    select: function (event, ui) {
		    	$('#scargo').val(ui.item.code);
		    },
		    close : function (event, ui) {
		        return false;
		    }
		}).focus(function() {
		    $(this).autocomplete("search", $(this).val());
		});
	}
}

$('#seta').daterangepicker({
  locale: {format: 'YYYY-MM-DD'},
  autoUpdateInput: false,
  drops: 'down',
  opens: 'right'
}).on('apply.daterangepicker', function (ev, picker) {
  $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
});

$( document ).ready(function() {
   	portTableInit();
   	searchCargoAutocomplete();
   	
   	$('#anFile').on('change', function () {
    	let fileNames = Array.from(this.files).map(file => file.name).join(', ');
   		 $(this).next('.custom-file-label').html(fileNames);
  	});
  
//   	let seta = $('#seta').data('daterangepicker');
//   	seta.setStartDate(moment().subtract(30, 'days').format('YYYY-MM-DD'));
//	seta.setEndDate(moment().format('YYYY-MM-DD'));
//	seta.element.trigger('apply.daterangepicker', seta);
});


var dropzone = new Dropzone("#mydropzone", {
  url: "#",
  addRemoveLinks: true
});

async function anSend(type){
	var rowId = $("input:radio[name=anRadio]:checked").val();
	var rowData = ComRowData(tableName, rowId);
	if(isEmpty(rowData.concinePic))
		alertMessage(getMessage('0004'), 'error');
	else if(isEmpty(rowData.concineEmail))
		alertMessage(getMessage('0005'), 'error');
	else if(type === 'T'){
		requestFileDownload('POST', '/api/management/arrival-notice-send-mail-template', rowData, 'ArrivalNoticeTemplate_' + rowData.hblNo + '.eml');
	}
	else if(type === 'M'){
		const formData = new FormData();
		// 파일 추가
		dropzone.files.forEach(file => {
		 	formData.append("files", file); // "files"는 서버에서 받을 필드명
		});
		const jsonBlob = new Blob([JSON.stringify(rowData)], { type: "application/json" });
		formData.append("jsonData", jsonBlob);
		
		let response = await requestFormDataApi('POST', '/api/management/arrival-notice-send-mail', formData);
		if(response.common.status === 'S'){
			alertMessage(getMessage('0006'), 'success');
			$('#fileUploadModal').modal('hide');
			dropzone.removeAllFiles(true);
	 		search();
	 	}
	}
}

var minSteps = 6,
  maxSteps = 60,
  timeBetweenSteps = 100,
  bytesPerStep = 100000;
  
dropzone.uploadFiles = function(files) {
  var self = this;

  for (var i = 0; i < files.length; i++) {

    var file = files[i];
      totalSteps = Math.round(Math.min(maxSteps, Math.max(minSteps, file.size / bytesPerStep)));

    for (var step = 0; step < totalSteps; step++) {
      var duration = timeBetweenSteps * (step + 1);
      setTimeout(function(file, totalSteps, step) {
        return function() {
          file.upload = {
            progress: 100 * (step + 1) / totalSteps,
            total: file.size,
            bytesSent: (step + 1) * file.size / totalSteps
          };

          self.emit('uploadprogress', file, file.upload.progress, file.upload.bytesSent);
          if (file.upload.progress == 100) {
            file.status = Dropzone.SUCCESS;
            self.emit("success", file, 'success', null);
            self.emit("complete", file);
            self.processQueue();
          }
        };
      }(file, totalSteps, step), duration);
    }
  }
}

function fileupload(){
	$('#fileUploadModal').modal('show');
}