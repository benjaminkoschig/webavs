$(document).ready(function() {
	idRowToInsert = "";
	$(".expandDetails").click(function() {
		idRowToInsert = $(this).attr("id").substring(6);
		var annee = $(this).parent().next().text();
		if ($("#subside"+idRowToInsert).length==0) {
			initListeSubsides($("#idContribuable").val(), annee);
		}
	});
});

function initListeSubsides(idContribuable, year){
	var a_params = new Array(
			"year:"+year,
			"idContribuable:"+idContribuable			
	);
	
	// format params
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}	
	var o_options= {
			serviceClassName: 'ch.globaz.amal.business.services.models.famille.FamilleContribuableService',
			serviceMethodName:'getListSubsideMember',
			parametres:year+","+idContribuable,
			callBack: createListeSubsides
		}
		globazNotation.readwidget.options=o_options;		
		globazNotation.readwidget.read();	
}

function createListeSubsides(data) {
	if ($("#subside"+idRowToInsert).length==0) {
		var b_isReprise = false;
		if ($("#isReprise").val() == 'true') {
			b_isReprise = true;
		} else {
			b_isReprise = false;
		}
		var detailLinkFamille = "amal?userAction=amal.famille.famille.afficher&selectedTabId=1&selectedId=";
		var detailLinkDetailFamille = "amal?userAction=amal.detailfamille.detailfamille.afficher&selectedTabId=1&selectedId=";
		var textToInsert = '';
		textToInsert += '<table class="subsideDetails" id="subside'+idRowToInsert+'" width="100%"  border="0">';
		textToInsert += '	<col width="18px" align="center"></col>';
		textToInsert += '	<col width="18px" align="left"></col>';
		textToInsert += '	<col align="left"></col>';
		textToInsert += '	<col width="60px" align="center"></col>';
		textToInsert += '	<col width="32px" align="center></col>';
		textToInsert += '	<col width="32px" align="center"></col>';
		textToInsert += '	<col width="80px" align="right"></col>';
		textToInsert += '	<col width="24px" align="right"></col>';
		textToInsert += '	<col align="left"></col>';
		textToInsert += '	<col align="center"></col>';
		textToInsert += '	<col align="center"></col>';
		textToInsert += '	<col align="center"></col>';
		textToInsert += '	<tr>';
		textToInsert += '		<th align="center"></th> <!-- Icone d&eacute;tail membre -->';
		textToInsert += '		<th align="center"></th> <!-- Icone d&eacute;tail subside -->';
		textToInsert += '		<th align="left">Nom, Pr&eacute;nom</th> <!-- Nom, pr&eacute;nom -->';
		textToInsert += '		<th align="center">Ann&eacute;e</th> <!-- Ann&eacute;e historique -->';
		textToInsert += '		<th align="center">Type</th> <!-- Type de demande -->';
		textToInsert += '		<th align="center">R</th> <!-- Refus -->';
		textToInsert += '		<th align="center">Status</th> <!-- Status -->';
		textToInsert += '		<th align="center">Contrib</th> <!-- Contribution -->';
		textToInsert += '		<th align="right"></th>';
		textToInsert += '		<th align="left">Document</th>';
		textToInsert += '		<th align="center">Envoi</th>';
		textToInsert += '		<th align="center">D&eacute;but</th>';
		textToInsert += '		<th align="center">Fin</th>';
		textToInsert += '	</tr>';
		for(iElement=0; iElement< data.length; iElement++){
			var classTr = 'amalRowOddContrib';
			if (iElement % 2 == 0) {
				classTr = 'amalRowContrib';
			}
			var amalRowHighligthedContrib = 'amalRowHighligthedContrib';
			if (!data[iElement].codeActif) {
				classTr += ' subsideDisabled';
				amalRowHighligthedContrib +=' subsideDisabled'; 
			}
			
			textToInsert += '<tr style="height:22px" class="'+classTr+'" onMouseOver="jscss(\'swap\', this, \''+classTr+'\', \''+amalRowHighligthedContrib+'\')" onMouseOut="jscss(\'swap\', this, \''+amalRowHighligthedContrib+'\', \''+classTr+'\')">';
			if (!b_isReprise) {
				textToInsert += '		<td align="center">';
				textToInsert += '			<a href="'+detailLinkFamille+data[iElement].familleId+'">';
				textToInsert += '				<img  ';
				textToInsert += '				src="images/amal/edit_user.png"';
				textToInsert += '				title="D&eacute;tails '+data[iElement].nomPrenom+'"'; 
				textToInsert += '				border="0" ';
				textToInsert += '				onMouseOver="this.style.cursor=\'hand\';"';
				textToInsert += '				onMouseOut="this.style.cursor=\'pointer\';"';
				textToInsert += '				width="18px"';
				textToInsert += '				height="18px"';
				textToInsert += '				>';
				textToInsert += '			</a>';
				textToInsert += '		</td>';
				textToInsert += '		<td align="center">';
			} else {
				textToInsert += '		<td align="center" style="text-align: left;" colspan="2">';
			}
			textToInsert += '			<a href="'+detailLinkDetailFamille+data[iElement].detailFamilleId+'">';
			textToInsert += '				<img  ';
			textToInsert += '				src="images/amal/view_text.png"';
			textToInsert += '				title="D&eacute;tails du subside '+data[iElement].nomPrenom+', '+data[iElement].debutDroit+'"'; 
			textToInsert += '				border="0" ';
			textToInsert += '				onMouseOver="this.style.cursor=\'hand\';"';
			textToInsert += '				onMouseOut="this.style.cursor=\'pointer\';"';
			textToInsert += '				width="18px"';
			textToInsert += '				height="18px"';
			textToInsert += '				>';
			textToInsert += '			</a>';
			textToInsert += '		</td>';
			textToInsert += '		<td align="left">'+data[iElement].nomPrenom+'</td>';
			textToInsert += '		<td align="center">'+data[iElement].anneeHistorique+'</td>';
			textToInsert += '		<td align="center" title="'+data[iElement].typeDemandeLibelleAJAX+'">'+data[iElement].typeDemandeCodeAJAX+'</td>';
			if (data[iElement].refus == false) {
				textToInsert += '		<td align="center">&nbsp;</td>';
			} else {
				textToInsert += '		<td align="center">R</td>';
			}
			textToInsert += '		<td align="center" title="'+data[iElement].codeTraitementDossierLibelleAJAX+'">'+data[iElement].codeTraitementDossierCodeAJAX+'</td>';
			if (parseFloat(data[iElement].supplExtra) > 0 ||
					parseFloat(data[iElement].montantSupplementPCFamille) > 0) {
				var colorSupplExtra = 'green';
				if (!data[iElement].codeActif) {
					colorSupplExtra = 'red';
				} 
				textToInsert += '		<td align="right" style="color:'+colorSupplExtra+'" title="Dont '+data[iElement].supplExtra+' de suppl&eacute;ment">'+data[iElement].montantTotalSubsideAJAX+'&nbsp;&nbsp;&nbsp;&nbsp;</td>';
			} else {
				textToInsert += '		<td align="right">'+data[iElement].montantTotalSubsideAJAX+'&nbsp;&nbsp;&nbsp;&nbsp;</td>';
			}
			
			var noModelCode = data[iElement].noModelesCodeAJAX;
			var noModelTemporaireCode = data[iElement].noModelesTemporaireCodeAJAX;
			var noModelLibelle = data[iElement].noModelesLibelleAJAX;
			var noModelTemporaireLibelle = data[iElement].noModelesTemporaireLibelleAJAX
			var noModelAbreviation = data[iElement].noModelesAbreviationAJAX;
			var noModelTemporaireAbreviation = data[iElement].noModelesTemporaireAbreviationAJAX;
			var idDetailFamilleAjax = data[iElement].idDetailFamilleAJAX;
			var idDetailFamille = data[iElement].detailFamilleId;			
			
			if (noModelTemporaireCode.length==0) {
				if (noModelCode.length==0) {
					textToInsert += '		<td align="right"></td>';
					textToInsert += '		<td align="left">-</td>';
				} else {
					textToInsert += '		<td align="right">'+noModelCode+'</td>';
					textToInsert += '		<td align="left" title="'+noModelLibelle+'">- '+noModelAbreviation+'</td>';
				}
			} else {
					if (idDetailFamille == idDetailFamilleAjax) {
						textToInsert += '		<td align="right" style="font-style:italic">'+noModelTemporaireCode+'</td>';
						textToInsert += '		<td align="left" style="font-style:italic" title="Non envoy&eacute; : '+noModelTemporaireLibelle+'">- '+noModelTemporaireAbreviation+'</td>';
					} else {
						textToInsert += '		<td align="right">'+noModelCode+'</td>';
						textToInsert += '		<td align="left" title="'+noModelLibelle+'">- '+noModelAbreviation+'</td>';
					} 
			}
			
			textToInsert += '		<td align="center">'+data[iElement].dateEnvoi+'</td>';
			textToInsert += '		<td align="center">'+data[iElement].debutDroit+'</td>';
			textToInsert += '		<td align="center">'+data[iElement].finDroit+'</td>';
			textToInsert += '	</tr>';
		}
		textToInsert += '</table>';
		$("#tdSubsides"+idRowToInsert).append(textToInsert);
	}
}


function getContextUrl() {
	if (this._s_context === null) {
		this._s_context = $('[name=Context_URL]').attr('content');
	}
	return this._s_context;
}