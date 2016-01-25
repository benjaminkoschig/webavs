var decomptesSearch;
var init = true;

$(function() {
	$('#boutonSommation').click(function() {
		if (confirm(globazGlobal.libelleConfirmeSommation)) { 
			var options = {
					serviceClassName:globazGlobal.decompteService,
					serviceMethodName:'genererSommationManuel',
					parametres:' ',
					callBack:function () {
						
					}
			};
			vulpeculaUtils.lancementService(options);
	    }
	});
	
	$('#btnRechercher').click(function() {
		find();
	});
	
	globazGlobal.etats.init();
	
	//On délaie de quelques ms afin que le moteur de notation ait fini son travail
	setTimeout(function() {
		$('#searchModel\\.forId').focus();
		
		$(".areaSearch :input").change(function() {
			find.call(this);		
		});
	}, 100);
	
	function find() {
		var result = false;
		$(".areaSearch :input").each( function(n,element){if ($(element).val()!='' &&  $(element).val()!='68012007') {result = true;} } );
		if (result) {
			$element = $(this);
			if($element.attr('id')=='searchModel.likeNoAffilie') {
				vulpeculaUtils.formatNoAffilie($element);
			}
			decomptesSearch.ajaxFind();
		} else {
			alert("Veuillez saisir des critères de recherche.");
		}		
	}
	
	defaultTableAjaxList.init({
		s_actionAjax : globazGlobal.ACTION_AJAX,
		userActionDetail : "vulpecula?userAction=vulpecula.decomptesalaire.afficher&provenance=saisieRapide&idDecompte=",
		s_search : '.areaSearch',
		s_selector: "#",
		init : function() {
			decomptesSearch = this;
			this.capage(20, [ 10, 20, 30, 50, 100 ], false);
			this.addSearch();
		},
		
		getParametresForFind: function() {
			$search = $('.areaSearch');
			m_map = ajaxUtils.createMapForSendData($search, this.s_selector);
			//Conversion des champs input dates en champ de recherche pour les complexModel¨
			var INPUT_DATE_DE = 'searchModel.forDateDe';
			var INPUT_DATE_AU = 'searchModel.forDateAu';
			
			var forDateDe = m_map[INPUT_DATE_DE];
			var forDateAu = m_map[INPUT_DATE_AU];
			
			var forDateDeMap = forDateDe.split('.');
			var forDateAuMap = forDateAu.split('.');
			
			if(forDateDeMap.length > 1) {
				var forDateDeToSend = forDateDeMap[1]+forDateDeMap[0];
				m_map[INPUT_DATE_DE] = forDateDeToSend;
			}
			if(forDateAuMap.length > 1) {
				var forDateAuToSend = forDateAuMap[1]+forDateAuMap[0];
				m_map[INPUT_DATE_AU] = forDateAuToSend;
			}
			
			return m_map;
		},
		b_changeStack : true
	});
});

globazGlobal.etats = (function () {
	function init() {
		var $etat = $('#searchModel\\.inEtats');
		var $etatTaxation = $('#searchModel\\.forEtatTaxation');
		
		onEtatChanged();
		$etat.change(function () {
			onEtatChanged();
		});
		
		function onEtatChanged() {
			if($etat.val()==globazGlobal.csTaxationOffice) {
				$etatTaxation.show();
			}
			else {
				$etatTaxation.hide();
				setEmptyToEtatTaxation();
			}				
		}
		
		function setEmptyToEtatTaxation() {
			$etatTaxation.val('');
		}
	}
	
	return {init: init};
})();
