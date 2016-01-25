function add () {
}

function upd() {
}

function validate() {
}

function cancel() {
}

function del() {
}

function init(){
}

$(function () {
	$('.compteurSearch').click(function (){
		var elementToAppend = $(this).closest('tr');
		var idCompteur = $(elementToAppend).attr('data-compteurId');
		if(!globazGlobal.isFound(this)) {
			globazGlobal.findAndDisplayLignesCompteur(idCompteur, elementToAppend);
			globazGlobal.setFound(this);
		} else {
			if(globazGlobal.isVisible(elementToAppend)) {
				globazGlobal.hideLignes(elementToAppend);
			} else {
				globazGlobal.showLignes(elementToAppend);
			}
		}
	});
});

globazGlobal.EXPAND = 'images/icon-expand.gif';
globazGlobal.COLLAPSE = 'images/icon-collapse.gif';

globazGlobal.findAndDisplayLignesCompteur = function(idCompteur, element) {
	var that = this;
	$.ajax({
		data: {idCompteur:idCompteur,
			  userAction:"vulpecula.congepaye.compteursAjax.afficherAJAX"
			},
		success: function (data) {
			that.displayLignesCompteur(data, element);
		}
	});
};

globazGlobal.displayLignesCompteur = function(data, element) {
	this.changeToCollapseIcon(element);
	$(data).insertAfter($(element));
};

globazGlobal.isFound = function(element) {
	return $(element).attr('data-found');
};

globazGlobal.setFound = function(element) {
	$(element).attr('data-found','found');
};

globazGlobal.hideLignes = function(element) {
	this.changeToExpandIcon(element);
	$(this.getElementsLignesForCompteur(element)).hide();
};

globazGlobal.showLignes = function(element) {
	this.changeToCollapseIcon(element);
	$(this.getElementsLignesForCompteur(element)).show();
};

globazGlobal.isVisible = function(element) {
	return $(this.getElementsLignesForCompteur(element)).is(':visible');
};

globazGlobal.changeToCollapseIcon = function(element) {
	$(element).find('img').attr('src',this.COLLAPSE);
};

globazGlobal.changeToExpandIcon = function(element) {
	$(element).find('img').attr('src',this.EXPAND);
};

globazGlobal.getElementsLignesForCompteur = function(element) {
	return $(element).nextUntil('.compteur');
};