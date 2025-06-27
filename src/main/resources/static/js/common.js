loding = async (type) => {
	if(type)
		$(".loading-mask").css('display','');
	else
		$(".loading-mask").css('display','none');
};

/**
 * 달러포맷 변경
 */
function usMoneyConversion(start, cellvalue, end){
	if(cellvalue.indexOf('.') > 0){
		var cellvalues = cellvalue.split(".");
		return start + cellvalues[0] + '.' + cellvalues[1].toString().padStart(2, '0') + end;
	}
	else {
		return start + cellvalue + '.' + ''.toString().padStart(2, '0') + end;
	}
}

/**
 * type = success , info, warning, error
 */
function alertMessage(message, type){
//	if(title === undefined){
//		if(type === 'success') title = 'Success';
//		else if(type === 'info') title = 'Info';
//		else if(type === 'warning') title = 'Warning';
//		else if(type === 'error') title = 'Error';
//	}
	swal('', message, type);
}

/**
 * type = success , info, warning, error
 */
confirmMessage = async (message, type, title, callFn) => {
	swal({
      title: title,
      text: message,
      icon: type,
      buttons: true,
      dangerMode: true,
    })
    .then((selection) => {
      if (selection)
      	callFn(selection);
      else
      	callFn(false);
    });
};

dataBinding = async (bindingData, prefix = "") => {
	var keys = Object.keys(bindingData);
	$.each(keys, function(index, key) {
		$("#" + prefix + key).val(bindingData[key]);
	});
};

clearFormValues = async (bindingData, prefix = "") => {
	var keys = Object.keys(bindingData);
	$.each(keys, function(index, key) {
		$("#" + prefix + key).val("");
	});
};

/**
 * 
 */
setCookie = (name, value) => {
	document.cookie = name + "=" + escape( value ) + "; path=/;";
}
    
/**
 * 
 */
getCookie = (name) => {
	var Found = false; 
	var start, end; 
	var i = 0;

	while(i <= document.cookie.length) { 
		start = i; 
		end = start + name.length; 
		    
		if(document.cookie.substring(start, end) == name) { 
		   Found = true; 
		   break; 
		} 
		i++; 
	} 
		    
	if(Found == true) { 
		start = end + 1; 
		end = document.cookie.indexOf(";", start); 
		if(end < start) 
			end = document.cookie.length; 
		return document.cookie.substring(start, end); 
	} 
	return "";	  
}

/**
 * select Box 생성
 */
async function ComSelectBox(url, options){
	let response = await requestApi('GET', url, options.param);
	var select = document.getElementById(options.id);
	$.each(response.data, function(index, data) {
		var option = document.createElement('option');
		option.value = data.value;
		option.text = data.text;
		select.appendChild(option);
	});
	$(select).selectric('refresh');
}

function isEmpty(str){
    return typeof str === 'undefined' || str === null || str === '' || str === 'null';
}

function emptyChange(str){
	if(isEmpty(str)) return '';
	let reg = /[→\r\n]/gim;
	return str.replace(reg, "");
	
//	return str;
}

function isTrue(bool){
	if(bool === undefined) return false;
	else if(bool) return true;
	else return false;
}

function limitLength(elem, maxLength) {
  if (elem.value.length > maxLength) {
    elem.value = elem.value.slice(0, maxLength);
  }
}

$( document ).ready(function() {
	$(".dateFormat").keyup(function(){
		$(this).val($(this).val().replace(/[^0-9]/gi, "").replace(/^(\d{4})(\d{2})(\d{2})$/, `$1-$2-$3`));
	});
	$(".numberFormat").keyup(function(){
		$(this).val($(this).val().replace(/[^0-9]/gi, ""));
	});
});

/** 
 * 현재 날짜
*/
function toDate() {
  	const today = new Date();
	const thirtyDaysAgo = new Date();
	return thirtyDaysAgo.toISOString().slice(0, 10);
}