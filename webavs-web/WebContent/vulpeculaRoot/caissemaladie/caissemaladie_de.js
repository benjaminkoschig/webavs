var GLO = GLO || {};

//fonctions de bases à redéfinir
function add() {
	GLO.suivi.editTable();
}

function upd() {
	GLO.suivi.save();
	GLO.suivi.editTable();
}

function validate() {
	GLO.suivi.save();
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="vulpecula.caissemaladie.caissemaladie.ajouter";
	}
	else
		document.forms[0].elements('userAction').value="vulpecula.caissemaladie.caissemaladie.modifier";
	return true;
}

function cancel() {
	document.forms[0].elements('userAction').value="vulpecula.postetravailvueglobale.travailleurvueglobale.afficher";
	document.forms[0].elements('selectedId').value=globazGlobal.idTravailleur;
	document.forms[0].elements('tab').value="caissesmaladies";
	document.forms[0].elements('_method').value="";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="vulpecula.caissemaladie.caissemaladie.supprimer";
		document.forms[0].submit();
	}
}

function validateFields() {
	var moisDebut = $('#moisDebut').val();
	var moisFin = $('#moisFin').val();
	
	if(!isValidMonthDate(moisDebut)) {
		return false;
	}
	
	if(moisFin.length != 0 && !isValidMonthDate(moisFin)) {
		return false;
	}
	
	return true;
}

function init(){
	$('#idPosteTravail').val(globazGlobal.idPosteTravail);
}

GLO.suivi = {
	options : {
		s_template_selector : "#suiviDocument",
		s_table_selector : "#tblSuivi"
	},
	suiviTable : null,
	init : function() {
		this.suiviTable = vulpeculaTable.create(this.options);
	},
	edit : function() {
		this.suiviTable.editRow();
	},
	editTable : function(){
		$(".btnDeleteParametre").show();
		$("#btnAddParametre").show();
	},
	load : function(data) {
		this.clearFields();
		this.suiviTable.load(data);
	},
	save : function() {
		var s_values = this.suiviTable.saveAsStringWithJSONNonAjax();
		var o_suivi = {
				suiviGSON : s_values
		};
		$("#suiviDocumentString").val(s_values);
		return o_suivi;
	},
	clearFields : function() {
		this.suiviTable.clear();
	},
	addRow : function() {
		this.suiviTable.addRow();
		this.setFocusLastRow();
	},
	setFocusLastRow : function() {
		var lastInput = $('#tblSuivi').find('select').last();
		lastInput.focus();
	}
};

$(function () {
	GLO.suivi.init();
	
	$("#tblSuivi").on("element.loaded", function(event, element, data){
		if(data.envoye == "true"){
			$(element).find(".envoye").attr("checked",data.envoye);
		}else{
			$(element).find(".envoye").removeAttr("checked");
		}
		$(element).find(".dateEnvoi").val(data.dateEnvoi);
	});
	
	GLO.suivi.load(globazGlobal.SUIVI);
	
});

