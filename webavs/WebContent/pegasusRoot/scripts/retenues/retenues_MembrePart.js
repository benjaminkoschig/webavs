

//function updateLabelType(){
//		$("#labelTypeSection").text($("#typeSection").children("option:selected").text());
//		$("#divTypeSection").show();
//}

//function updateLabelRole(){
//		$("#labelRole").text($("#role").children("option:selected").text());
//		$("#divRole").show();
//		$('#noFacture').attr("disabled", false); 
//}

//function updateLabelRole2(){
//		$("#labelRole2").text($("#role2").children("option:selected").text());
//		$("#divRole2").show();
//}

function RetenuePart(container){	
	
	// variables
	var that=this;
	this.ACTION_AJAX=ACTION_AJAX_SUBSIDE_ANNEE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDataTable");
	this.detail=this.mainContainer.find(".areaDetail");
	
	this.paramId = null;
	this.b_applyStyle = false;

	this.$type = $("#simpleRetenuePayement\\.csTypeRetenue");
	this.$retenueforAdressePaiement = $(".retenuefor" + csadressepaiement);
	this.$retenueforFactureExistante = $(".retenuefor"  + csfactureexistante);
	this.$retenueforFactureFuture = $(".retenuefor" + csfacturefuture);
	this.$retenueSelected = null;
	this.btnAjaxDelete = $(".btnAjaxDelete");
	// functions	
	this.checkDateDebut = false;
	this.afterRetrieve=function(data){		
		this.defaultLoadData(data, "#");
		this.selectedEntityId = data.simpleRetenuePayement.idRetenue;
		var $addressAffichee = $(".adresseAffichee");
		$addressAffichee.children(".adresse").html(data.adressePaiement);
		if(data.simpleRetenuePayement.csTypeRetenue == csadressepaiement){
			if(IS_DOM2R){
				$("#ligneHasAdresse").css("marginBottom","35px");
			} else {
				$("#ligneHasAdresse").css("marginBottom","55px");
			}
		} else {
			$("#ligneHasAdresse").css("marginBottom","0");
		}
		
		this.detail.show();
		if(this.isBeforeDatePaiement(data.simpleRetenuePayement.dateDebutRetenue)){
			this.btnAjaxDelete.hide();
			this.btnAjaxDelete.removeClass("btnAjaxDelete");
		} else {
			this.btnAjaxDelete.addClass("btnAjaxDelete");
			this.btnAjaxDelete.show();
		}
		this.checkDateDebut = false;
//		$addressAffichee.css({
//			 position: "absolute"
//		});
	};
	
	this.getParametres=function($data){
		o_map = ajaxUtils.createMapForSendData(this.detail,"#");
		o_map.idPca = ID_PCA;
		$.extend(o_map,this.getParametresForFind());
		return o_map;
	};	
	
	this.getParametresForFind=function(){
		return {			
			'searchModel.forIdPca':ID_PCA
			};
	};
	
	this.clearFields=function(){
		this.detail.clearInputForm();
		this.detail.find('.adresse').html('');
		this.detail.find('#adressePaiement').attr('defaultvalue','');
		this.detail.find('.adresse').children().remove();
		this.detail.find("#labelRole").html('');
		this.detail.find("#labelTypeSection").html('');
		this.detail.find("#simpleRetenuePayement\\.dateDebutRetenue").val(globazGlobal.DATE_PROCHAIN_PAIMENT);
		this.selectedEntityId = null;
	};
	
	this.getParentViewBean=function(){};
	
	this.setParentViewBean=function(newViewBean){}; 	
	
	this.formatTableTd=function($elementTable){};
	
	this.mainContainer.bind(eventConstant.AJAX_CHANGE,function (){
		$(".descAdresse").attr("disabled",true);
	});
	
	// Formatatage de la table 
	this.formatTable=function(){};
	
	//override la fonction du fichier AbstractScalableAJAXTableZone
	this.showDetail= function (n_idEntity){
		this.beforeShowDetail();
	};
	
	this.isBeforeDatePaiement = function (date) {
		if(date){
			return 	globazNotation.utilsDate.isDateBefore(date, globazGlobal.DATE_PROCHAIN_PAIMENT);
		} else {
			return false;
		}
	};
	
	this.addEvent = function () {
		var that = this;
		this.$type.change(function() {
			that.$retenueforAdressePaiement.hide();
			that.$retenueforFactureExistante.hide();
			that.$retenueforFactureFuture.hide();
			$(".retenuefor"+that.$type.val()).show();
			$("#simpleRetenuePayement\\.montantRetenuMensuel").focus();
			$retenueSelected = that.$type.val();	
		});
		
		var dateTempHack = null ; 
		$("#simpleRetenuePayement\\.dateDebutRetenue").change(function(){
			// hack car la notation execute l'évenement change 
			if (dateTempHack != this.value && this.checkDateDebut) {
				if(that.isBeforeDatePaiement(this.value)){
					globazNotation.utils.consoleError(globazGlobal.messageAvantProchaiementPaiement.replace("&0",this.value).replace("&1",globazGlobal.DATE_PROCHAIN_PAIMENT));
					$(".btnAjaxAdd").attr("disabled",true);
					$(".btnAjaxValidate").attr("disabled",true);
					
				}	else {
					$(".btnAjaxAdd").attr("disabled",false);
					$(".btnAjaxValidate").attr("disabled",false);
				}
				dateTempHack = this.value;
			}
		});
	};
	
	// initialization
	this.init(
	    function(){			    	
			this.addTableEventOnElements(this.$trInTbody);
			this.stopEdition();
			this.list();
	    	this.addEvent();
	    	$(".descAdresse").attr("disabled",true);   	
		}
	);	
}



RetenuePart.prototype=AbstractScalableAJAXTableZone;


$(function(){	
	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new RetenuePart($that);
		this.zone=zone;
		if(globazGlobal.isUpdatable){
			$that.find('.btnAjaxUpdate').click(function(){
				zone.startEdition();
			}).end()
			.find('.btnAjaxCancel').click(function(){
				zone.stopEdition();
				$(".descAdresse").prop("disabled",true);
			}).end()
			.find('.btnAjaxValidate').click(function(){
				zone.validateEdition();
			}).end()
			.find('.btnAjaxDelete').click(function(){
				zone.ajaxDeleteEntity(zone.selectedEntityId);
			}).end()
			.find('.btnAjaxAdd').click(function(){
				zone.startEdition();
				zone.clearFields();
				zone.detail.show();
				zone.checkDateDebut = true;
				$retenueSelected = csadressepaiement;
				$("#simpleRetenuePayement\\.csTypeRetenue").val(csadressepaiement);	
				$("#simpleRetenuePayement\\.csTypeRetenue").change();
				$(".descAdresse").attr("disabled",false);
			}).end();
		} else {
			$that.find(".btnAjax").remove();
		}
	});
});