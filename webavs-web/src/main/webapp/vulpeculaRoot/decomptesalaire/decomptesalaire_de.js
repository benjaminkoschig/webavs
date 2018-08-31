var GLO = GLO || {};

GLO.isMensuel = false;
GLO.idPosteTravail = null;
GLO.idNouveauTravailleur = null;

var zoneAjax;

GLO.keys = {
	enter : "13",
	right_row : "39",
	left_row : "37",
	plus : "107"
};

GLO.absences = {
	options : {
		s_template_selector : "#lineAbsence",
		s_table_selector : "#tblAbsences"
	},
	absencesTable : null,
	init : function() {
		this.absencesTable = vulpeculaTable.create(this.options);
	},
	edit : function() {
		this.absencesTable.editRow();
	},
	load : function(data) {
		this.clearFields();
		this.absencesTable.load(data.decompteSalaireGSON.absencesGSON);
	},
	save : function() {
		var s_values = this.absencesTable.saveAsStringWithJSON();
		var o_absences = {
				absencesGSON : s_values
		};
		return o_absences;
	},
	clearFields : function() {
		this.absencesTable.clear();
	},
	addRow : function() {
		this.absencesTable.addRow();
		this.setFocusLastRow();
	},
	setFocusLastRow : function() {
		var lastInput = $('#tblAbsences').find('select').last();
		lastInput.focus();
	}
};

GLO.navigation = {
	//Le parent correspondant a la DefaultTableAjaxList
	o_parent : null,
	//Définit le mode (update/read)
	mode : null,
	isEntityLoaded : false,
	isFirst : false,
	isLast : false,
	//Définit si lors de la validation il faut passer au cas suivant
	isPasserAuSuivant : true,
	//Définit si par défaut on souhaite entrer en modification dans l'écran
	modificationParDefaut : true,
	boutonValider : null,
	boutonDelete : null,
	wantNouveau : false,
	init : function(parent) {
		var that = this;
		
		this.o_parent = parent;
		this.boutonValider = $("#btnAjaxValidate");
		this.boutonDelete = $("#btnAjaxDelete");
		this.boutonNouveau = $("#btnAjaxNouveau");
		
		//Si le décompte est éditable et que l'on a activer l'option modification par défaut
		if(globazGlobal.IS_EDITABLE && this.modificationParDefaut) {
			this.mode = globazGlobal.IS_EDITABLE==true ? "update" : "read";
		}
		
		//Si on est en création, on désactive le cas à cocher isPassserAuSuivant
		if(globazGlobal.isNouveau) {
			this.isPasserAuSuivant = false;
			setTimeout(function() {
				that.showNouveauButton();
				$('#descriptionPosteTravail').focus();
			});;
		}
		
		this.bindEvents();
		//Gestion des bornes par rapport au bouton isPasserSuivant (début, fin)
		this.handleIsPasserSuivant();
		
	},
	bindEvents : function() {
		var that = this;
		this.boutonNouveau.click(function() {
			if(globazGlobal.isNouveau) {
				GLO.navigation.wantNouveau = true;
				that.o_parent.validateEdition();
			} else {
				that.redirectToNouveau();
			}
		});
	},
	redirectToNouveau : function() {
		window.location.href = "vulpecula?userAction=vulpecula.decomptesalaire.afficher&_method=add&idDecompte=" + globazGlobal.ID_DECOMPTE;
	},
	showNouveauButton : function() {
		this.boutonNouveau.show();
	},
	enableDisableNavigationButton : function(previous, next) {
		this.isFirst = previous;
		this.isLast = next;
		GLO.decompteSalaire.$btnPrevious.prop('disabled', previous);
		GLO.decompteSalaire.$btnNext.prop("disabled", next);
	},
	handleIsPasserSuivant : function() {
		if(!globazGlobal.IS_EDITABLE) {
			this.isPasserAuSuivant = false;
		}
		
		if(GLO.navigation.isLast) {
			GLO.decompteSalaire.$passerSuivant.prop("disabled", true);
			GLO.navigation.isPasserAuSuivant = false;
		} else {
			GLO.decompteSalaire.$passerSuivant.prop("disabled", false);
		}
		GLO.decompteSalaire.$forcerFranchise0.prop('checked',false);
		if (this.isPasserAuSuivant) {
			this.boutonValider.hide();
			GLO.decompteSalaire.$btnNext.html("&nbsp;&nbsp;" + globazGlobal.BOUTON_VALIDER_LABEL + "&nbsp;&gt;&nbsp;&nbsp;");
			GLO.decompteSalaire.$passerSuivant.prop('checked',true);
		} else {
			GLO.decompteSalaire.$passerSuivant.prop('checked',false);
			if ("update" === this.mode) {
				this.boutonValider.show();
				this.boutonDelete.show();
			}
			GLO.decompteSalaire.$btnNext.html("&nbsp;&nbsp;&gt;&nbsp;&nbsp;");
		}		
	},
	clickPrevious : function() {
		if (!GLO.decompteSalaire.$btnPrevious.is(':disabled')) {
			// Désactive le bouton afin d'éviter le multi-click en cas de lenteur
			GLO.decompteSalaire.$btnPrevious.prop("disabled", true);
			
			if (!this.isFirst) {
				this.o_parent.navigation = "PREVIOUS";
				this.o_parent.ajaxLoadEntity();
			}
		}
	},
	clickNext : function() {
		if (!GLO.decompteSalaire.$btnNext.is(':disabled')) {
			// Désactive le bouton afin d'éviter le multi-click en cas de lenteur
			GLO.decompteSalaire.$btnNext.prop("disabled", true);
			if (!this.isLast) {
				if (!GLO.decompteSalaire.$heure.is(':disabled') && GLO.navigation.isPasserAuSuivant) {
					GLO.decompteSalaire.beforeUpdate();
					this.o_parent.validateEdition();
				} else {
					this.o_parent.navigation = "NEXT";
					this.o_parent.ajaxLoadEntity();
				}
			}
		}
	},
	clickUpdate : function() {
		this.mode = "update";
		if(!this.isEntityLoaded) {
			this.o_parent.ajaxLoadEntity();
		}
		if (GLO.idPosteTravail != null) {
			GLO.decompteSalaire.$descriptionPosteTravail.prop('readonly', true);
			GLO.decompteSalaire.$descriptionPosteTravail.addClass("readOnly");
		}
		setTimeout(function () {
			GLO.decompteSalaire.manageMensuel();
		},200);
	},
	startEdition : function(parent) {
		parent.startEdition();
		GLO.navigation.boutonDelete.show();
		GLO.absences.edit();
	},
	isModeLecture : function() {
		return GLO.decompteSalaire.$heure.is(':disabled');
	},
	redirectToDecompteIfLast : function() {
		var isEnErreur = $("#decompteSalaireGSON\\.isEnErreur").val();
    	if(this.isLast && isEnErreur == "false") {
    		//Hack: On délaie la redirection afin d'étre sûr que les décomptes salaires soient mise à jour (AJAX)
    		setTimeout(function() {
    			window.location = "vulpecula?userAction=vulpecula.decomptedetail.decomptedetail.afficher&mode=edition&selectedId="+globazGlobal.ID_DECOMPTE;
    		},200);
    	}
	}
};

GLO.decompteSalaire = {
	cotisationMustReload : "false",
	isDeleted : false,
	init: function () {
		var that = this;
		this.$heure = $("#decompteSalaireGSON\\.heures");
		this.$salaireHoraire = $("#decompteSalaireGSON\\.salaireHoraire");
		this.$salaireTotal = $("#decompteSalaireGSON\\.salaireTotal");
		this.$masseAC2 = $("#decompteSalaireGSON\\.masseAC2");
		this.$masseFranchise = $("#decompteSalaireGSON\\.masseFranchise");
		this.$mntCalcule = $("#mntCalcule");
		this.$passerSuivant = $("#passerSuivant");
		this.$btnNext = $("#btnNext");
		this.$btnPrevious = $('#btnPrevious');
		this.$descriptionPosteTravail = $('#descriptionPosteTravail');
		this.$periodeDebut = $('#decompteSalaireGSON\\.periodeDebut');
		this.$periodeFin = $('#decompteSalaireGSON\\.periodeFin');
		this.$vider = $('#vider');
		this.$quittancer = $('#decompteSalaireGSON\\.quittancer');
		this.$anneeCotisations = $('#decompteSalaireGSON\\.anneeCotisations');
		this.$forcerFranchise0 = $('#decompteSalaireGSON\\.forcerFranchise0');
		this.$isEnErreur = $('#decompteSalaireGSON\\.isEnErreur');
		
		this.$masseAC2.change(function (event, data) {
			if(eventConstant.isChangeFromAjax(data)) return;
			GLO.cotisations.load(GLO.decompteSalaire.getSalaireTotal());
		});
		
		this.$masseFranchise.change(function (event, data) {
			if(eventConstant.isChangeFromAjax(data)) return;
			GLO.cotisations.load(GLO.decompteSalaire.getSalaireTotal());
		});
		
		this.$salaireTotal.change(function (event, data) {
			if(eventConstant.isChangeFromAjax(data)) return;
			that.roundSalaireTotal();
			that.calculMontant();
			GLO.cotisations.load(GLO.decompteSalaire.getSalaireTotal());
		});
		
		this.$vider.click(function() {
			that.$heure.val('0.00');
			that.$salaireHoraire.val('0.00');
			that.$salaireTotal.val('0.00');
			that.$heure.select().focus();
		});
		
		this.$periodeDebut.add(this.$periodeFin).change(function (event, data) {
			if(eventConstant.isChangeFromAjax(data)) return;
			var $calendar = $(this);
			var dateDebut = getDateFromFormat(that.getPeriodeDebut(), 'dd.MM.yyyy');
			var dateFin = getDateFromFormat(that.getPeriodeFin(), 'dd.MM.yyyy');
			if(that.getPeriodeDebut().length>0 && that.getPeriodeFin().length>0 && GLO.isMensuel && dateDebut < dateFin) {
				if(!$calendar.prop('readOnly')) {
					that.loadHeures(false);
				}
			}
		});
		
		if(this.hasAnneeCotisations()) {
			this.$anneeCotisations.val(new Date().getFullYear());
		}
	},
	roundSalaireTotal : function() {
		var $this = this.$salaireTotal;
		var salaireTotalFloat = globazNotation.utilsFormatter.amountTofloat($this.val());
		$this.val(globazNotation.utilsFormatter.formatStringToAmout(roundToFiveCentimes(salaireTotalFloat)));
	},
	isTypeDecompteWithHeure : function() {
		return globazGlobal.TYPE_DECOMPTE == globazGlobal.TYPE_PERIODIQUE;
	},
	hasAnneeCotisations : function() {
		return globazGlobal.TYPE_DECOMPTE === globazGlobal.TYPE_CPP;
	},
	resetHeures : function() {
		this.$heure.val("0.00");
	},
	getPeriodeDebut : function() {
		return this.$periodeDebut.val();
	},
	getPeriodeFin : function() {
		return this.$periodeFin.val();
	},
	calculMontant : function() {
		 var nbHeure = this.getHeures();
		 var salaireHoraire = this.getSalaireHoraire();
		 var salaireTotal = this.getSalaireTotal();
		 var mntCalcule = parseFloat(nbHeure * salaireHoraire).toFixed(2);
		 
		GLO.decompteSalaire.$mntCalcule.val(mntCalcule);
		if (salaireTotal == 0 && mntCalcule != 0) {
			GLO.decompteSalaire.$salaireTotal.val(mntCalcule);
		}
		if (salaireTotal != 0 && salaireHoraire != 0 && nbHeure == 0) {
			GLO.decompteSalaire.$heure.val((salaireTotal / salaireHoraire).toFixed(2));
		}
		if(salaireHoraire == 0 && salaireTotal != 0 && nbHeure != 0) {
			var salaireHoraireCalcule = salaireTotal / nbHeure;
			this.$salaireHoraire.val(globazNotation.utilsFormatter.formatStringToAmout(salaireHoraireCalcule,2,true));			
		}
		this.roundSalaireTotal();
	},
	getHeures : function() {
		return GLO.decompteSalaire.$heure.val().replace("'","");
	},
	getSalaireHoraire : function() {
		return GLO.decompteSalaire.$salaireHoraire.val().replace("'", "");
	},
	getSalaireTotal : function() {
		return globazNotation.utilsFormatter.amountTofloat(this.$salaireTotal.val());
	},
	getMasseAC2 : function() {
		if(this.$masseAC2.length>0) {
			return globazNotation.utilsFormatter.amountTofloat(this.$masseAC2.val());	
		} else {
			return "0.0";
		}
	},
	getMasseFranchise : function() {
		if(this.$masseFranchise.length>0) {
			return globazNotation.utilsFormatter.amountTofloat(this.$masseFranchise.val());
		} else {
			return "0.0";
		}
	},
	getAnneeCotisations : function() {
		var annee = parseInt(this.$anneeCotisations.val(), 0);
		if(annee>0) {
			return annee;
		} else {
			return null;
		}
	},
	manageMensuel : function() {
		var that = this;
		if(GLO.isMensuel) {
			//Si c'est un salaire mensuel, on va alors tenter de rechercher les heures
			if(this.getHeures()==0 && globazGlobal.isNouveau) {
				this.loadHeures(true);
			}
			this.$mntCalcule.hide();
		} else {
			this.$mntCalcule.show();
		}
		this.$salaireHoraire.removeAttr('readonly');
		this.$heure.removeAttr('readonly');
		this.$salaireHoraire.removeClass('readOnly');
		this.$heure.removeClass('readOnly');

		if (GLO.decompteSalaire.isTypeDecompteWithHeure()) {
			this.$heure.focus().select();
		} else {
			//Workaround: Probléme de sélection inexplicable
			setTimeout(function() {
				that.$salaireTotal.focus().select();
			}, 100);
		}
		
		if(!globazGlobal.isNouveau && !globazGlobal.isNouveauTravailleur) {
			this.$descriptionPosteTravail.attr('readonly','readonly');
			this.$descriptionPosteTravail.addClass('readOnly');
		}
	},
	loadHeures : function(wantFocus) {
		var that = this;
		if (GLO.decompteSalaire.isTypeDecompteWithHeure()) {
			if (GLO.idPosteTravail == null) {
				return;
			}
			var options = {
					serviceClassName:globazGlobal.posteTravailViewService,
					serviceMethodName:'findNombreHeuresParMois2',
					parametres:GLO.idPosteTravail + ',' + that.getPeriodeDebut() + ',' + that.getPeriodeFin(),
					callBack:function (data) {
						that.$heure.val(globazNotation.utilsFormatter.formatStringToAmout(data,2,true));
						if(wantFocus) {
							if (GLO.decompteSalaire.isTypeDecompteWithHeure()) {
								that.$heure.focus().select();
							} else {
								that.$salaireTotal.focus().select();
							}
						}
					}
			};
			vulpeculaUtils.lancementService(options);
		}
	},
	beforeUpdate : function() {
		var diffCotisations = 'difference_cotisations';
		var diffAnnee = 'difference_annee';
		var anneeCotisations = GLO.decompteSalaire.getAnneeCotisations();
		if(globazGlobal.isNouveau || GLO.decompteSalaire.isDeleted || globazGlobal.isNouveauTravailleur) {
			return;
		}
		var options = {
				serviceClassName:globazGlobal.decompteSalaireViewService,
				serviceMethodName:'checkCotisationDecompte',
				parametres:globazGlobal.ID_LIGNE_DECOMPTE+','+anneeCotisations,
				callBack:function (data) {
					GLO.decompteSalaire.cotisationMustReload = "false";
					if (diffCotisations===data) {
						if (window.confirm(globazGlobal.messageDialogCotisation)) {
							GLO.decompteSalaire.cotisationMustReload = "true";
						}
					} else if(diffAnnee===data)  {
						GLO.decompteSalaire.cotisationMustReload = "true";
					} else {
						// On ne fait rien ! throw "Unexpected value for data";
					}

				}
		};
		vulpeculaUtils.lancementServiceSync(options);
	}
};

GLO.historiqueSalaire = (function() {
	function load() {
		$('#historiquesalaireContent').empty();
		if (GLO.idPosteTravail == null) {
			return;
		}
		vulpeculaUtils.ajaxWait({
			data: { idPosteTravail:GLO.idPosteTravail,
				   "userAction":"vulpecula.decomptesalaire.historiqueSalaireAjax.afficherAJAX"
				},
				selectorToAppend : '#historiquesalaireContent',
			success: function (data) {
				$('#historiquesalaireContent').empty().html(data);
			}
		});
	}
	return {
		load : load
	};
})();

GLO.cotisations = (function(){
	function init (parent) {
		$('#cotisations').on('click','.supprimerCotisation', function() {
			var message = jQuery.i18n.prop("ajax.deleteMessage");
			if (!window.confirm(message)) {
				return;
			}
			
			var idCotisationdecompte = $(this).attr('data-idCotisationDecompte');
			var options = {
					serviceClassName:globazGlobal.decompteSalaireViewService,
					serviceMethodName:'deleteCotisationDecompte',
					parametres:globazGlobal.ID_LIGNE_DECOMPTE + "," + idCotisationdecompte,
					callBack:function (data) {
						load();
						$('#decompteSalaireGSON\\.tauxContribuable').val(data);
					}
			};
			vulpeculaUtils.lancementService(options);		
		});
	}

	function load(masseSalariale) {
		$.ajax({
			data: {idDecompteSalaire:globazGlobal.ID_LIGNE_DECOMPTE,
				  "userAction":"vulpecula.decomptesalaire.cotisationsAjax.afficherAJAX",
				  masseSalariale : masseSalariale,
				  masseAC2 : GLO.decompteSalaire.getMasseAC2(),
				  masseFranchise : GLO.decompteSalaire.getMasseFranchise()
				},
			success: function (data) {
				$('#cotisationsContent').empty().html(data);
			}
		});
	}
	
	return {
		init : init,
		load : load
	};
})();

$(function () {
	
	$('.consultationGed').click(function() {
		$('.consultationGed').prop('disabled', true);
		var id = $(this).attr('id');
		
		var options = {
				serviceClassName:globazGlobal.decompteViewService,
				serviceMethodName:'callTGedmyProdis',
				parametres:id,
				callBack: function() {
					
				}
		}
		vulpeculaUtils.lancementService(options);
	});
	setTimeout(function() {
		$('.consultationGed').prop('disabled', false);
	}, 100);
	
	$("#aideContext").click(function() { 
		$("#help-show").fadeToggle();
	});
	function forceRemoveFocus() {
		$('.hasFocus').removeClass('hasFocus');
	}
	
	$("#tabs").tabs({
		selected : 1
	});
	
	
	$("#btnAjaxValidate").click(function() {
		GLO.decompteSalaire.beforeUpdate();
	});
	$("#btnAjaxDelete").click(function() {
		GLO.decompteSalaire.isDeleted = true;
	});
	
	GLO.decompteSalaire.init();
	GLO.handleNote();
	
	GLO.decompteSalaire.$heure.on("change", function(event, data) {
		if(eventConstant.isChangeFromAjax(data)) return;
		GLO.decompteSalaire.calculMontant();
		GLO.cotisations.load(GLO.decompteSalaire.getSalaireTotal());
	});
	GLO.decompteSalaire.$salaireHoraire.on("change", function(event, data) {
		if(eventConstant.isChangeFromAjax(data)) return;
		GLO.decompteSalaire.calculMontant();
		GLO.cotisations.load(GLO.decompteSalaire.getSalaireTotal());
	});
	GLO.decompteSalaire.$passerSuivant.click(function(event) {
		GLO.navigation.isPasserAuSuivant = this.checked;
		GLO.navigation.handleIsPasserSuivant();
	});
	$("#btnAjaxUpdate").click(function() {
		GLO.navigation.clickUpdate();
		GLO.absences.edit();
	});
	
	function handleEBusinessPart(data) {
		var $ebusinesspart = $('#ebusinesspart')
		if(!data) {
			$ebusinesspart.css('display','none');
			return;
		}
		// Gestion des codes erreurs
		$("#listCodeErreurs").html("");
		var tabCodes = data.decompteSalaireGSON.codeErreur;
		if (tabCodes && tabCodes.length > 0) {
			$.each(tabCodes, function(index, item) {
				GLO.loadLibelleCodeErreur(item);
			});
			$ebusinesspart.css('display','block');
		} else {
			$ebusinesspart.css('display','none');
		}
	}
	
	handleEBusinessPart();
	
	defaultDetailAjax.init({
		s_entityIdPath : "decompteSalaireGSON.idDecompteSalaire",
		init: function () {
			zoneAjax = this;
			GLO.navigation.init(this);
			GLO.absences.init();
			GLO.cotisations.init(this);
			
			if(globazGlobal.ID_LIGNE_DECOMPTE) {
				this.ajaxLoadEntity();
			}
			
			var that = this;
			var $areaDetail = $(".areaDetail");
			this.selectedEntityId = globazGlobal.ID_LIGNE_DECOMPTE;
			$("#decompteSalaireGSON\\.idDecompteSalaire").val(this.selectedEntityId);

			//
			$areaDetail.on(eventConstant.AJAX_UPDATE_COMPLETE, function() {
				if(GLO.navigation.wantNouveau) {
					GLO.navigation.redirectToNouveau();
				}
				globazGlobal.isNouveau = false;
				
				if(!GLO.navigation.isPasserAuSuivant) {
					GLO.navigation.mode = "read";
				} else {
					GLO.decompteSalaire.$descriptionPosteTravail.prop('readonly', false);
					GLO.decompteSalaire.$descriptionPosteTravail.removeClass("readOnly");
		    		GLO.navigation.startEdition(that);
		    		GLO.decompteSalaire.manageMensuel();
				}
				
				GLO.decompteSalaire.calculMontant();
				GLO.navigation.handleIsPasserSuivant();
				GLO.navigation.showNouveauButton();

				if (!GLO.decompteSalaire.$quittancer.is(':checked')) {
					GLO.decompteSalaire.$quittancer.prop("disabled", false);
					$("#aTraiter").show();
				} else {
					$("#aTraiter").hide();
				}
			});

			$areaDetail.on(eventConstant.AJAX_LOAD_DATA, function() {
				GLO.navigation.isEntityLoaded = true;
				if ('update' == GLO.navigation.mode) {
					if (!GLO.decompteSalaire.$heure.is(':disabled') && GLO.idPosteTravail == null) {
						GLO.decompteSalaire.$descriptionPosteTravail.prop('readonly', true);
						GLO.decompteSalaire.$descriptionPosteTravail.addClass('readOnly');
					} else {
						GLO.decompteSalaire.$descriptionPosteTravail.prop('readonly', false);
						GLO.decompteSalaire.$descriptionPosteTravail.removeClass('readOnly');
						GLO.navigation.startEdition(that);
					}
				}
				GLO.decompteSalaire.calculMontant();
				GLO.decompteSalaire.manageMensuel();
				GLO.navigation.handleIsPasserSuivant();
				GLO.navigation.showNouveauButton();

				if (!GLO.decompteSalaire.$quittancer.is(':checked')) {
					GLO.decompteSalaire.$quittancer.prop("disabled", false);
					$("#aTraiter").show();
				} else {
					$("#aTraiter").hide();
				}
				
			});
			
			zoneAjax.onDelete = function(data) {
				if(GLO.navigation.isLast) {
					GLO.navigation.redirectToDecompteIfLast();
				} else {
					GLO.navigation.clickNext();
				}
			};
			
			GLO.decompteSalaire.$btnPrevious.click(function() {
				GLO.navigation.clickPrevious();
			});
			GLO.decompteSalaire.$btnNext.click(function() {
				GLO.navigation.clickNext();
			});
			
			$(document).on('keydown', function (e) {
			    if(e.keyCode == GLO.keys.right_row && e.ctrlKey) {
			    	GLO.navigation.clickNext();
			    } else if(e.keyCode == GLO.keys.left_row && e.ctrlKey) {
			    	GLO.navigation.clickPrevious();
			    } else if (!GLO.navigation.isModeLecture()) {
			    	if (e.keyCode == GLO.keys.plus && e.ctrlKey && e.altKey) {
			    		GLO.absences.addRow();
					}
			    }
			});
			
			$(document).keypress(function(e) {
			    if(globazGlobal.IS_EDITABLE && e.which == GLO.keys.enter) {
			    	if (!GLO.navigation.isModeLecture()) {
			    		GLO.decompteSalaire.calculMontant();
			    		GLO.decompteSalaire.beforeUpdate();
			        	that.validateEdition();
			        	GLO.navigation.redirectToDecompteIfLast();
			        	forceRemoveFocus();
			    	}
			    }
			});
			
			if(!this.selectedEntityId) {
				GLO.navigation.startEdition(this);
				GLO.decompteSalaire.$btnNext.prop("disabled", true);
				GLO.decompteSalaire.$btnPrevious.prop("disabled", true);
				GLO.decompteSalaire.$passerSuivant.prop("disabled", true);
			}
			
			this.sequence = globazGlobal.SEQUENCE;
			this.navigation = globazGlobal.NAVIGATION;

		},
		getParametersForLoad: function() {
			this.sequence = $("#decompteSalaireGSON\\.sequence").val();
			return {
				idDecompte:globazGlobal.ID_DECOMPTE, 
				sequence: this.sequence, 
				navigation:this.navigation,
				idDecompteSalaire:globazGlobal.ID_LIGNE_DECOMPTE,
				isPasserSuivant : GLO.navigation.isPasserAuSuivant
			};
		},
		onRetrieve: function(data) {
			GLO.isMensuel = data.decompteSalaireGSON.isMensuel;
			
			if (data.decompteSalaireGSON.idPosteTravail) {
				GLO.idPosteTravail = data.decompteSalaireGSON.idPosteTravail;
				globazGlobal.TAUXEBU = data.decompteSalaireGSON.tauxSaisieEbu;
			} else {
				GLO.idPosteTravail = null;
			}
			GLO.idNouveauTravailleur = data.decompteSalaireGSON.correlationId;
			globazGlobal.isNouveauTravailleur = data.decompteSalaireGSON.nouveauTravailleur;
			
			this.t_element = this.defaultLoadData(data, "#");
			globazGlobal.ID_LIGNE_DECOMPTE = data.decompteSalaireGSON.idDecompteSalaire;
			zoneAjax.selectedEntityId = globazGlobal.ID_LIGNE_DECOMPTE; 
			GLO.cotisations.load();
			GLO.absences.load(data);
			GLO.historiqueSalaire.load();
			GLO.navigation.enableDisableNavigationButton(data.decompteSalaireGSON.first, data.decompteSalaireGSON.last);
			GLO.hasDroitAF(data);
			this.disabeldEnableForm(true);
			handleEBusinessPart(data);
			$('#libelleDateFin').text($('#decompteSalaireGSON\\.periodeFin').val());
			var $ligneSuprimmee = $("#ligneSuprimmee");
			if(data.decompteSalaireGSON.ligneSupprimee){
				$ligneSuprimmee.show();
			}else{
				$ligneSuprimmee.hide();
			}

			if(data.decompteSalaireGSON.quittancer){
				$ligneSuprimmee.hide();
			}
			$('.lastModification').html(' Update: ' + data.decompteSalaireGSON.spy);
			GLO.handleNote();
			GLO.decompteSalaire.isDeleted = false;
		},
		onUpdate: function (data,action) {			
			
			if(action!=='del') {

				this.superOnUpdate(data);
				
				$('#libelleDateFin').text($('#decompteSalaireGSON\\.periodeFin').val());
				var $ligneSuprimmee = $("#ligneSuprimmee");
				if(data.decompteSalaireGSON.ligneSupprimee){
					$ligneSuprimmee.show();
				}else{
					$ligneSuprimmee.hide();
				}
				
				if(data.decompteSalaireGSON.quittancer){
					$ligneSuprimmee.hide();
				}
								
				GLO.isMensuel = data.decompteSalaireGSON.isMensuel;
				
				if (data.decompteSalaireGSON.idPosteTravail) {
					GLO.idPosteTravail = data.decompteSalaireGSON.idPosteTravail;
					globazGlobal.TAUXEBU = data.decompteSalaireGSON.tauxSaisieEbu;
				} else {
					GLO.idPosteTravail = null;
				}
				GLO.idNouveauTravailleur = data.decompteSalaireGSON.correlationId;
				globazGlobal.isNouveauTravailleur = data.decompteSalaireGSON.nouveauTravailleur;
				
				handleEBusinessPart(data);
				
				GLO.navigation.enableDisableNavigationButton(data.decompteSalaireGSON.first, data.decompteSalaireGSON.last);
				$('.lastModification').html(' Update: ' + data.decompteSalaireGSON.spy);
				globazGlobal.ID_LIGNE_DECOMPTE = $('#decompteSalaireGSON\\.idDecompteSalaire').val();
				zoneAjax.selectedEntityId = globazGlobal.ID_LIGNE_DECOMPTE;
				this.isEntityLoaded = true;
	    		GLO.absences.load(data);
	    		GLO.cotisations.load();
	    		GLO.historiqueSalaire.load();
	    		GLO.handleNote();
	    		GLO.hasDroitAF(data);
				GLO.decompteSalaire.isDeleted = false;
				
			}
		},
		onError: function (data) {
			GLO.absences.edit();
			if(!this.selectedEntityId) {
				GLO.decompteSalaire.$btnNext.prop("disabled", true);
				GLO.decompteSalaire.$btnPrevious.prop("disabled", true);
				GLO.decompteSalaire.$passerSuivant.prop("disabled", true);
			}
		},		
		getParametres : function() {
			var parametres = GLO.absences.save();
			parametres.isPasserSuivant = GLO.navigation.isPasserAuSuivant;
			parametres.cotisationMustReload = GLO.decompteSalaire.cotisationMustReload;
			parametres.idPosteTravail = GLO.idPosteTravail;
			return parametres;
		}
	});
});

GLO.hasDroitAF = function(data) {
	var options = {
		serviceClassName:globazGlobal.decompteSalaireViewService,
		serviceMethodName:'hasDroitActifAF',
		parametres:data.decompteSalaireGSON.idTiersTravailleur + "," + data.decompteSalaireGSON.periodeDebut,
		callBack:function (hasDroit) {
			var $tableDroitActif = $('#tableDroitActif'); 
			if(hasDroit) {
				$tableDroitActif.css('display','block');
			} else {
				$tableDroitActif.css('display','none');
			}
		}
	};
	vulpeculaUtils.lancementService(options);
}

GLO.loadLibelleCodeErreur = function(data, list) {
	var taux = parseFloat(globazGlobal.TAUXEBU);
	taux = taux.toFixed(2);
	var options = {
		serviceClassName:globazGlobal.decompteSalaireViewService,
		serviceMethodName:'loadCodesErreur',
		parametres:data+","+taux,
		callBack:function (tabErreur) {
			$("#listCodeErreurs").html($("#listCodeErreurs").html() + "<li>" + tabErreur);
		}
	};
	vulpeculaUtils.lancementService(options);
}

GLO.handleNote = function() {
		if(GLO.idPosteTravail && GLO.idPosteTravail.length > 0) {
			$('.descriptionTravailleur').show();
			$('.descriptionNouveauTravailleur').hide();
		} else {
			$('.descriptionTravailleur').hide();
			
			GLO.handleNoteNouveauTravailleur();
			return;
		}
			
		//Recherche des notes et des informations relatives au travailleur
		if (GLO.idPosteTravail == null) {
			return;
		}
		var options = {
			serviceClassName:globazGlobal.travailleurViewService,
			serviceMethodName:'getInformationsTravailleur',
			parametres:GLO.idPosteTravail,
			callBack:function (infos) {
				$('#titlePosteTravail').prop("title", "id : "+GLO.idPosteTravail);
				$('#infoDateNaissance').html(infos.dateNaissance);
				$('#infoGenreSalaire').html(infos.genreSalaire);
				$('#infoQualification').html(infos.qualification);
				if (infos.dateFinPoste.length > 0) {
					$('#infoDateFinPoste').css("color","red").css("font-weight","bold").html("Date de fin au "+infos.dateFinPoste);
				} else {
					$('#infoDateFinPoste').css("color","black").css("font-weight","normal").html("Actif");
				}
				$('#infoNote').html(infos.note);
			}
	};
	vulpeculaUtils.lancementService(options);
}

GLO.handleNoteNouveauTravailleur = function() {
	if (globazGlobal.isNouveauTravailleur && GLO.idNouveauTravailleur && globazGlobal.ID_LIGNE_DECOMPTE) {
		$('.descriptionNouveauTravailleur').show();
	} else {
		$('.descriptionNouveauTravailleur').hide();
		return;
	}
		
	var options = {
		serviceClassName:globazGlobal.travailleurViewService,
		serviceMethodName:'getInformationsNouveauTravailleur',
		parametres:GLO.idNouveauTravailleur,
		callBack:function (infos) {
			$('#titleCorrelationId').prop('title', 'id : ' + GLO.idNouveauTravailleur);
			$('#infoTDateNaissance').html(infos.dateNaissance);
			$('#infoTGenreSalaire').html(infos.genreSalaire);
			$('#infoTQualification').html(infos.qualification);
			$('#infoTNom').html(infos.nom);
		}
	};
	vulpeculaUtils.lancementService(options);
}
;