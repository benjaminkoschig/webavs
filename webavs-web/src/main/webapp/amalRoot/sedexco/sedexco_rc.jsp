<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedexCO"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMSousTypeMessageSedexCOLibellesSubside"%>
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
	idEcran="AM3004";
	rememberSearchCriterias = true;
	bButtonNew = false;
%>
<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

bFind = true;

usrAction = "amal.sedexco.sedexco.lister";
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
		height: 150,
		minWidth: 440,
		noneSelectedText: 'Tous',
		header: true,
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedText : "# sur #"
	}).multiselect("uncheckAll");
});

function callBackSetAllAnnoncesManual(data) {
	$("#btnFind").click();
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

function clearFields() {
	$("#searchModel\\.forSDXDateAnnonceGOE").val('');
	$("#searchModel\\.forSDXDateAnnonceLOE").val('');
	$("#searchModel\\.forCMIdTiersGroupe").val('');
	$("#searchModel\\.forCMNumCaisse").val('');
	$("#searchModel\\.likeCMNomCaisse").val('');
	$("#searchModel\\.inSDXStatus").multiselect("uncheckAll");
	$("#searchModel\\.inSDXMessageSubType").multiselect("uncheckAll");
	setWhereKey();
}

function getParametersForCSV() {
	var inMessageSubType = $("#searchModel\\.inSDXMessageSubType").val();
	if (inMessageSubType==null || inMessageSubType==undefined) {
		inMessageSubType = new Array();
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
	
	var forDateAnnonceGOE = $("#searchModel\\.forSDXDateAnnonceGOE").val();
	var forDateAnnonceLOE = $("#searchModel\\.forSDXDateAnnonceLOE").val();
	var forCMIdTiersGroupe = $("#searchModel\\.forCMIdTiersGroupe").val();
	var forCMNumCaisse = $("#searchModel\\.forCMNumCaisse").val();
	var likeCMNomCaisse = $("#searchModel\\.likeCMNomCaisse").val();
	
	var params = "likeCMNomCaisse:"+likeCMNomCaisse+";forCMNumCaisse:"+forCMNumCaisse+
		";forCMIdTiersGroupe:"+forCMIdTiersGroupe+";inSDXStatus:"+inSDXStatus+";inMessageSubType:"+inMessageSubType+
		";forDateAnnonceGOE:"+forDateAnnonceGOE+
		";forDateAnnonceLOE:"+forDateAnnonceLOE+",setForOrder:dateAnnonceDesc";
	return params;
}

</script>

<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
		Gestion des annonces SEDEX RP
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
					</select></TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<td</td>
				</TR>
				<TR style="height:16px">
					<td>&nbsp;</td>
					<td>N&deg; caisse</td>
					<TD><INPUT class="clearable" type="text"
						name="searchModel.forCMNumCaisse" value=""></TD>
					<TD>Sous-type message</TD>
					<TD>
						<select name="searchModel.inSDXMessageSubType" multiple="multiple" id="searchModel.inSDXMessageSubType" >
							<%
							for (AMMessagesSubTypesAnnonceSedexCO s : AMMessagesSubTypesAnnonceSedexCO.getSortedEnums()) {
								%>
								<option value="<%=s.getValue()%>"><%=AMMessagesSubTypesAnnonceSedexCO.getSubTypeCSLibelle(s.getValue())%></option>
								<%						
							}
							%>
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
						name="searchModel.forSDXDateAnnonceGOE" value=""
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
					<TD><input class="clearable" type="text"
						name="searchModel.forSDXDateAnnonceLOE" value=""
						data-g-calendar="mandatory:false" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</TD>
					<TD>&nbsp;
					</TD>
					<TD>
						<a data-g-download="docType:csv,
							dynParametres:getParametersForCSV,
	                    	serviceClassName:ch.globaz.amal.business.services.sedexCO.AnnoncesCOService,
	                    	serviceMethodName:exportListAnnonces,
	                    	docName:listeAnnonces,
	                    	displayOnlyImage:false">Créer liste</a> 
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
<%@ include file="/theme/find/bodyButtons.jspf"%>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf"%>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf"%>
