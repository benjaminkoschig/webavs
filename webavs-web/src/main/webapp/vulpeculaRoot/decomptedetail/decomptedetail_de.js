globazGlobal.updateMode = false;

globazGlobal.cotisations = (function() {
	function load() {
		$.ajax({
			data : {
				idDecompte : globazGlobal.idDecompte,
				userAction : "vulpecula.decompte.cotisationsAjax.afficherAJAX"
			},
			success : function(data) {
				$('#cotisation').empty().html(data);
			}
		});
	}

	return {
		load : load
	};
})();

globazGlobal.totalSalaires = (function() {
	function load() {
		$.ajax({
					data : {
						idDecompte : globazGlobal.idDecompte,
						userAction : "vulpecula.decompte.totalSalairesAjax.afficherAJAX"
					},
					success : function(data) {
						$('#totalSalaires').empty().html(data);
					}
				});
	}

	return {
		load : load
	};
})();

function add() {
	document.forms[0].elements('userAction').value = "vulpecula.decompte.decompte.afficher&_method=_add";
}

function upd() {
	var $dateReception = $("#date_reception");
	if ($dateReception.val() && $dateReception.val().length > 0) {
		$dateReception.disabled = true;
	}
	globazGlobal.updateMode = true;
}

function validate() {
	state = true;
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value = "vulpecula.decomptedetail.decomptedetail.ajouter";
	} else {
		document.forms[0].elements('userAction').value = "vulpecula.decomptedetail.decomptedetail.modifier";
	}
	return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value = "back";
	} else {
		document.forms[0].elements('userAction').value = "vulpecula.decomptedetail.decomptedetail.afficher";
		document.forms[0].elements('selectedId').value = $('#idDecompte').val();
	}
}

function del() {
	if (window.confirm(globazGlobal.messageSuppression)) {
		document.forms[0].elements('userAction').value = "vulpecula.decomptedetail.decomptedetail.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

$(function() {
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
	// Si on a soumis le decompte pour un controle, la variable est settee a
	// true par l'Action (FWDefaultServlet) et on lance directement la phase de
	// controle.
	if (globazGlobal.echecEBusinessControler) {
		window
				.confirm("Tous les decomptes salaires doivent être quittancés avant le contrôle du décompte !");
	} else if (globazGlobal.echecControler) {
		afficheDialogControle(globazGlobal.idDecompte);
	}
	// Sinon, si le decompte est controlable, on passe en mode edition
	else if (globazGlobal.isControlable) {
		action(UPDATE);
		upd();
		setTimeout(function() {
			if ($('#pasDeControle').is(':checked')) {
				$("#montantControle").val("0.00");
				$("#pasDeControle").prop("checked", false);
			}
			$("#montantControle").select().focus();
		}, 100);
	}

	globazGlobal.cotisations.load();

	globazGlobal.totalSalaires.load();

	var tableAjax;
	$("#tabs").tabs();
	defaultTableAjax
			.init({
				s_actionAjax : "vulpecula.decomptedetail.ligneDecompteAjax",
				userActionDetail : "vulpecula?userAction=vulpecula.decomptedetail.ligneDecompteAjax.afficherAJAX&selectedId=",
				s_search : '.areaSearch',
				s_selector : ".",
				init : function() {
					this.capage(20, [ 10, 20, 30, 50, 100 ]);
					this.addSearch();

					var that = this;
					$('.areaTable input').change(function() {
						that.ajaxFind();
					});

					this.mainContainer.on('click', '.supprimerSalaire',
							function() {
								var idLigneDecompte = $(this).closest('tr')
										.attr('idEntity');
								that.ajaxDeleteEntity({
									idLigneDecompte : idLigneDecompte
								});
							});

					this.onDeleteAjax = function() {
						globazGlobal.cotisations.load();
						globazGlobal.totalSalaires.load();
					};
				},
				getParametresForFind : function() {
					var map = {
						idDecompte : globazGlobal.idDecompte,
						idPosteTravail : $('#idPosteTravail').val(),
						nomTravailleur : $('#nomTravailleur').val()
					};
					return map;
				}
			});

	if (globazGlobal.isEdition) {
		action(UPDATE);
		upd();
		setTimeout(function() {
			$("#montantControle").select().focus();
		}, 100);
	}

	$("#pasDeControle").change(function() {
		$("#montantControle").val("-1");
		controler(globazGlobal.idDecompte);
	});
});

function reaffichePage() {
	// On reaffiche la page dans un premier temps, sinon il faut juste mettre a
	// jour l'etat et l'historique
	document.forms[0].elements('userAction').value = "vulpecula.decomptedetail.decomptedetail.afficher";
	document.forms[0].elements('selectedId').value = $('#idDecompte').val();
	document.forms[0].submit();
}

function modifierControle(idDecompte) {
	var o_options = {
		serviceClassName : globazGlobal.decompteService,
		serviceMethodName : 'modifierControleErrone',
		parametres : idDecompte,
		callBack : function() {
			reaffichePage();
		}
	};
	globazNotation.readwidget.options = o_options;
	globazNotation.readwidget.read();
}

function rectifierControle(idDecompte) {
	var textAreaValue = " ";
	if ($("#remarqueRectificatif").val()) {
		textAreaValue = $("#remarqueRectificatif").val();
	}

	var o_rectifier = {
		idDecompte : idDecompte,
		remarque : textAreaValue
	};

	var o_options = {
		serviceClassName : globazGlobal.decompteViewService,
		serviceMethodName : 'rectifier',
		parametres : JSON.stringify(o_rectifier),
		callBack : function() {
			reaffichePage();
		}
	};
	vulpeculaUtils.lancementServicePost(o_options);
}

function afficheDialogControle(idDecompte) {
	// On affiche un dialog qui permet a l'utilisateur de rectifier ou modifier
	var dialogConfirm = $("#dialog-confirm");
	dialogConfirm.dialog({
		resizable : false,
		height : 900,
		width : 450,
		modal : false,
		closeOnEscape : true,

		buttons : [ {
			text : globazGlobal.libelleBoutonModifier,
			click : function() {
				modifierControle(idDecompte);
			}
		}, {
			text : globazGlobal.libelleBoutonRectifier,
			click : function() {
				rectifierControle(idDecompte);
			}
		} ]
	});
}

function devalider(idDecompte) {
	var o_options = {
		serviceClassName : globazGlobal.decompteViewService,
		serviceMethodName : 'devalider',
		parametres : idDecompte,
		callBack : function(data) {
			reaffichePage();
		}
	};

	if (window.confirm(globazGlobal.libelleDialogDevalider)) {
		globazNotation.readwidget.options = o_options;
		globazNotation.readwidget.read();
	}
	;
}

function controler(idDecompte) {
	$("#btnControler").prop('disabled', true);
	if (globazGlobal.updateMode) {
		document.forms[0].elements('userAction').value = "vulpecula.decomptedetail.decomptedetail.modifier";
		document.forms[0].elements('controler').value = 'on';
		action(COMMIT);
	} else {
		var o_controleOptions = {
			idDecompte : globazGlobal.idDecompte,
			pasDeControle : $("#pasDeControle").is(':checked')
		};
		var o_options = {
			serviceClassName : globazGlobal.decompteViewService,
			serviceMethodName : 'controler',
			parametres : JSON.stringify(o_controleOptions),
			callBack : function(data) {
				if (data.isErreurEBusiness == true) {
					confirm("Tous les decomptes salaires doivent être quittancés avant le contrôle du décompte !");
				} else if (data.isValid == false) {
					$("#dialogMontantControle").text(
							globazNotation.utilsFormatter.formatStringToAmout(
									data.montantControle, 2));
					$("#dialogTotalSalaires").text(
							globazNotation.utilsFormatter.formatStringToAmout(
									data.totalContributions, 2));
					$("#dialogDifference").text(
							globazNotation.utilsFormatter.formatStringToAmout(
									data.difference, 2));
					$("#remarqueRectificatif").text(data.remarqueRectification);
					afficheDialogControle(idDecompte);
				} else {
					reaffichePage();
				}
			}
		};
		globazNotation.readwidget.options = o_options;
		globazNotation.readwidget.read();
	}

}

function annuler(idDecompte) {
	var o_options = {
		serviceClassName : globazGlobal.decompteService,
		serviceMethodName : 'annuler',
		parametres : idDecompte,
		callBack : function(data) {
			reaffichePage();
		}
	};

	if (window.confirm(globazGlobal.libelleDialogAnnuler)) {
		globazNotation.readwidget.options = o_options;
		globazNotation.readwidget.read();
	};
}