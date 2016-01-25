/*
 * ECO
 */

function DonneeFinancierePart () {}

DonneeFinancierePart.prototype = AbstractScalableAJAXTableZone;

DonneeFinancierePart.prototype.doAddPeriode = false;

DonneeFinancierePart.prototype.afterStopEdition = function () {
	this.doAddPeriode = false;
};

DonneeFinancierePart.prototype.formatTable = function () {
	this.formatTableTd(this.$trInTbody);
};

DonneeFinancierePart.prototype.addSpy = function (data) {
	var dfhScreationSpy;
	var dfhSpy;
	if(!$.isPlainObject(data)) {
		dfhScreationSpy = data.find('creationSpy').text() ;
		dfhSpy = data.find('spy').text() ;
		globazNotation.utilsFormatter.displaySpy(this.detail, dfhScreationSpy, dfhSpy);
	}
};

DonneeFinancierePart.prototype.supperAfterRetrieve = function (data, idEntity) {
	var idEntity, b_isCopie, idVersionDroit;

	if($.isPlainObject(data)){
		var t = this.s_spy.split('\.');
		if (t.length) {
			var	objetRoot = data;
			for (var i = 0; i < t.length; i++) {
				objetRoot = objetRoot[t[i]];
			}
		}
		idEntity = objetRoot.idDonneeFinanciereHeader;
		b_isCopie = objetRoot.isCopieFromPreviousVersion;
		idVersionDroit = objetRoot.idVersionDroit;
	} else {
		this.addSpy(data);
		idEntity = data.find("idHeaderDonneeFianciere").text();
		b_isCopie = data.find('isCopieFromPreviousVersion').text() === "true";
		idVersionDroit = data.find("idVersionDroit").text();
	}
	pritDansCalcul.handelIcon(idEntity,idVersionDroit, this.detail, b_isCopie, this); 
};



DonneeFinancierePart.prototype.supperClearFields = function () {
	pritDansCalcul.handelIcon(null, null, this.detail, true); 
};

DonneeFinancierePart.prototype.executeClearFields = function () {
	this.clearFields();
	this.supperClearFields();
};

DonneeFinancierePart.prototype.executeAfterRetrieve = function (data,idEntity){
	this.afterRetrieve(data,idEntity);
	this.supperAfterRetrieve(data);
};


var b_isTdFormated_DonneeFinancierePart = false;

var fillDateDebut =  {
	btnAjaxAdd: null,
	dateAnnonce: null,
	
	init: function () {
		this.btnAjaxAdd = $('.btnAjaxAdd');
		this.dateAnnonce = globazNotation.utilsDate.convertDateToMontDate($("#infoDroitDateAnnonce").text());
		this.addEvent();
	},	
	
	addEvent: function () {
		var that = this;
		this.btnAjaxAdd.click(function () {
			var $container = $(this).closest(".mainContainerAjax");
			that.setDateDebut($container);
		});
	},

	setDateDebut: function ($container) {
		var that = this,
			$dateDebut = $container.find('[name="dateDebut"]');
		
		setTimeout(function ()   {
			$dateDebut.prop("disabled", false);
			$dateDebut.val(that.dateAnnonce);
		}, 50);
	}
};


buttonClorePeriode = {
	t_inited: [],
	superGetParametres: null,
	o_zone: null,
	
	init: function ($area, o_zone) {
		var that = this;
		//that.o_zone = o_zone;
		if($.inArray($area.attr("idMembre"), this.t_inited) === -1) {		
			$area.find(".btnAjaxUpdate").click(function () {
				var $dateFin = null;
				var $dateDebut = null;
				$dateFin = $area.find('[name="dateFin"]');
				$dateDebut = $area.find('[name="dateDebut"]');
				if(!$dateFin.length) {
					$dateFin = $area.find('[name$="dateFin"]');
					$dateDebut = $area.find('[name$="dateDebut"]');
				}
				if(!$dateFin.length) {
					$dateFin = $area.find('[class$="dateFin"]');
					$dateDebut = $area.find('[class$="dateDebut"]');
				}
				
				if(!$.trim($dateFin.val()) && $.trim($dateDebut.val())){
					var $boutton = $area.find('.btnAjaxClorePeriode');
					setTimeout(function (){$boutton.show();}, 100);
				}
			});
			
			$area.find('.btnAjaxClorePeriode').click(function () {
				 var superGetParametres = o_zone.getParametres;
				 o_zone.getParametres =  function () {
						var map = superGetParametres.call(o_zone);
						map.forceClorePeriode = true;
						o_zone.getParametres = superGetParametres;
						return map;
					};
				 o_zone.validateEdition();
			});
			
			this.t_inited.push($area.attr("idMembre"));
		}
	}
};

DonneeFinancierePart.prototype.onAddTableEvent = function () {
	// this.table.find('tbody tr[header=false]').hide();
	var that = this;
	// this.$trInTbody.filter("[header=false]").hide();
	// this.table.find('td,th').addClass("center").addClass("medium");
	this.table.find('th').addClass("center medium");
	buttonClorePeriode.init(this.mainContainer, this);
	// var $HeaderTrue = this.table.find('tbody tr[header=true]');
	// var $HeaderTrue = null; //this.$trInTbody.filter("[header=true]");
	// var $HeaderFalse = null;

	this.$trInTbody.each(function () {

		var $this = $(this);
		var $next = $this.next();

		if ($this.attr('header') == 'false') {
			$this.hide();

		} else if ($this.attr('header') == 'true') {
			$this.css({
				'font-weight': 'bold'
			});
			that.formatTableTd($this);
			that.addTableEventOnElements($this);
			if ($next.attr('header') == 'false') {

				$this.find('td:eq(0)').undelegate('click').click(function () {
					if (!that.b_isTdFormated_DonneeFinancierePart) {
						that.addTableEventOnElements(that.$trInTbody.filter(":hidden"));
						that.formatTableTd(that.$trInTbody.filter(":hidden"));
						that.b_isTdFormated_DonneeFinancierePart = true;
					}
					b_isTdFormated = true;
					var $td = $(this);
					var $parent = $this; // $this.parents('tr');
					var $siblings = $parent.siblings('[idGroup=' + $parent.attr('idGroup') + ']');
					if ($td.attr('groupVisible') == 'true') {
						$td.attr('groupVisible', 'false');
						$td.find('img').attr('src', '/webavs/images/arrowDown.gif');
						$siblings.hide();
					} else {
						$td.attr('groupVisible', 'true');
						$td.find('img').attr('src', '/webavs/images/arrowUp.gif');
						$siblings.show();
					}
				}).append('<img src="/webavs/images/arrowDown.gif"/>');
			}
		}
		
		if ($next.attr('header') == 'true') {
			// ajout d'un séparateur entre les groupes
			if(!that.nbTD){
				that.nbTD = $this.find("td").length;
			}
			$this.after('<tr class="spearator" style="line-height:0px">' +
						  '<td colspan="'+that.nbTD+'" style="background-color:#B3C4DB;border-bottom:1px solid #B3C4DB;border-top:1px solid #B3C4DB;">' +
						  '</td></tr>');
		}

	});

};

var keyborad = {
	$areaTitre: {},
	n_indexSelected: null,
	n_indexTable: 0,
	n_indexOnglet: null,
	$elementSelected: null,
	$trSelected: null,
	$trs: null,
	b_ctrl: false,
	b_alt: false,
	b_autreDetail: true,
	$onglet: null,

	init: function () {
		this.$areaTitre = $(".areaTitre");
		this.addEvent();
	},

	findNextIndex: function (event, n_currentIndex, $container, b_loop) {
		return this.findNextIndex0(event, n_currentIndex, $container, b_loop, [38, 40]);
	},

	findNextIndexTab: function (event, n_currentIndex, $container, b_loop) {
		return this.findNextIndex0(event, n_currentIndex, $container, b_loop, [37, 39]);
	},

	findNextIndex0: function (event, n_currentIndex, $container, b_loop, t_touche) {
		var n_index = 0, n_maxIndex = $container.length - 1;
		b_loop = (typeof b_loop === 'undefined') ? true : b_loop;
		if (event.which === t_touche[0]) {
			if ((n_currentIndex === 0 || n_currentIndex === null) && b_loop) {
				n_index = n_maxIndex;
			} else {
				n_index = n_currentIndex - 1;
			}
		} else if (event.which === t_touche[1]) {
			if ((n_currentIndex === n_maxIndex || n_currentIndex === null) && b_loop) {
				n_index = 0;
			} else {
				n_index = n_currentIndex + 1;
			}
		}
		return n_index;
	},

	findNextElement: function (event, n_currentIndex, $container) {

	},

	setMasterKey: function (b_actived, event) {
		if (17 === event.which) {
			this.b_ctrl = b_actived;
			this.setOngletEtatOrigine();
		}
		if (16 === event.which) {
			// this.b_alt = b_actived;
		}
	},

	bindZone: function (event) {
		var n_indexTable = 0;
		// fléche haut(38) bas(40)
		if (/^(38|40)$/.test(event.which)) {

			if (this.$elementSelected != undefined && this.$elementSelected.length) {
				var $trs = this.getTrsEnCours();// this.$elementSelected.closest('.areaMembre ').find('.areaDFDataTable').find('tbody tr:visible').not('.spearator');
				n_indexTable = this.findNextIndex(event, this.n_indexTable, $trs, false);
				this.b_autreDetail = (n_indexTable > $trs.length || n_indexTable < 0);
			}

			if (this.$elementSelected !== null /* && this.b_autreDetail */) {
				this.$elementSelected.removeClass('ui-state-hover');
			}

			if (this.b_autreDetail) {
				if (this.$trSelected != undefined && this.$trSelected.length) {
					this.$trSelected.removeClass('hover');
				}
				this.n_indexSelected = this.findNextIndex(event, this.n_indexSelected, this.$areaTitre);
				this.$elementSelected = $(this.$areaTitre.get(this.n_indexSelected));

				if (event.which == 38) {
					var $trs = this.getTrsEnCours(); // this.$elementSelected.closest('.areaMembre ').find('.areaDFDataTable').find('tbody tr:visible').not('.spearator');
					this.b_autreDetail = false;
					this.n_indexTable = $trs.length + 1;
				} else {
					this.$elementSelected.addClass('ui-state-hover');
					this.$elementSelected.find('.forFocus').focus();
				}

			} else {
				if (n_indexTable == 0 && event.which == 38) {
					this.$elementSelected.addClass('ui-state-hover');
					this.$elementSelected.find('.forFocus').focus();
					this.b_autreDetail = true;
				} else {

				}
			}
			this.setOngletEtatOrigine();
			this.selectTr(event);
		}

	},

	setOngletEtatOrigine: function () {
		if (this.n_indexOnglet !== null) {
			this.$onglet.eq(this.n_indexOnglet).removeClass('ui-state-focus');
			this.$onglet.eq(this.n_indexOnglet).find('a').css('color', '#D5DFED');
		}
	},

	bindOnglet: function (event) {
		var $onglet = this.$onglet.not('.selected');
		var $a;
		if (this.n_indexOnglet === null) {
			this.n_indexOnglet = this.$onglet.filter('.selected').index();
		}
		if (/^(37|39)$/.test(event.which)) {
			var n_index = this.findNextIndexTab(event, this.n_indexOnglet, this.$onglet);
			this.setOngletEtatOrigine();
			if (this.$onglet.eq(n_index).is('.selected')) {
				this.n_indexOnglet = n_index;
				this.bindOnglet(event);
			} else {
				this.$onglet.eq(n_index).addClass('ui-state-focus');
				$a = this.$onglet.eq(n_index).find('a');
				$a.css('color', 'black').focus();
				this.n_indexOnglet = n_index;
			}
		}
	},

	getTrsEnCours: function () {
		return this.$elementSelected.closest('.areaMembre ').find('.areaDFDataTable').find('tbody tr:visible').not('.spearator');
	},

	selectTr: function (event) {
		var $trs = this.getTrsEnCours();
		if (this.$trSelected !== null) {
			this.$trSelected.removeClass('hover');
		}
		if (!this.b_autreDetail) {
			this.n_indexTable = this.findNextIndex(event, this.n_indexTable, $trs, false);
			this.$trSelected = $trs.eq(this.n_indexTable - 1);

			if (this.$trSelected.is('.trVide')) {
				this.bindZone(event);
			} else {
				this.$trSelected.addClass('hover');
				this.$trSelected.find('.forFocus').focus();
			}
		} else {
			this.n_indexTable = 0;
			this.$trSelected = null;
		}
	},

	addEvent: function () {
		var that = this, b_addFocus = false;
		$trs = $('.areaDFDataTable').find('tbody tr:visible').not('.spearator');

		this.$onglet = $('.onglets li');
		// Bind la touche ENTER
		this.$areaTitre.keydown(function (e) {
			if (13 === e.which && that.$elementSelected !== null) {
				that.$elementSelected.click();
			}
		});

		$('html').keyup(function (e) {
			that.setMasterKey(false, e);
		});
		$('html').keydown(function (e) {
			if (!b_addFocus) {
				ajaxUtils.addElementForFocus(that.$areaTitre);
				ajaxUtils.addElementForFocus($trs.find('td:eq(0)'));
				// ajaxUtils.addElementForFocus(that.$onglet);
				// Bind la touche ENTER
				$trs.keydown(function (e) {
					if (13 === e.which && that.$trSelected !== null) {
						that.$trSelected.find('td:eq(1)').click();
						e.stopImmediatePropagation();
					}
				});
				b_addFocus = true;
			}
			that.setMasterKey(true, e);
			if (that.b_ctrl) {
				that.bindZone(e);
				that.bindOnglet(e);
			}
		});
	}
};



var pritDansCalcul = {
	$elementTrigger: null,
	$container: null,
	s_imagePath: null,
	s_selectorIcon: ".icon_prendreEnCompteDansCalcul",
	s_iconPritEncompte:"ui-icon-bullet", //ui-icon-calculator
	s_iconExclut:"ui-icon-cancel",
	b_isTempalateInit: false,
	b_isAlterabled: false,
	n_idVersionDroitAffiche: null,
	n_idVersionDroitTemp:null,

	init: function () {
		var that = this, 
			s_message = "",
			s_csEtatDroit ='',
			$infoDroit =  $("#infoDroit");
		
		this.n_idVersionDroitAffiche = $infoDroit.attr("idVersionDroit");
		s_csEtatDroit = $infoDroit.find("[areaAssureIdCsEtatDroit]").attr('areaAssureIdCsEtatDroit');
		
		//Enregistré, Au calcul, Calculé
		this.b_isAlterabled  = ('64003001,64003002,64003003').indexOf(s_csEtatDroit) >= 0;
		if (this.b_isAlterabled) {
			$(".conteneurMembres").on("click", ".prendreEnCompteDansCalcul", function () {
				if(that.$container && that.$container.length && that.isAlterabled() ){
					var $icon = that.$container.find(that.s_selectorIcon);
					if(	that.$container.find(that.s_selectorIcon).data("idEntity")) {
						that.createDialog();
						if(!$icon.data("isCopieFromPreviousVersion")) {
							s_message = "Voulez-vous exclure cette donnée de la définition de la plage de calcul ?";
						} else {
							s_message = "Voulez-vous inclure cette donnée dans la définition de la plage de calcul ?";
						}
						that.$templateDialog.find(".textDialogToDispplay").text(s_message);
						that.$templateDialog.dialog("open");
						that.$elementTrigger = $(this);
						that.s_imagePath = that.$elementTrigger.find(that.s_selectorIcon).css("background-image");
						that.$container = that.$elementTrigger.closest(".areaDetail");
					}
				}
			});
		} 
	},
	
	isAlterabled: function () {
		return (this.n_idVersionDroitAffiche == this.n_idVersionDroitTemp) && this.b_isAlterabled;
	},
	
	handelIcon: function (idEntity, idVersionDroit, $container, b_isCopieFromPreviousVersion) {
		this.n_idVersionDroitTemp = idVersionDroit;
		$container.find(".wrapperPrendreEnCompteDansCalcul").remove();
		if(	idEntity ) {
			this.$templateDialog = $("<div><div class='textDialogToDispplay'></div></div>");
			
			this.$templateIcon = $("<div class='wrapperPrendreEnCompteDansCalcul' style='position:absolute;right:0px; margin-right:4px'>" +
										"<div class='prendreEnCompteDansCalcul ui-state-default1 ui-corner-all1'>" +
											"<span class='ui-icon icon_prendreEnCompteDansCalcul'></span>" +
										"</div>" +
									"</div>");
			if (this.isAlterabled()) {
				globazNotation.utilsInput.emulateJqueryButton(this.$templateIcon);
			} else {
				$container.remove(".wrapperPrendreEnCompteDansCalcul");
				this.$templateIcon.find(".prendreEnCompteDansCalcul").css("cursor",'inherit');
			}
			$container.prepend(this.$templateIcon);
			var $icon = $container.find(this.s_selectorIcon);
			this.$container = $container;
			$icon.data("idEntity",idEntity);
			this.changeIcon(b_isCopieFromPreviousVersion,idVersionDroit);
		}
	},
	
	createDialog: function () {
		var that = this;
		this.$templateDialog.dialog({
			autoOpen: false,
			open: function (event, ui) {
			},
			width: 380,
			buttons: {
				"Oui": function (ui, event) {
					that.executeAjax(this);
					$(this).dialog("destroy");
				},
				"Non": function () {
					$(this).dialog("destroy");
				}
			}
		});
	},

	updateImage: function (data) {
		$icon.css("background-image", 'url("images/ui-icons_638fbc_256x240.png")');;
	},
	
	changeIcon: function (b_isCopieFromPreviousVersion,idVersionDroit) {
		var $icon = this.$container.find(this.s_selectorIcon), s_title;
		$icon.data("isCopieFromPreviousVersion",b_isCopieFromPreviousVersion);
		
		// On affiche l'icon si on a bien une donnée fiancière
		if($icon.data("idEntity")){
			$icon.show();
			if	(!b_isCopieFromPreviousVersion && (idVersionDroit === this.n_idVersionDroitAffiche) )	{
				s_title = "Participe à la définition de la plage de calcul";
				$icon.closest(".ui-icon").removeClass(this.s_iconExclut).addClass(this.s_iconPritEncompte).css("background-image", 'url("theme/jquery/images/ui-icons_638fbc_256x240.png")');
			} else {
				$icon.closest(".ui-icon").removeClass(this.s_iconPritEncompte).addClass(this.s_iconExclut).css("background-image", 'url("theme/jquery/images/ui-icons_f9bd01_256x240.png")');
				s_title = "Est exclu de la définition de la plage de calcul";
			}
		} else {
			$icon.hide();
		}
		$icon.attr("title",s_title);
	},
	
	callBack: function (data,elementUi) {
		
		var zone = this.$container.closest(".areaMembre").get(0).zone;
		this.$container.find(this.s_selectorIcon).unblink();
		this.$container.removeOverlay();
		if(data) {
			this.changeIcon(data.isCopieFromPreviousVersion,data.idVersionDroit);
			// on doit recharger le detail pour ne pas avoir une erreur de typ concurente acess, car le viewbean est serilisé du coté client
			zone.ajaxLoadEntity(zone.idEntity, true);
			globazNotation.utilsFormatter.displaySpy(this.$container,data.creationSpy, data.spy);
		}
	}, 

	executeAjax: function (elementUi) {
		var that = this, o_options, ajax, 
			$icon = this.$container.find(that.s_selectorIcon);
		
		$icon.blink({delay:100});
		this.$container.overlay({b_relatif:true});
		o_options = {
			serviceClassName: "ch.globaz.pegasus.business.services.models.droit.DonneeFinanciereHeaderService",
			parametres: $icon.data("idEntity") +","+ this.n_idVersionDroitAffiche,
			serviceMethodName: 'toggleTookInCalculating',
			callBack: function (data) {
				that.callBack(data,elementUi);
			},
			errorCallBack: function (jqXHR) {
				ajaxUtils.displayError(jqXHR);
				that.callBack(null,elementUi);
			}
		};
		
		ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
		ajax.options = o_options;
		ajax.read();
	}
};


$(function () {
	$('html').bind(eventConstant.AJAX_INIT_DONE, function () {
		globazNotation.utilsInput.applyChangeOnSelect();
		keyborad.init();
		pritDansCalcul.init();
		fillDateDebut.init();
	});
});
