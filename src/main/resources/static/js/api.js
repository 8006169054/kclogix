requestFileDownload = async (method, url, params, fileName) => {
	let token = getToken();
	let headers = {
		'Content-Type': method?.match(/(POST|PUT|PATCH)/) ? 'application/json' : 'text/plain'
		,'Authorization': token === null ? '' : 'Bearer ' + token
	};
	let body = (method || '').match(/(POST|PUT|PATCH)/) && params ? JSON.stringify(params) : null;

	if (method.match(/GET/) && (params != null && params != undefined)) {
		url = `${url}?${new URLSearchParams(params)}`;
	}

	loding(true);
	return await fetch(`${url}`, {
		method: method,
		mode: 'cors',
		headers: headers,
		body: body,
		cache: 'no-cache',
		credentials: 'include'
	})
		.then(function(response) {
			status = response.status;
			switch (response.status) {
				case 200:
					return response.blob();

				case 204:
					return undefined;

				default:
					return response.blob();
			}
		})
		.then((blob) => {
			if (status >= 200 && status < 300) {
				const blobURL = window.URL.createObjectURL(blob);
				const tempLink = document.createElement('a');
				tempLink.href = blobURL;
				tempLink.download = fileName;
				document.body.appendChild(tempLink);
				tempLink.click();
				tempLink.remove();
			}
		})
		.catch(function(e) {
			console.log(e);
			throw Error('excel download error');
		})
		.finally(function() { loding(false); });
};

/**
 * 파일 업로드
 */
requestFormDataApi = async (method, url, formData, option) => {
	let responseHeaders = {};
	let responseHeaderJSON = {};
	let token = getToken();
	let headers = {
		'Authorization': token === null ? '' : 'Bearer ' + token
	};
	loding(true);
	return await fetch(`${url}`, {
		mode: 'cors',
		headers: headers,
		cache: 'no-cache',
		credentials: 'include',
		method: method,
		enctype: 'multipart/form-data', // * 중요 *
		body: formData
	})
	.then(function(response) {
		responseStatus = response.status;
        responseHeaders = response.headers;
        for (let pair of responseHeaders.entries()) {
			responseHeaderJSON[pair[0]] = pair[1];
		}
		setToken(responseHeaderJSON);
		switch (response.status) {
			case 200:
				return response.json();

			case 204:
				return undefined;

			case 403:
//				$.removeCookie('kainos', { path: '/view' });
				//setkainosLang(null);
				location.replace("/login/authlogin");
				return undefined;

			default:
				return response;
		}
	})
	.then(function(response) {
		switch (!!(responseStatus >= 200 && responseStatus < 300)) {
			case true:
				if(response.common.status === 'E' && response.common.message != undefined )
					swal('', response.common.message, 'error').then((selection) => {
				      if (selection)
				      	if(option.errorFn !== undefined)
				      		option.errorFn(response);
				    });				
				else if(response.common.status === 'S' && response.common.message != undefined )
					swal('', response.common.message, 'success').then((selection) => {
				      if (selection)
				      	if(option.successFn !== undefined)
				      		option.successFn(response);
				    });				
				else
					return response;

			default:
				return response;
		}
	})
	.catch(function(error) {
		console.log('error : ', error);
		//      oLoader(false);
		//      Swal.fire("", Error(`${error}`).message, "error");
//		throw Error(`${error}`).message;
	})
	.finally(function() { loding(false); });
};


/** Server API 호출 기능 */
requestApi = async (method, url, params, option) => {
	let responseHeaders = {};
	let responseHeaderJSON = {};
	let token = getToken();
	let headers = {
		'Content-Type': method?.match(/(POST|PUT|PATCH|DELETE)/) ? 'application/json' : 'text/plain'
		,'Authorization': token === null ? '' : 'Bearer ' + token
	};
	let body = (method || '').match(/(POST|PUT|PATCH|DELETE)/) && params ? JSON.stringify(params) : null;

	if (method.match(/GET/) && (params != null && params != undefined)) {
		url = `${url}?${new URLSearchParams(params)}`;
	}
	loding(true);
	return await fetch(`${url}`, {
		method: method,
		mode: 'cors',
		headers: headers,
		body: body,
		cache: 'no-cache',
		credentials: 'include'
	})
		.then(function(response) {
			responseStatus = response.status;
			responseHeaders = response.headers;
			for (let pair of responseHeaders.entries()) {
				responseHeaderJSON[pair[0]] = pair[1];
			}
			setToken(responseHeaderJSON);
			switch (response.status) {
				case 200:
					return response.json();

				case 204:
					return undefined;

				case 403:
//					$.removeCookie('kainos', { path: '/view' });
//					setkainosLang(null);
					location.replace("/view/login/authlogin");
					return undefined;

				default:
					return response;
			}
		})
		.then(function(response) {
			switch (!!(responseStatus >= 200 && responseStatus < 300)) {
				case true:
					if(response.common.status === 'E' && response.common.message != undefined )
						swal('', response.common.message, 'error').then((selection) => {
					      if (selection)
					      	if(option !== undefined && option.errorFn !== undefined)
					      		option.errorFn(response);
					    });				
					else if(response.common.status === 'S' && response.common.message != undefined )
						swal('', response.common.message, 'success').then((selection) => {
					      if (selection)
					      	if(option !== undefined && option.successFn !== undefined)
					      		option.successFn(response);
					    });				
					else
						return response;

				default:
					return response;
			}
		})
		.catch(function(error) {
			console.log('error : ', error);
			//      oLoader(false);
			//      Swal.fire("", Error(`${error}`).message, "error");
//			throw Error(`${error}`).message;
		})
		.finally(function() { loding(false); });
};

window.storage = window.sessionStorage || (function() {
	// window.sStorage = (function() {
	var winObj = opener || window;  //opener가 있으면 팝업창으로 열렸으므로 부모 창을 사용
	var data = JSON.parse(winObj.top.name || '{}');
	var fn = {
		length: Object.keys(data).length,
		setItem: function(key, value) {
			data[key] = value + '';
			winObj.top.name = JSON.stringify(data);
			fn.length++;
		},
		getItem: function(key) {
			return data[key] || null;
		},
		key: function(idx) {
			return Object.keys(data)[idx] || null;  //Object.keys() 는 IE9 이상을 지원하므로 IE8 이하 브라우저 환경에선 수정되어야함
		},
		removeItem: function(key) {
			delete data[key];
			winObj.top.name = JSON.stringify(data);
			fn.length--;
		},
		clear: function() {
			winObj.top.name = '{}';
			fn.length = 0;
		}
	};
	return fn;
})();

async function setkainosLang(lang){
//	localStorage.setItem("kainos-lang", lang);
	sessionStorage.setItem("kainos-lang", lang);
	await requestApi('GET', '/open/lang', {'lang' : lang});
	location.reload();
}

function getkainosLang(){
//	var kainoslang = sessionStorage.getItem("kainos-lang") === null ? localStorage.getItem("kainos-lang") : sessionStorage.getItem("kainos-lang");
	var kainoslang = sessionStorage.getItem("kainos-lang");
	return isEmpty(kainoslang) ? 'en' : kainoslang;
}

function setToken(responseHeaderJSON){
	if(responseHeaderJSON !== undefined && responseHeaderJSON.authorization !== undefined){
		const token = responseHeaderJSON.authorization.split('Bearer ')[1];
		sessionStorage.setItem("kainos", token);
//		localStorage.setItem("kainos", token);
//		$.cookie('kainos', token);
	}
}

function getToken(){
//	return sessionStorage.getItem("kainos") === null ? localStorage.getItem("kainos") : sessionStorage.getItem("kainos");
	return sessionStorage.getItem("kainos");
}

/** ================================================================================ */
//priceFormatter = (price) => {
//	return price.toLocaleString();
//};
//
//priceFormatterRemove = (price) => {
//	return price.replace(/,/g, "");
//};
//
//deleteFormatter = () => {
//	return '<i class="bx bx-trash" style="cursor: pointer;"></i>';
//};
//
//shareFormatter = () => {
//	return '<div class="Icon_icon_select__tLN6i"><div class="Icon_icon__I3Lry Icon_added__UaQEK"><i class="bx bx-share" style="cursor: pointer;"></i></div><div class="Icon_icon_name__XWqpO"></div></div>';
//};
//
//oLoader = (showOnInit) => {
//	if(showOnInit)
//		$('#oloader_canvas_0_1').show();
//	else
//		$('#oloader_canvas_0_1').hide();
//};
//
//addDate = (addDay) => {
//	let d = new Date();
//	let sel_day = addDay; //일자를 조절하시면 됩니다. -1이면 하루전/ +1이면 내일
//	d.setDate(d.getDate() + sel_day );
//	let year    = d.getFullYear();
//	let month   = ('0' + (d.getMonth() +  1 )).slice(-2);
//	let day     = ('0' + d.getDate()).slice(-2);
//	dt = year+"-"+month+"-"+day;
//	return dt;
//};