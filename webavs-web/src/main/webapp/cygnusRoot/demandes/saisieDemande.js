/**
 *author jje
 */

var demandeAjax = {
		
		init:function(){
			this.bindEvent();
			this.getInfo();
		},
		
		bindEvent:function(){
			var that=this;
			
			if (typeof($('[name="dateDebutTraitement"]').val()) != 'undefined'){
				$('[name="dateDebutTraitement"]').blur(function(){
					that.getInfo();
				});
			}
			
			if (typeof($('[name="dateFacture"]').val()) != 'undefined'){
				$('[name="dateFacture"]').blur(function(){
					that.getInfo();
				});
			}
			
			if (typeof($('[name="dateDecisionOAI"]').val()) != 'undefined'){
				$('[name="dateDecisionOAI"]').blur(function(){
					that.getInfo();
				});
			}
			
		},
		
		getInfo:function(){
		
			var that = this;
			var options = {
				serviceClassName: 'globaz.cygnus.services.saisieDemande.RFRechercherDescriptionsQdsMembresFamillesService',
				serviceMethodName:'rechercher',
				parametres:that.getParameters(),
				wantInitThreadContext: true,
				callBack: function (data){
					that.displayInfo(data);
				}
			};
			
			globazNotation.readwidget.options = options;
			globazNotation.readwidget.read();
		},
		
		getParameters:function(){

			var idTiersSelectionneVal = $('input[type="radio"][name="membreFamille"]:checked').val();
			var idTiersRequerant;

			if(typeof(idTiersSelectionneVal)=='undefined'){
				idTiersRequerant = $('#idTiers').val(); 
			}else{
				idTiersRequerant = idTiersSelectionneVal;
				idAdressePaiementDemande = idTiersSelectionneVal;
				$('#idTiers').val(idTiersSelectionneVal);
			}

			var dateFactureDemande;
			if (typeof($('[name="dateFacture"]').val()) != 'undefined'){
				dateFactureDemande = $('[name="dateFacture"]').val();
			}else if (typeof($('[name="dateDecisionOAI"]').val()) != 'undefined'){
				dateFactureDemande = $('[name="dateDecisionOAI"]').val();
			}else if (typeof($('[name="dateDecompte"]').val()) != 'undefined'){
				dateFactureDemande = $('[name="dateDecompte"]').val();
			}
			
			var dateDebutTraitementDemande;
			if (typeof($('[name="dateDebutTraitement"]').val()) != 'undefined'){
				dateDebutTraitementDemande = $('[name="dateDebutTraitement"]').val();
			}
			
			var $method = $('[name="_method"]');			
			var idQdPrincipale = $('#idQdPrincipale').val();
			var codeTypeDeSoinList = $('[name="codeTypeDeSoinList"]').val();
			var codeSousTypeDeSoinList = $('[name="codeSousTypeDeSoinList"]').val();
			var servletContextSd = 'testCont';
			var mainservletPatSd = 'testMain';

			var paramsDemande=this.changeVal(idTiersRequerant)+','+this.changeVal(dateFactureDemande)+','
		      +this.changeVal(dateDebutTraitementDemande)+','+this.changeVal(idQdPrincipale)+','
		      +this.changeVal(codeTypeDeSoinList)+','+this.changeVal(codeSousTypeDeSoinList)+','+servletContextSd+','
		      +mainservletPatSd+','+this.changeVal(idAdressePaiementDemande)+','+this.changeVal($method.val());

			return paramsDemande;
		},
		
		changeVal:function (s_val) {
			if(typeof(s_val)!='undefined'){
				if(s_val.length>0){
					return s_val;
				}else{
					return "0";
				}
			}else{
				return "0";
			}
		},
		
		displayInfo:function(data){
			$('#idAdressePaiementDemande').val(data.idTiers);
			var $adresse = $('.adresseAffichee .adresse');
			$adresse.empty().append(data.adressePaiement);
			
			$('#descQd').html(data.descriptionQd);
			$('#descFamilleCc').html(data.membresFamilleCC);
			$('[name="idTiers"]').val($("input[type=radio][name=membreFamille]:checked").attr('value'));
			
			//si l'état de la demande est différent d'enregistré on ne peut plus modifier le bénéficiaire
			var csEtatDemande = $('[name="csEtatDemande"]').val();
			if ($('[name="isAfficherDetail"]').val() == 'true'){	
				$("[name*=membreFamille]").attr('disabled', true);				
				if (csEtatDemande != $("#csEtatEnregistre").val()){
					$("[name*=membreFamille]").attr('readonly', true);
				}
			}
		}
};

var demandeUtils = {
		
		onClickMembreFamille:function(){
			$('[name="idTiers"]').val($("input[type=radio][name=membreFamille]:checked").attr('value'));
			demandeAjax.getInfo();
		},
		
		ajoutMontantCurrentMotifsRefus:function(elem){
			currentMotifsRefus[$(elem).attr('name').substr(23,8)].montant= demandeUtils.formatMnt($(elem).attr('value'));
		},

		formatMnt:function(valueStr) {
		
			var decote=/'/g;
			valueStr = valueStr.replace(decote,"");
			if (isNaN(valueStr))
				valueStr="";
			else
				valueStr = (new Number(valueStr)).toFixed(2);
			
			var i = valueStr.indexOf(".");
			var j = 0;
			while (i>0) {
				if (j%3==0 && j!=0 && valueStr.charAt(i-1)!='-') {
					valueStr = valueStr.substring(0,i) +"'"+ valueStr.substring(i);
					j++;
					i--;
				}
				i--;
				j++;
			}
		
			return valueStr;
		},
		
		updateMntTotal:function(){
		
			var decote=/'/g;	
			var total = parseFloat(0);
			
			if ($('[name="hasMotifRefusDemande"]').val() == "false"){
				
				var listMontants = $(".montant");
				$.each(listMontants,function (index, element) {
						
						if (element.value != null){
							if (element.name != "montantMensuel" && element.name != "montantAssocieDevis" && element.name != "nombreKilometres" && element.name != "prixKilometre"){
								if (element.name == "montantFacture" || element.name == "montantDecompte" || element.name == "montantFacture44"){
									total = total + parseFloat(element.value.replace(decote,""));
								}else{
									total = total - parseFloat(element.value.replace(decote,""));
								}
							}
						}
				});
			}
			
			var mntResiduelElem = $("#idMontantAPayer");
			mntResiduelElem.html('<b><i>'+ demandeUtils.formatMnt(parseFloat(total).toFixed(2)) +'</i></b>');
			
			$('[name="montantAPayer"]').val(demandeUtils.formatMnt(parseFloat(total).toFixed(2)));
			
		},
		
		onChangeClasseMontant:function(elem){
			$(elem).val(demandeUtils.formatMnt($(elem).val()));
			demandeUtils.updateMntTotal();
		},

		onChangeMontantOai:function(elem){
			var decote=/'/g;
			
			
		
			
			$('[name="montantFacture44"]').val(
			demandeUtils.formatMnt(parseFloat(($(elem).val().replace(decote,"")/3)*4).toFixed(2)));
			
			
		},
		
		onChangeMontantMensuel:function(){
			
			//var onChangeOk = false;
			var imputerSurDecompte = false;
			
			/*if ($("#typeDemande").val() == $("#csTypeDemandeDefaut").val()){
				onChangeOk = true;
			}else{*/
				if (typeof($("#idIsPP").val() != 'undefined') && $("#idIsPP").prop("checked")){
					//onChangeOk = true;
					imputerSurDecompte=true;
				}
			/*}*/
			
			//if (onChangeOk){
			
				var decote=/'/g;
				var dateDebutTraitementString = {};
				var dateFinTraitementString = {};
				
				if (typeof(document.getElementsByName("dateDebutTraitement")[0]) != 'undefined'){
					//fieldFormat(document.getElementsByName("dateDebutTraitement")[0],"CALENDAR",null);
					//fieldFormat(document.getElementsByName("dateFinTraitement")[0],"CALENDAR",null);
					dateDebutTraitementString = document.getElementsByName("dateDebutTraitement")[0].value;
					dateFinTraitementString = document.getElementsByName("dateFinTraitement")[0].value;
				}else{
					//fieldFormat(document.getElementById("dateDebutTraitementIdMMAAAA"),"CALENDAR",null);
					//fieldFormat(document.getElementById("dateFinTraitementIdMMAAAA"),"CALENDAR",null);
					dateDebutTraitementString = document.getElementById("dateDebutTraitementIdMMAAAA").value;
					dateFinTraitementString = document.getElementById("dateFinTraitementIdMMAAAA").value;
				}
				
				if (dateDebutTraitementString.length == 7){
					
					var regEx=/(\d{1,2})\.(\d{2,4})/;
					var nbMoisPeriodeTraitement;
					
					// Si l'année de début et de fin sont égale
					if (dateFinTraitementString != "" && regEx.exec(dateDebutTraitementString)[2] == regEx.exec(dateFinTraitementString)[2]){
						nbMoisPeriodeTraitement = regEx.exec(dateFinTraitementString)[1]-regEx.exec(dateDebutTraitementString)[1]+1;
					}else{
						nbMoisPeriodeTraitement = 12-regEx.exec(dateDebutTraitementString)[1]+1;
					}
	
					var nouveauMontantFacture = nbMoisPeriodeTraitement * $('[name="montantMensuel"]').val().replace(decote,"");
					
					if (imputerSurDecompte){
						$('[name="montantDecompte"]').val(demandeUtils.formatMnt(nouveauMontantFacture.toFixed(2)));
					}else{
						$('[name="montantFacture"]').val(demandeUtils.formatMnt(nouveauMontantFacture.toFixed(2)));
					}
				}
				
				demandeUtils.updateMntTotal();
		}

};


$(function(){
	
	$("#motifsRefusId").multiSelect({  
        selectAll: false, 
        noneSelected: $("#motifsDeRefusPasDeSelection").val(),  
        oneOrMoreSelected: $("#motifsDeRefusSelection").val()
 		},
	 function() {
		 	//Fonction appelée à chaque case cochée ou décochée
		 	
		 	//Récupération des éléments cochés
			var selected=new Array();
		 	$("#motifsRefusId").next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
				selected.push($(this).attr('value'));
			});
			
			var resumeMotifsRefusElem = $("#resumeMotifsRefusId");
			var montantsMotifsRefusTab = $("#montantsMotifsRefusTabId");
			var champMontantMotifRefusHtml = "";
			var listMotifsRefus = $("[name=listMotifsRefusInput]");
			var hasMotifRefusDemande = $("[name=hasMotifRefusDemande]");
			hasMotifRefusDemande.val("false");

			//Suppression du résumé des motifs de refus
			resumeMotifsRefusElem.children().remove();
			montantsMotifsRefusTab.children().remove();
			
			//Ajout des résumés des motifs sélectionnés et ajout des CS dans le champ ListMotifsRefusInput
			listMotifsRefus.val(selected.join(','));

			$.each(selected,function(i,value){
				
				//Si l'élément coché comporte un montant on ajoute un champ montant
				if (currentMotifsRefus[value].hasMontant == "true"){
				
				   champMontantMotifRefusHtml = champMontantMotifRefusHtml 								+
				   '<TR><TD colspan="3">*'+ currentMotifsRefus[value].libelle							+
	 			   '</TD></TR><TR><TD width="198px" ></TD><TD>' 										+
	 			   '<INPUT name="champMontantMotifRefus_' + value 										+
	 			   '" class="montant" value="'+currentMotifsRefus[value].montant+'"'					+
	 			   ' onkeypress="return filterCharForFloat(window.event);"'								+
	 			   ' onChange="demandeUtils.onChangeClasseMontant(this);demandeUtils.ajoutMontantCurrentMotifsRefus(this);"/>'	+
	 			   ' <b style="color:#ff3300;">-</b></TD><TD></TD></TR>';
				   
				   montantsMotifsRefusTab.html(champMontantMotifRefusHtml);
				   
				}else{
					
					hasMotifRefusDemande.val("true");
					var ligne=$('<li/>').appendTo(resumeMotifsRefusElem);
					ligne.html(currentMotifsRefus[value].libelle);
				}
			});
			
			demandeUtils.updateMntTotal();
	});
	
	var listMontants = $("[class=montant]");
	
	listMontants.change(function(){
		if (this.name == 'montantVerseOAI'){
			demandeUtils.onChangeMontantOai(this);
			demandeUtils.onChangeClasseMontant(this);
		}else{
			demandeUtils.onChangeClasseMontant(this);
		}
	});
	
	//Mémorisation des montants des motifs cochés
	var listElementsMontantsMotifsRefus = $("[name*=champMontantMotifRefus_]");
	$.each(listElementsMontantsMotifsRefus,function (index, element) {
		demandeUtils.ajoutMontantCurrentMotifsRefus(element);
	});
	
	typeSousTypeDeSoinListInit();
	
	demandeAjax.init();
	
	demandeUtils.updateMntTotal();
	
	
});
