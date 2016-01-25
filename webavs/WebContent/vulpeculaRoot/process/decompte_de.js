	$(function () {
		var PERIODICITE_MENSUELLE_TRIMESTRIELLE = "MENSUELLE_TRIMESTRIELLE";
		var PERIODICITE_ANNUELLE = "ANNUELLE";
		
		var classError = "errorColor";
		var isValueKo = "isValueKo";
		
		var $idConvention = $('#CONVENTIONS');
		var $employeurNumero = $('#employeurNumero');
		var $employeurNumeroLibelle = $('#employeurNumeroLibelle');
		var $periodiciteMensuelleTrimestrielle = $('#periodeMensuelleTrimestrielle');
		var $periodiciteAnnuelle = $('#periodeAnnuelle');
		var $annuelle = $('#annuelle');
		var $typeDecompte = $('#TYPE_DECOMPTE');
		
		$periodiciteAnnuelle.hide();
		var $periodicite = $('.typePeriodicite');

		// Dans le cas où l'on entre dans le détail, il faut bouger manuellement la case à cocher.
		if(globazGlobal.isAnnuelle) {
			$annuelle.prop('checked', true);
			showAnnuelleView();
		} else {
			showMensuelleTrimestrielleView();			
		}
		
		$typeDecompte.change(function() {
			var $this = $(this);
			if(globazGlobal.csComplementaire==$this.val()) {
				$periodicite.prop('disabled','disabled');
				$('#annuelle').prop('checked',true).change();
			} else {
				$periodicite.removeProp('disabled');
			}
		});
		
		//Si la convention est set, alors on ne laisse pas la possibilité de sélectionner un employeur
		$idConvention.change(function() {
			if(!isAffilieEmpty()) {
				$employeurNumeroLibelle.removeClass(classError);
				$employeurNumeroLibelle.prop('disabled',true);
			} else if(isAffilieEmpty()) {
				$employeurNumeroLibelle.addClass(classError);
				$employeurNumeroLibelle.prop('disabled',false);
			}
		});
		
		//On désactive les champs de période en fonction de la périodicite choisie
		$periodicite.change(function() {
			if(isPeriodiciteAnnuelle()) {
				showAnnuelleView();
			} else {
				showMensuelleTrimestrielleView();
			}
		});
		
		//Si l'employeur est saisi, alors on ne peut saisir la convention
		$employeurNumero.change(function() {
			if(!isIdConventionEmpty()) {
				$idConvention.removeClass(isValueKo);
				$idConvention.attr('disabled',true);
			}
			else if(isIdConventionEmpty()) {
				$idConvention.addClass(isValueKo);
				$idConvention.attr('disabled', false);
			}
		});
		
		function isPeriodiciteAnnuelle() {
			var periodicite = $('.typePeriodicite:checked').val();
			return PERIODICITE_ANNUELLE===periodicite;
		}
		
		function isIdConventionEmpty() {
			return $employeurNumero.val()==='';
		}
		
		function isAffilieEmpty() {
			return !$idConvention.val();
		}
		
		function handlePeriodicite() {
			
		}
		
		function showAnnuelleView() {
			$periodiciteMensuelleTrimestrielle.hide();
			$periodiciteAnnuelle.show();
		}
		
		function showMensuelleTrimestrielleView() {
			$periodiciteMensuelleTrimestrielle.show();
			$periodiciteAnnuelle.hide();
		}
	});