//fonctions de bases � red�finir
function devalider() { 
	var o_options = {
		serviceClassName: globazGlobal.taxationService,
		serviceMethodName: 'devalider',
		parametres:globazGlobal.idTaxationOffice ,
		callBack:function() {
			reaffichePage();
		}
	};
		globazNotation.readwidget.options = o_options;
		globazNotation.readwidget.read();
}

function annuler() {
	var o_options = {
			serviceClassName: globazGlobal.taxationService,
			serviceMethodName: 'annuler',
			parametres:globazGlobal.idTaxationOffice,
			callBack:function() {
				reaffichePage();
			}
		};
			globazNotation.readwidget.options = o_options;
			globazNotation.readwidget.read();	
}

function imprimer() { 
	//Affiche une boite de dialogue quand le bouton "imprimer" est cliqu�
	$(function() {
	    $( "#dialog" ).dialog({
	      autoOpen: false,
	      show: {
	        effect: "none",
	        duration: 100
	      },
	      hide: {
	        effect: "none",
	        duration: 100
	      }
	    });
	 
	    $( "#btnPrt" ).click(function() {
	      $( "#dialog" ).dialog( "open" );
	    });
	});
	$(function() {
	    var progressbar = $( "#progressbar" );
	 
	    progressbar.progressbar({
	      value: false,
	      complete: function() {
	    	  $('#dialog').dialog('close');
	      }
	    });
	 
	    function progress() {
	      var val = progressbar.progressbar( "value" ) || 0;
	 
	      progressbar.progressbar( "value", val + 10 );
	 
	      if ( val < 99 ) {
	        setTimeout( progress, 80 );
	      }
	    }
	 
	    setTimeout( progress, 50 );
	  });
	/*FIN DE LA BOITE DE DIALOGUE*/
	var o_options = {
		serviceClassName: globazGlobal.taxationService,
		serviceMethodName: 'imprimer',
		parametres:globazGlobal.idTaxationOffice ,
		callBack:function() {
			//reaffichePage();
		}
	};
		globazNotation.readwidget.options = o_options;
		globazNotation.readwidget.read();
}

function reaffichePage(){
	location.href= "vulpecula?userAction=vulpecula.taxationoffice.afficher&idDecompte="+globazGlobal.idDecompte;
}


function add () {
	document.forms[0].elements('userAction').value="vulpecula.taxationoffice.taxationoffice.afficher&_method=_add";
}

function upd() {
}

function validate() {
	state = true;
	 if (document.forms[0].elements('_method').value == "add") {
	     document.forms[0].elements('userAction').value="vulpecula.taxationoffice.taxationoffice.ajouter";
	 } else {
	     document.forms[0].elements('userAction').value="vulpecula.taxationoffice.taxationoffice.modifier";
	 	var lignesTaxation = globazGlobal.lignesTaxation.retrieve();
		var lignesTaxationGSON = JSON.stringify(lignesTaxation);
		$("#lignesTaxation").val(lignesTaxationGSON);
	 }
	 return state;
}

function cancel() {
	 if (document.forms[0].elements('_method').value == "add"){
		 document.forms[0].elements('userAction').value="back"; 
	 }else{
		 document.forms[0].elements('userAction').value="vulpecula.taxationoffice.taxationoffice.afficher";
	     document.forms[0].elements('selectedId').value=$('#idDecompte').val();
	 }
}
	
function del() {
}

function init() {
	
}

//chargement du dom jquery
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
	
	globazGlobal.lignesTaxation.init();
	
	$("#designationPassageFacturation").change(function() {
		if($('#designationPassageFacturation').val()=='0' || $('#designationPassageFacturation').val()==''){
			$('#passageFacturation').val('0');
		}
	});
});

globazGlobal.lignesTaxation = {
		changed : false,
		$masse : null,
		$montant : null,
		init : function(){
			var that = this;
			
			this.$masse = $(document).find('.masse');
			this.$montant = $(document).find('.montant_cotisation');
			
			this.$masse.change(function() {
				this.changed = true;
			});
			this.$montant.change(function() {
				this.changed = true;
			});
			
			this.$masse.focusout(function () {
				if(this.changed) {
					var $this = $(this);
					var taux = $this.closest("tr").find('.taux').text();
					var $montantS = $this.closest("tr").find('.montant_cotisation');
					var masse = roundToFiveCentimes(globazNotation.utilsFormatter.amountTofloat($this.val()));
					$this.val(masse);
					var newMontant =roundToFiveCentimes(masse * taux / 100);
					
					$montantS.val(globazNotation.utilsFormatter.formatStringToAmout(newMontant));
					
					that.calculerTotaux();
					that.changed = false;
				}
			});
			
			this.$montant.focusout(function () {
				if(this.changed) {
					var $this = $(this);
					var montant = roundToFiveCentimes(globazNotation.utilsFormatter.amountTofloat($this.val()));
					var taux = $this.closest("tr").find('.taux').text();
					var $masseS = $this.closest("tr").find('.masse');
					var newMasse = roundToFiveCentimes(montant * 100 / taux);
					$this.val(montant)
					
					$masseS.val(globazNotation.utilsFormatter.formatStringToAmout(newMasse));	
					
					that.calculerTotaux();
					that.changed = false;
				}
			});
			
		},
		retrieve : function(){
			var lignes = [];
			$('.ligneTaxation').each(function() {
				var $this = $(this);
				var $id = $this.find('.id');
				
				var taux = $this.closest("tr").find('.masse').val();
				var montant = $this.closest("tr").find('.montant_cotisation').val();
				
				ligne = {
						id : $id.val(),
						masse : globazNotation.utilsFormatter.amountTofloat(taux),
						montant : globazNotation.utilsFormatter.amountTofloat(montant)
				};
				lignes.push(ligne);
			});
			return lignes;
		},
		calculerTotaux : function() {
			var $totalMasse = $('#totalMasse');
			var $totalMontant = $('#totalMontant');
			
			var sum = 0;
			$.each(this.$montant, function(index, montant) {
				sum += parseFloat($(montant).val());
			});
			$totalMontant.val(globazNotation.utilsFormatter.formatStringToAmout(sum));
			
			var sum = 0;
			$.each(this.$masse, function(index, masse) {
				var masseAcc = globazNotation.utilsFormatter.amountTofloat($(masse).val());
				sum += parseFloat(masseAcc);
			});
			$totalMasse.val(globazNotation.utilsFormatter.formatStringToAmout(sum));	
		}
}