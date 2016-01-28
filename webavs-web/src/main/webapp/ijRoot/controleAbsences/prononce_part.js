var _prononceId = "";
var _prononceSelectionne = "";

function initZoneAjaxPrononceIJAI() {

	var ajaxObj = defaultTableAjax.init({
		s_container : '#zoneAjaxPrononceIJAI',
		s_actionAjax : globazGlobal.actionAjaxPrononce,

		getParametresForFind : function() {
			return {
				'searchModel.forIdTiers' : globazGlobal.idTiers
			};
		},

		getParametres : function() {
			return {
				'idPrononceSelectionne' : _prononceId,
				'prononceSelectionne' : _prononceSelectionne
			};			
		},
		
//		init : function() {
//			this.list();
//		}
		init : function() {
			this.capage(5, [ 10, 20, 30, 50, 100 ]);
		}
	});

	var $boutonSelectionnerPrononces = $('#boutonSelectionnerPrononces');
	$boutonSelectionnerPrononces.click(function() {
		managePrononce();
	});
	
	// Sauvegarde du prononce en cas de changement d'état de la checkbox
	$('#zoneAjaxPrononceIJAI').bind(
			eventConstant.AJAX_FIND_COMPLETE, function() {
				managePrononce();
				// Sauvegarde du prononce en cas de changement d'état de la checkbox
				$('.showDetailPrononce').click(function() {
					savePrononce(this);					
				});
			});

	function savePrononce(checkBox){
		if ($(checkBox).is(':checked')) {
			_prononceSelectionne = "true";
		} else {
			   _prononceSelectionne = "false";
		}
		
		var td = $(checkBox).parent();
		var tr = $(td).parent();
		var id = tr.attr("identity");
		_prononceId = id;
		ajaxObj[0].ajaxUpdateEntity(id);
	}
}

var flag = false;

function managePrononce() {
	var $boutonSelectionnerPrononces = $('#boutonSelectionnerPrononces');
	var rows = $('#bodyPrononce').children();
	// Si le flag est à true, on affiche toute les lignes
	if (flag) {
		$boutonSelectionnerPrononces.attr('value', globazGlobal.actionAjaxLabelBoutonValiderSelection);
		// Affichage des lignes
		for ( var ctr = 0; ctr < rows.length; ctr++) {
			var tr = $(rows[ctr]);
			$(tr).css({
				display : 'table-row'
			});
		}

		// Affichage de la colonne de sélection
		for ( var ctr = 0; ctr < rows.length; ctr++) {
			var tr = $(rows[ctr]);
			var tds = $(tr).children();
			$(tds[5]).css({
				display : 'table-cell'
			});
		}
		$('#colonneSelectionPrononce').css({ display : 'table-cell'});
	}
	// Si le flag est a false, on masque les lignes non checked
	else {

		$boutonSelectionnerPrononces.attr('value', globazGlobal.actionAjaxLabelBoutonSelectionnerPrononces);
		for (ctr = 0; ctr < rows.length; ctr++) {
			var tr = $(rows[ctr]);
			var tds = $(tr).children();
			var checkBox = $(tds[5]).children();
			if ($(checkBox).is('input')) {
				if ($(checkBox).is(':checked')) {
					$(tr).css({
						display : 'table-row'
					});
				} else {
					$(tr).css({
						display : 'none'
					});
				}
			} else {
				// WTF ?? 
			}
		}

		// Masquage de la colonne de sélection
		for (ctr = 0; ctr < rows.length; ctr++) {
			var tr = $(rows[ctr]);
			var tds = $(tr).children();
			$(tds[5]).css({
				display : 'none'
			});
		}
		$('#colonneSelectionPrononce').css({ display : 'none'});
	}
	flag = !flag;
}

$(document).ready(function() {
	initZoneAjaxPrononceIJAI();
});
