<!--hide this script from non-javascript-enabled browsers
// Fonctions pour les actions sur boutons
var savedFindOnClick = 'undefined';
var savedNewOnClick = 'undefined';
var savedExportOnClick = 'undefined';

function disableBtn(aBtn) {
	if (aBtn != null) {
		aBtn.onclick = '';
		//aBtn.style.display = 'none';
		aBtn.disabled = true;
	}
}

function onClickNew() {
	disableBtn(document.getElementById('btnNew'));
	var oBtnFind = document.getElementById('btnFind');
	if (oBtnFind != null) {
		disableBtn(oBtnFind);
	}
	var oBtnExport = document.getElementById('btnExport');
	if (oBtnExport != null) {
		disableBtn(oBtnExport);
	}
}

function onClickFind() {
	disableBtn(document.getElementById('btnFind'));
	var oBtnNew = document.getElementById('btnNew');
	if (oBtnNew != null) {
		disableBtn(oBtnNew);
	}
	var oBtnExport = document.getElementById('btnExport');
	if (oBtnExport != null) {
		disableBtn(oBtnExport);
	}
}

function onClickExport() {
	disableBtn(document.getElementById('btnExport'));
	var oBtnNew = document.getElementById('btnNew');
	if (oBtnNew != null) {
		disableBtn(oBtnNew);
	}
	var oBtnFind = document.getElementById('btnFind');
	if (oBtnFind != null) {
		disableBtn(oBtnFind);
	}

}

function showButtons() {
	var oBtnNew = document.getElementById('btnNew');
	if (oBtnNew != null) {
		oBtnNew.onclick = savedNewOnClick;
		//oBtnNew.style.display = 'inline';
		oBtnNew.disabled = false;
	}

	var oBtnFind = document.getElementById('btnFind');
	if (oBtnFind != null) {
		oBtnFind.onclick = savedFindOnClick;
		//oBtnFind.style.display = 'inline';
		oBtnFind.disabled = false;
	}

	var oBtnExport = document.getElementById('btnExport');
	if (oBtnExport != null) {
		oBtnExport.onclick = savedExportOnClick;
		//oBtnFind.style.display = 'inline';
		oBtnExport.disabled = false;
	}
}

function showWaitingPopup() {
	if (timeWaiting != -1 && timeWaitingId == -1)
		timeWaitingId = setTimeout("if (document.getElementById('fr_list').readyState!='complete') document.getElementById('waitingPopup').style.visibility='visible';",timeWaiting*1000);		
	return true;
}

function fnStartInit() {		
	if (document.getElementById("fr_list").readyState=="complete") {
		showButtons();
		document.getElementById("waitingPopup").style.visibility="hidden";
		timeWaitingId = -1;
	}
}
// stop hiding -->