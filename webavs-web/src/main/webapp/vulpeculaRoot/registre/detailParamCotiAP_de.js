var cotisationSearch;
var typeListeMONTANT = [68031001,68031002,68031005];
var typeListeFACTEUR = [68031008];


$(function () {
	$(".btnAjaxValidate").click(function (event) {
//		forcer l'id de la coti parente comme étant celle de la coti rootEntity
		$('#currentEntity\\.parametreCotisationAssociationSimpleModel\\.idCotisationAssociationProfessionnelle').val($('#searchModel\\.forIdCotisationAssociationProfessionnelle').val());
		if(!validate()){
			event.stopImmediatePropagation();
		}
	});
	
	$(".btnAjaxAdd").click(function () {
		if(contains(typeListeMONTANT,$('#currentEntity\\.parametreCotisationAssociationSimpleModel\\.typeParam').val())){
			isMontant();
		} else if(contains(typeListeFACTEUR, $('#currentEntity\\.parametreCotisationAssociationSimpleModel\\.typeParam').val())){
			isFacteur();
		} else{
			isTaux();
		}
	});
	
	$(".areaSearch :input").change(function() {
			cotisationSearch.ajaxFind();
	});
	
	$('#currentEntity\\.parametreCotisationAssociationSimpleModel\\.typeParam').change(function() {
		if(contains(typeListeMONTANT,$('#currentEntity\\.parametreCotisationAssociationSimpleModel\\.typeParam').val())){
			isMontant();
		}else if(contains(typeListeFACTEUR, $('#currentEntity\\.parametreCotisationAssociationSimpleModel\\.typeParam').val())){
			isFacteur();
		} else {
			isTaux();
		}
	});
	
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity.parametreCotisationAssociationSimpleModel",

		init: function () {
			cotisationSearch = this;
			this.capage(30, [10, 20, 30, 50, 100]);
			this.addSearch();
			$("select[name=currentEntity\\.parametreCotisationAssociationSimpleModel\\.typeParam] option[value='68031007']").remove();
		},

		clearFields: function () {
			this.defaultClearFields();
			$('.spyFormatted').html('');
		},
		
		afterRetrieve : function (data) {
			this.defaultLoadData(data, '#');
			$('#nomCotisation').val(data.currentEntity.cotisationAssociationProfessionnelleSimpleModel.libelle + ' | ' + data.currentEntity.administrationComplexModel.tiers.designation1 + " " + data.currentEntity.administrationComplexModel.tiers.designation1);
		}
	});
});

function validate() {
	return true;
}

function isTaux(){
	document.getElementById('trMontant').disabled='disabled';
	document.getElementById('trFacteur').disabled='disabled';
	document.getElementById('trTaux').disabled='';
}

function isMontant(){
	document.getElementById('trMontant').disabled='';
	document.getElementById('trTaux').disabled='disabled';
	document.getElementById('trFacteur').disabled='disabled';
}

function isFacteur(){
	document.getElementById('trMontant').disabled='disabled';
	document.getElementById('trTaux').disabled='disabled';
	document.getElementById('trFacteur').disabled='';
}

function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] == obj) {
            return true;
        }
    }
    return false;
}