
var globalViewBean = {};
function DeblocageAjax(m_options) {
	this.ACTION_AJAX = globazGlobal.ACTION_AJAX;
	this.mainContainer = m_options.$mainContainer ;
	this.selectedEntityId = m_options.n_idEntity;
	this.tempateCreancier = '';
	
	this.onRetrieve = function (data) {
		var that = this;
		this.defaultLoadData(data, m_options.s_selector); 
	};
	
	this.getParametersForLoad = function () {
		return {};
	};

	this.getParametres = function () {
		
		var param = ajaxUtils.createMapForSendData(this.mainContainer,'.');
		var map = {};
		for(var key in param){
			map["simpleDeblocage."+key] = param[key];
		}
		
		
		if(param.idAvoirPaiementUnique === ''){
			map["idAvoirPaiementUnique"] = param['avoirPaiement.idAdrPmtIntUnique'];
		}else{
			map["idAvoirPaiementUnique"] = param.idAvoirPaiementUnique;
		}
			
		
		map["simpleDeblocage.csTypeDeblocage"] = this.mainContainer.closest(".titre").attr("id").split("_")[1];
		map["simpleDeblocage.idPca"] = globazGlobal.idPca ;
		map["simpleDeblocage.idApplicationAdressePaiement"] = this.mainContainer.find("avoirPaiement\\.idApplication").val();
		map[m_options.s_entityIdPath] = this.selectedEntityId;
		return map;
	};
	
	this.stopEdition = function () {
		//this.disabeldEnableForm(true);
		//this.mainContainer.find('input:button').hide().filter('.btnAjaxUpdate, .btnAjaxDelete').show();
		var $adresse = 	this.mainContainer.find(".adresse");
		
		if ($adresse.length) { 
			this.mainContainer.clearInputForm();
			this.currentViewBean = null;
			this.selectedEntityId = null;
		}
		
		$adresse.empty();
		this.mainContainer.removeClass(this.modifiedZoneClass);
		this.isModifyingEntity = false;
		//this.mainContainer.triggerHandler(eventConstant.AJAX_STOP_EDITION);
		this.triggerEvent();
	};
	
	this.onUpdateCrancier = function (data) {
		 var obj = {}; 
		 var $html =null;
		 obj.montant = data.simpleDeblocage.montant;
		 obj.refPaiement =  data.simpleDeblocage.refPaiement;
		 obj.compte =  data.adressePaiement.banque.compte;
		 obj.idDeBlocage = data.simpleDeblocage.idDeblocage;
		 obj.banqueDesignation1 =  data.adressePaiement.banque.designation1;
		 obj.banqueDesignation2 =  data.adressePaiement.banque.designation2;
		 obj.rue = data.adressePaiement.banque.rue;
		 obj.numero = data.adressePaiement.banque.numero;
		 obj.localite = data.adressePaiement.banque.localite;
		 obj.npa = data.adressePaiement.banque.npa;
		 obj.designationTiers1 =  data.adressePaiement.tiers.designation1;
		 obj.designationTiers2 =  data.adressePaiement.tiers.designation2;

		 obj.tiersCreancierDesignation1 =  data.designationTiers1;
		 obj.tiersCreancierDesignation2 =  data.designationTiers2;		 
		 
		 $html =  globazNotation.template.compile$(obj, this.tempateCreancier);
		 newAjaxDeblocage($html);
		 $("#creanciers").append($html);
	};
	
	this.onUpdate = function (data, action) {

		var t_entityIdPath = m_options.s_entityIdPath.split(".");
		var tempData = data;
		var count = 0;
		for (var i in data) {
		    if (data.hasOwnProperty(i)) {
		        count++;
		    }
		}
		if(data && count > 2){
			for (var i=0; t_entityIdPath.length > i; i++) {
				tempData = tempData[t_entityIdPath[i]];
			}
			this.t_element = this.defaultLoadData(data, m_options.s_selector); 
			this.selectedEntityId = tempData;
		} else {
			this.selectedEntityId = null;
			this.currentViewBean = null;
		}
	
		this.startEditionWithoutHiddingButton();
		
		
		
		if(data && data.simpleDeblocage && "64070001" === data.simpleDeblocage.csTypeDeblocage && action == 'add'){
			this.onUpdateCrancier(data);
		}

		//gestion du bouton enregistr? lors de la sauvgarde cr?ancier
		if("64070001" === data.simpleDeblocage.csTypeDeblocage && action == 'add'){
			this.mainContainer.find(".save").find(".ui-icon").css("background-image", "");
		}else{
			this.mainContainer.find(".save").find(".ui-icon").css("background-image", "url(theme/jquery/images/ui-icons_green_256x240.png)");
		}
		
		this.mainContainer.css("color","");
	};

	this.getParentViewBean = function () {
		return globalViewBean;
	};

	this.setParentViewBean = function (newViewBean) {
		globalViewBean = newViewBean;
	};
	
	this.onDelete = function (data){
		this.mainContainer.remove();
	};

	// Hack temporaire, voir probleme AbstractSimpleAJAXDetailZone (wiki)
	this.startEditionWithoutHiddingButton = function () {
		this.disabeldEnableForm(false);
		//this.mainContainer.find('input:button').hide().filter('.btnAjaxValidate,.btnAjaxCancel').show();
		this.isModifyingEntity = true;
		ajaxUtils.addFocusOnFirstElement(this.mainContainer);
		this.mainContainer.addClass(this.modifiedZoneClass);
		this.triggerEvent()
	}
	
	
	// initialization
	this.init(function () {
		var that = this;
	
		this.mainContainer.find(".liveSum").keyup(function () {
			that.mainContainer.find(".save").find(".ui-icon").css("background-image", "url(theme/jquery/images/ui-icons_f9bd01_256x240.png)");
			that.mainContainer.css("color","#29769F");
		});
		this.tempateCreancier = $("#templateCreancier").html();
		this.mainContainer.find(".save").button({
			icons: {
				primary: 'ui-icon-disk'
				},
				text: false,
				disabled: false
		}).click(function (){
			that.validateEdition();
		});
		
		var mainContainer = this.mainContainer;
		
		this.mainContainer.find(".del").button({
			icons: {
				primary: 'ui-icon-trash'
				},
				text: false,
				disabled: false
		}).click(function (){
			that.ajaxDeleteEntity();
			//mainContainer.html('');
		});
	});
}


var readAdressePaiement = {
	b_temporise: true,
	init: function () {
		this.addEvent();
//			$('html').bind(eventConstant.NOTATION_MANAGER_DONE, function () {
//				if ($.trim($('#idTiers').val()) == '') {
//					$('.adresse').html('');
//				}
//			});
	},
	
	addEvent: function () {
		var that = this;
		$('.idTiersCreancier').change(function () {
			if ($.trim(this.value) != '' && that.b_temporise) {
				that.b_temporise = false;
				that.readAdresse(this.value, this);
				setTimeout(function () {
					that.b_temporise = true;
				}, 200);
			}
		});
		
		
	},

	displayAdresse: function (data,element) {
	
		if(data && data.adresseFormate) {
			var $container = $(element).closest('.areaDetail');
			var html = data.adresseFormate.replace(/[\r\n]/g, '<br />');
			$container.find(".adresse").html(html);
			$container.find(".idAvoirPaiementUnique").val(data.fields.id_avoir_paiement_unique);
		}
	},

	readAdresse: function (idTiers, element) {
		
		var that = this;
		var options = {
			serviceClassName: 'ch.globaz.pyxis.business.service.AdresseService',
			serviceMethodName: 'getAdressePaiementTiers',
			parametres: idTiers + ",true," + globazGlobal.CS_DOMAINE_APPLICATION_RENTE + "," + globazNotation.utilsDate.toDayFromated() + ",0",
			callBack: function (data){
				that.displayAdresse(data,element); 
			}
		};
		globazNotation.readwidget.options = options;
		globazNotation.readwidget.read();
	}

};

DeblocageAjax.prototype = AbstractSimpleAJAXDetailZone;


var t_zone = [];
function newAjaxDeblocage($element) {
	var options = {};
	options.$mainContainer = $element;
	options.s_entityIdPath = "simpleDeblocage.idDeblocage";
	options.n_idEntity = $element.attr("idEntity") ;
	var zone = new DeblocageAjax(options);
	//zone.stopEdition();
	t_zone.push(zone);
}

var LANGUAGES = 'T,A';

liveSum = {
	$resultLiveSum: null,
	montantBlocage: null,
	$validerButton: null,
	
	init: function ($validerButton) {
		this.$validerButton = $validerButton;
		this.$resultLiveSum = $("#resultLiveSum");
		this.montantBlocage = globazGlobal.montantBlocage;
		this.bindEvent();
		this.sumAndChangeValue();
	},
	
	sum: function () {
		var sum = 0, val;
		$(".liveSum, .amountToSum").each(function () {
			val = 0;
			if (this.value) {
				val = this.value;
			} else if(this.innerHTML) {
				val = this.innerHTML;
			}
			sum = sum +  globazNotation.utilsFormatter.amountTofloat(val);
		});
		return sum;
	},
	
	sumAndChangeValue: function () {
		var sum, solde;
		sum = this.sum();
		solde =  globazNotation.utilsFormatter.amountTofloat(this.montantBlocage)-sum;
		if(solde < 0){
			this.$validerButton.button("disable");
			this.$resultLiveSum.addClass("errorSum");
		} else {
			this.$validerButton.button("enable");
			this.$resultLiveSum.removeClass("errorSum");
		}
		
		$("#montantLiberer").text( globazNotation.utilsFormatter.formatStringToAmout(sum));
		this.$resultLiveSum.text( globazNotation.utilsFormatter.formatStringToAmout(solde));
	},
	
	bindEvent: function () {
		var that = this;
		$("#detailDeblocage").on("keyup",'.liveSum', function () {
			that.sumAndChangeValue();
		});	
	}
};


function addAnim() {
	var s_src = './images/loader_horizontal.gif';

	var $img =	$('<img />', {
			'src': s_src,
			'class': 'loading_horizonzal'
		}).css({
			'z-index': 1000,
			'opacity': 1,
			'text-align': 'center'
		});
	
};

validationDevalidation = {
		
		$validerButton: null,
		$deValiderLiberation: null,
		$detailDeblocage: null,
		$imgWait: null,
		isDevalidable: false,
		
		init: function (isDevalidable) {
			this.isDevalidable = isDevalidable;
			this.$detailDeblocage = $("#detailDeblocage");
			this.addButton();
			this.addEvent();
		},
		
		addButton: function () {
			this.$validerButton =  $("#ValiderLiberation").button({});
			if(this.isDevalidable){
				this.$deValiderLiberation = $("#DeValiderLiberation").button({});
			}
		},
		
		disableButton: function () {
			if(!this.$validerButton) {
				this.addButton();
			}
			this.$validerButton.button("disable");
			if(this.$deValiderLiberation) {
				this.$deValiderLiberation.button("disable");
			}
		},
		
		enableButton: function () {
			this.$validerButton.button("enable");
			if(!this.$validerButton) {
				this.$deValiderLiberation.button("enable");
			}
		},
		
		addWait: function () {
			this.$detailDeblocage.overlay({b_relatif:true});
			var s_src = './images/loader_horizontal.gif';
			var $img =	$('<img />', {
					'src': s_src,
					'class': 'loading_horizonzal'
				}).css({
					'z-index': 1000,
					'opacity': 1,
					'text-align': 'center',
					top: '0',
					left: '45%',
					position: 'absolute'
				});
			this.$detailDeblocage.append($img);
			this.$imgWait= $img;
		},
		
		removeWait: function () {
			this.$detailDeblocage.removeOverlay();
			this.$imgWait.remove();
		},
		
		submit: function (s_action) {
	
			var that = this;
			this.disableButton();
			this.addWait();
			params = {
					idPca: globazGlobal.idPca,
					action: s_action
				};
			
			ajaxUtils.ajaxCustom( globazGlobal.ACTION_AJAX+".ajouterAJAX", params, function (data) {
				that.enableButton();
				that.removeWait();
				//window.location.reload(true); 
				window.location = window.location.href;
			}, function () { 
				that.enableButton();
				that.removeWait();
			});
		},
		
		addEvent: function () {
			var that = this;
			this.$validerButton.click(function () {
				that.submit(globazGlobal.paramActionLiberer);
			});

			if(this.$deValiderLiberation) {
				this.$deValiderLiberation.click(function () {
					that.submit(globazGlobal.paramActionDeLiberer);
				});
			}
		}
};


$(function () {
	validationDevalidation.init(globazGlobal.isDevalidable);

	
	$('.avoirPaiement.idTiers').change(function () {
		that.readAdresse(this.value, this);
	});
	
	if(globazGlobal.isUpdatable) {
		readAdressePaiement.init();
		$(".areaDetail").each(function () {
			newAjaxDeblocage($(this));
		});
	
		liveSum.init(validationDevalidation.$validerButton);
		
	} else {
		//validationDevalidation.disableButton();
		setTimeout(function () {
			$('html').triggerHandler(eventConstant.AJAX_INIT_DONE);
		},300);
	}
});
