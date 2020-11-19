<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="globaz.amal.utils.AMCaisseMaladieHelper"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
	
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%
	idEcran="AM3006";
	rememberSearchCriterias = true;
	bButtonNew = false;
	String email = ((globaz.globall.db.BSession) ((globaz.framework.controller.FWController) session.getAttribute("objController")).getSession()).getUserEMail();
%>
<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

bFind = false;

usrAction = "amal.sedexpt.sedexpt.lister";

$(document).ready(function() {
	// -------------------------------------------------------------------------------
	// INITIALISATION
	// -------------------------------------------------------------------------------
	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	// augmente la taille de l'iframe en hauteur :)
	$('iframe').css('height','550px');
	// initialisation des options
	$('.optionLine').hide();
	$('#legendOptionId').css('background-image','url(\'<%=request.getContextPath()%>/images/amal/arrow_collapsed.png\')');	

	// -------------------------------------------------------------------------------
	// EVENEMENTS
	// -------------------------------------------------------------------------------
	// manage the on click event on legend option
	$('#legendOptionId').click(function(){
		$('.optionLine').toggle();
		var s_backImage = $('#legendOptionId').css('background-image');
		if(s_backImage.indexOf('expanded')>=0){
			$('#legendOptionId').css('background-image','url(\'<%=request.getContextPath()%>/images/amal/arrow_collapsed.png\')');	
		}else{
			$('#legendOptionId').css('background-image','url(\'<%=request.getContextPath()%>/images/amal/arrow_expanded.png\')');	
		}
	});
	
	$("#searchModel\\.forCMIdTiersGroupe, #searchModel\\.forActifs").change(function() {
		setWhereKey();
	});
	
	
	$("#searchModel\\.inSDXStatus").multiselect({
		height: 200,
		noneSelectedText: 'Tous',
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText: function(numChecked, numTotal, checkedItems){
			var itemsChecked = "";
			var nb = 0;
			$(checkedItems).each(function() {
				nb++;
				var cs = $(this).val();
				if (cs == "<%=IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue()%>") {
					itemsChecked +="I";
				} else if (cs == "<%=IAMCodeSysteme.AMStatutAnnonceSedex.CREE.getValue()%>") {
					itemsChecked +="C";
				} else if (cs == "<%=IAMCodeSysteme.AMStatutAnnonceSedex.ENVOYE.getValue()%>") {
					itemsChecked +="E";
				} else if (cs == "<%=IAMCodeSysteme.AMStatutAnnonceSedex.RECU.getValue()%>") {
					itemsChecked +="R";
				} else if (cs == "<%=IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_CREE.getValue()%>") {
					itemsChecked +="EC";
				} else if (cs == "<%=IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_ENVOYE.getValue()%>") {
					itemsChecked +="EE";
				} else if (cs == "<%=IAMCodeSysteme.AMStatutAnnonceSedex.ERREUR_RECU.getValue()%>") {
					itemsChecked +="ER";
				}
				
				if (nb<numChecked) {
					itemsChecked +=", ";
				}
			});
			return itemsChecked;
		}
	}).multiselect("uncheckAll");
	
	$("#searchModel\\.inSDXMessageSubType").multiselect({
		height: 200,
		minWidth: 350,
		noneSelectedText: 'Tous',
		header: true,
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText : "# sur #"
	}).multiselect("uncheckAll");
	
	$("#searchModel\\.inSDXTraitement").multiselect({
		click: function(event, ui) {
			var nbChecked = $("#searchModel\\.inSDXTraitement").multiselect("getChecked").length;
			if (nbChecked==1) {
				var itemChecked = $("#searchModel\\.inSDXTraitement").multiselect("getChecked")[0].value;
				if (itemChecked == "42005702") {					
					$("#setAllAsManual").show();
				} else {
					$("#setAllAsManual").hide();
				}
			} else {
				$("#setAllAsManual").hide();
			}
		},
		height: 200,
		minWidth: 200,
		noneSelectedText: 'Tous',
		header: true,
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText : "# sur #"
	}).multiselect("uncheckAll");
	
	$("#setAllAsManual").button().css("height","10px");
	
	$("#setAllAsManual").click(function() {
		var msgConfirm = "Tout les éléments 'A traiter' qui correspondent aux critères de recherche seront mis en 'Traitement manuel' ! Continuer ? "
		if (confirm(msgConfirm)) {
			var a_params = getParametersForCSV();
			var o_options= {
	            	serviceClassName:'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
	            	serviceMethodName:'setAllAnnoncesManual',
					parametres:a_params,
					callBack: callBackSetAllAnnoncesManual,
					errorCallBack: callBackSetAllAnnoncesManualOnError
				}
	
				globazNotation.readwidget.options=o_options;		
				globazNotation.readwidget.read();
		}
	});

	$("#searchModel\\.forSUBAnneeHistorique").focusout(function (e) {
		isYearCompliant();
	});

	$("#linkCreerListePC").live("click",function() {
		attachActionCreerListePC();
	});
});

function callBackSetAllAnnoncesManual(data) {
	$("#btnFind").click();
}

function callBackSetAllAnnoncesManualOnError(data) {
	alert("La mise à jour a rencontrée une erreur ! Tentez une mise à jour individuelle et contactez GLOBAZ si l'erreur persiste.");
}

function setWhereKey(){
	var s_onlyActifs = "";
	if ($("#searchModel\\.forActifs").prop("checked")) {
		s_onlyActifs = "OnlyActifs";
	}
	
	
	if ($("#searchModel\\.forCMIdTiersGroupe").val() == "0") {
		$("#searchModel\\.whereKey").val("rcListeSansGroupe"+s_onlyActifs);
	} else {
		$("#searchModel\\.whereKey").val("rcListe"+s_onlyActifs);
	}
}

function isYearCompliant() {
	var objRegExp  = /^(\d{4}$)/;

	if($('#searchModel\\.forSUBAnneeHistorique').val() !== "") {
		if ( objRegExp.test( $('#searchModel\\.forSUBAnneeHistorique').val())) {
			return true;
		} else {
			alert("L'année spécifiée n'est pas une année valide.");
			$('#searchModel\\.forSUBAnneeHistorique').val("");
		}
	}
	return true;
}

function clearFields() {
	$("#searchModel\\.forSDXDateMessageGOE").val('');
	$("#searchModel\\.forSDXDateMessageLOE").val('');
	$("#searchModel\\.forCMIdTiersGroupe").val('');
	$("#searchModel\\.forSUBAnneeHistorique").val('');
	$("#searchModel\\.forCMNumCaisse").val('');
	$("#searchModel\\.likeCMNomCaisse").val('');
	$("#searchModel\\.inSDXStatus").multiselect("uncheckAll");
	$("#searchModel\\.inSDXTraitement").multiselect("uncheckAll");
	$("#searchModel\\.inSDXMessageSubType").multiselect("uncheckAll");
	setWhereKey();
}

function getParametersForCSV() {
	var inMessageSubType = $("#searchModel\\.inSDXMessageSubType").val();
	if (inMessageSubType==null || inMessageSubType==undefined) {
		inMessageSubType = '801|802';
	} else {
		inMessageSubType=inMessageSubType.join();
		inMessageSubType = inMessageSubType.replace(/\,/g,'|');
	}

	var inSDXStatus = $("#searchModel\\.inSDXStatus").val();
	if (inSDXStatus==null || inSDXStatus==undefined) {
		inSDXStatus = new Array();
	} else {
		inSDXStatus=inSDXStatus.join();
		inSDXStatus = inSDXStatus.replace(/\,/g,'|');
	}
	
	var inSDXTraitement = $("#searchModel\\.inSDXTraitement").val();
	if (inSDXTraitement==null || inSDXTraitement==undefined) {
		inSDXTraitement = new Array();
	} else {
		inSDXTraitement=inSDXTraitement.join();
		inSDXTraitement = inSDXTraitement.replace(/\,/g,'|');
	}
	
	var forDateMessageGOE = $("#searchModel\\.forSDXDateMessageGOE").val();
	var forDateMessageLOE = $("#searchModel\\.forSDXDateMessageLOE").val();
	var forCMIdTiersGroupe = $("#searchModel\\.forCMIdTiersGroupe").val();
	var forCMNumCaisse = $("#searchModel\\.forCMNumCaisse").val();
	var likeCMNomCaisse = $("#searchModel\\.likeCMNomCaisse").val();
	var forSUBAnneeHistorique = $("#searchModel\\.forSUBAnneeHistorique").val();
	var listeForEmail = $("#listeForEmail").val();

	var params = "likeCMNomCaisse:"+likeCMNomCaisse+";forCMNumCaisse:"+forCMNumCaisse+
		";forCMIdTiersGroupe:"+forCMIdTiersGroupe+";inSDXStatus:"+inSDXStatus+";inMessageSubType:"+inMessageSubType+
		";inSDXTraitement:"+inSDXTraitement+";forDateMessageGOE:"+forDateMessageGOE+
			";forSUBAnneeHistorique:"+forSUBAnneeHistorique+";listeForEmail:"+listeForEmail+";forDateMessageLOE:" +
			forDateMessageLOE+",setForOrder:dateMessageDesc";
	;

	return params;
}

function attachActionCreerListePC() {
	$( "#dialogCreationListePC" ).dialog({
		minHeight:"200px",
		height:"auto",
		width:"500px",
		autoOpen: true,
		modal: true,
		buttons: {
			Ok: function() {
				generationCreerListePC();
				$( this ).dialog( "close" );
			},
			Annuler: function() {
				$( this ).dialog( "close" );
			}
		}
	});
}

function generationCreerListePC() {

	var params = getParametersForCSV();

	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
		serviceMethodName:'exportListAnnoncesReponsePT',
		parametres: params,
		callBack: callBackcreerListePC
	}
	globazNotation.readwidget.options=o_options;
	globazNotation.readwidget.read();
}

function callBackcreerListePC(data) {
	// window.location.reload();
}

</script>


<div id="dialogCreationListePC" title="Créer liste PC" style="display: none;">
	<table>
		<tr>
			<td>
				<strong>Renseigner une adresse email</strong>
			</td>
		</tr>
		<tr>
			<td>
				<input type="text" id="listeForEmail" name="listeForEmail" value="<%=email%>" style="width: 10cm">
			</td>
		</tr>
	</table>
</div>


<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
		Gestion des annonces SEDEX PT
	<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
				<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<div id="divRecherche" style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png');
					background-repeat:repeat-x; float:left;border:1px solid #226194;">
			
			<TABLE border="0" cellspacing="0" cellpadding="0" width="100%" style="border-collapse:collapse; font-size: 12px">
				<tr style="height:48px">
					<td>&nbsp;&nbsp;&nbsp;</td>
					<td colspan="6">
						<h4>Paramètres de recherche</h4>
					</td>
					<td>&nbsp;&nbsp;&nbsp;</td>
				</tr>
				<TR>
					<td>&nbsp;</td>
					<td style="width:120px">Groupe</td>
					<TD>
						<select name="searchModel.forCMIdTiersGroupe">
						<%

						// Génération de la combobox
						HashMap<String, String> myOptions = null;
						String csValue="";
						String csLibelle="";
						myOptions = AMCaisseMaladieHelper.getGroupesCM();
						Object[] myKeys=myOptions.keySet().toArray();
						Arrays.sort(myKeys);
						for(int iIndex=0; iIndex<myKeys.length;iIndex++){
							csValue = (String) myKeys[iIndex];
							csLibelle = (String) myOptions.get(myKeys[iIndex]);
						%>
							<option value="<%=csValue%>"><%=csLibelle%></option>
						<%
						}// fin for option


						%>
						</select>
					</TD>
					<TD> </TD>
					<TD> </TD>
					<TD>&nbsp;</TD>
					<TD><span id="setAllAsManual" style="display:none" title="Met le traitement 'Traitement manuel' à tout les éléments 'A traiter' qui correspondent aux critères">Traitement manuel</span></TD>
					<td></td>
				</TR>
				<TR style="height:16px">
					<td>&nbsp;</td>
					<td>N&deg; caisse</td>
					<TD><INPUT class="clearable" type="text"
						name="searchModel.forCMNumCaisse" value=""></TD>
					<TD>Sous-type message</TD>
					<TD>
						<select name="searchModel.inSDXMessageSubType" multiple="multiple" id="searchModel.inSDXMessageSubType" >
							<option value="<%=AMMessagesSubTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue()%>"><%=AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(AMMessagesSubTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue())%></option>
							<option value="<%=AMMessagesSubTypesAnnonceSedex.REPONSE_PRIME_TARIFAIRE.getValue()%>"><%=AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(AMMessagesSubTypesAnnonceSedex.REPONSE_PRIME_TARIFAIRE.getValue())%></option>
						</select>
					</TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<td>&nbsp;</td>
				</TR>
				<TR style="height:16px">
					<td>&nbsp;</td>
					<TD>Nom caisse</TD>
					<TD><INPUT class="clearable" type="text"
						name="searchModel.likeCMNomCaisse" value="">
					</TD>
					<TD>Date message depuis</TD>
					<TD><input class="clearable" type="text"
						name="searchModel.forSDXDateMessageGOE" value=""
						data-g-calendar="mandatory:false" />
					</TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<td>&nbsp;</td>
				</TR>
				<TR style="height:16px">
					<td>&nbsp;</td>
					<td>Statut</td>
					<td>
						<select multiple="multiple" name="searchModel.inSDXStatus" id="searchModel.inSDXStatus" >
							<option selected="selected" value="<%=IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue()%>">Initial</option>
							<option selected="selected" value="<%=IAMCodeSysteme.AMStatutAnnonceSedex.CREE.getValue()%>">Crée</option>
							<option selected="selected" value="<%=IAMCodeSysteme.AMStatutAnnonceSedex.ENVOYE.getValue()%>">Envoyé</option>
							<option selected="selected" value="<%=IAMCodeSysteme.AMStatutAnnonceSedex.RECU.getValue()%>">Reçu</option>
							<option selected="selected" value="<%=IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_CREE.getValue()%>">Erreur création</option>
							<option selected="selected" value="<%=IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_ENVOYE.getValue()%>">Erreur envoi</option>
							<option selected="selected" value="<%=IAMCodeSysteme.AMStatutAnnonceSedex.ERREUR_RECU.getValue()%>">Erreur réception</option>
						</select>
					</td>
					<TD>Date message jusqu'à</TD>
					<TD>
						<table>
							<tr>
								<td>
									<input class="clearable" type="text"
									name="searchModel.forSDXDateMessageLOE" value=""
									data-g-calendar="mandatory:false" />
								</td>
								<td> </td>
								<td>Année</td>
								<td><input class="clearable" type="text" id="searchModel.forSUBAnneeHistorique"
										   name="searchModel.forSUBAnneeHistorique" value="" maxlength="4"/>
								</td>
							</tr>
						</table>&nbsp;&nbsp;&nbsp;&nbsp;
					</TD>
					<TD>&nbsp;
					</TD>
					<TD>
						<table>
							<tr>
								<td>
									<a id="linkCreerListePC" class="butonDownload ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button" aria-disabled="false">
										<span class="ui-button-text">
											Créer liste PC<img class="typeDoc" style="margin:0 0 0 10px;padding:0" src="/webavs_web/scripts/jsnotation/imgs/csv.png" id="" height="20px">
										</span></a>

								</td>
								<td>
									<a data-g-download="docType:csv,
										dynParametres:getParametersForCSV,
										serviceClassName:ch.globaz.amal.business.services.sedexRP.AnnoncesRPService,
										serviceMethodName:exportListAnnonces,
										docName:listeAnnonces,
										displayOnlyImage:false">Créer liste</a>
								</td>
							</tr>
						</table>
					</TD>
					<td>&nbsp;</td>
				</TR>				
				<TR style="height:16px">
					<td>&nbsp;</td>
					<TD colspan="6">&nbsp;</TD>
					<td>&nbsp;</td>
				</TR>
				<tr style="font-size: 0.9em;font-style:italic">
					<td>&nbsp;</td>
					<td colspan="6">
						<fieldset style="border-top-width:1px;border-left-width:0px;border-right-width:0px;border-bottom-width:0px"
								  > 
							<legend style="padding-left: 16px"
								  style="background-image: url('<%=request.getContextPath()%>/images/amal/arrow_expanded.png')"
								  style="background-attachment: scroll"
								  style="background-repeat: no-repeat"
								  style="background-origin: padding-box"
								  style="background-clip: border-box"
								  style="background-position-x: 0%"
								  style="background-position-y: 40%"
								  style="background-size: auto"
								  style="background-color: transparent"
								  style="cursor:hand"
								  id="legendOptionId"
							>
								Options&nbsp;
							</legend>
						</fieldset>
					</td>
					<td>&nbsp;</td>
				</tr>
				<TR class="optionLine" style="font-size: 0.9em;font-style:italic;">
					<td>&nbsp;</td>
					<TD></TD>
					<TD></TD>			
					<TD>Trier par</TD>
					<%
					Vector orderList=new Vector(3);	
					orderList.add(new String[]{"default","Date message desc"});
					orderList.add(new String[]{"rcListDateMsgAsc","Date message asc"});					
					orderList.add(new String[]{"orderByIdDetFamDESC_numDecisionDESC_msgSubTypeASc","Ordre d'exécution"});
					%>
					<TD><ct:FWListSelectTag data="<%=orderList%>" defaut=""
						name="searchModel.orderKey" /></TD>
					<TD>&nbsp;</TD>
					<TD>
						<input style="font-size: 0.9em;" type="button" id="clearButton" onclick="clearFields()" accesskey="C" value="Effacer" />
						[ALT+C]
					</TD>
				</TR>
				<TR>
					<td>&nbsp;</td>
					<TD colspan="6">&nbsp;</TD>
					<td>&nbsp;</td>
				</TR>	
				<input type="hidden" name="searchModel.whereKey" value="rcListe" />			
			</TABLE>
		</div>
	</td>
</tr>


<%-- /tpl:insert --%>
<TR>
	<TD height="20">
		<INPUT type="hidden" name="userAction" value="">
		<INPUT type="hidden" name="_sl" value="">
		<INPUT type="hidden" name="_method" value="">
		<INPUT type="hidden" name="_valid" value="">
		<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
	</TD>
</TR>
</TBODY>
</TABLE>
</TD>
</TR>
<TR>
	<TD bgcolor="#FFFFFF" colspan="2" align="right">
			<%if (bButtonDelete) {%><input class="btnCtrl" id="btnDel" type="button" value="<%=btnDelLabel%>" onclick="del();"><%}%>
			<%if (bShowExportButton) {%>
		<INPUT type="button" name="btnExport" value="<%=btnExportLabel%>" onClick="onExport();">
			<%}%>
			<%if (bButtonFind) {%>
		<INPUT type="submit" name="btnFind" value="<%=btnFindLabel%>">
			<%} if (bButtonNew) {%>
		<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="onClickNew();btnNew.onclick='';document.location.href='<%=actionNew%>'">
			<%}%>

			<%
				String actionCreationAnnonce =  servletContext + mainServletPath + "?userAction=amal.sedexpt.sedexptcreationannonce.afficher";
			%>
			<INPUT type="submit" name="btnCreationAnnonce" value="Création annonces" onClick="btnCreationAnnonce.onclick='';document.location.href='<%=actionCreationAnnonce%>'">

<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf"%>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf"%>
