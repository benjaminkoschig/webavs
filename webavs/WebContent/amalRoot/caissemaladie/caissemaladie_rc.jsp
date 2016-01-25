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

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
	
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%
	idEcran="AM3001";
	rememberSearchCriterias = true;
	bButtonNew = false;
%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">

bFind = true;

usrAction = "amal.caissemaladie.caissemaladie.lister";
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
	
	$("#searchModel\\.forIdTiersGroupe, #searchModel\\.forActifs").change(function() {
		setWhereKey();
	});
});

function setWhereKey(){
	var s_onlyActifs = "";
	if ($("#searchModel\\.forActifs").prop("checked")) {
		s_onlyActifs = "OnlyActifs";
	}
	
	
	if ($("#searchModel\\.forIdTiersGroupe").val() == "0") {
		$("#searchModel\\.whereKey").val("rcListeSansGroupe"+s_onlyActifs);
	} else {
		$("#searchModel\\.whereKey").val("rcListe"+s_onlyActifs);
	}
}

function clearFields() {
	$('#searchModel\\.forIdTiersGroupe').val('');
	$('#searchModel\\.forActifs').prop("checked", false);
	$('#searchModel\\.orderBy').val('orderRCListeNomCaisse');
	$('#searchModel\\.forNumCaisse').val('');
	$('#searchModel\\.likeNomCaisse').val('');
	setWhereKey();
}

</script>

<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>
	<%-- tpl:insert attribute="zoneTitle" --%>
		Gestion des Assureurs
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
					<select name="searchModel.forIdTiersGroupe">
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
					<td>N&deg; caisse</td>
					<TD><INPUT class="clearable" type="text"
						name="searchModel.forNumCaisse" value=""></TD>
					<td></td>
					<TD></TD>
					<td>&nbsp;</td>
				</TR>
				<TR style="height:16px">
					<td>&nbsp;</td>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<TD>Nom caisse</TD>
					<TD><INPUT class="clearable" type="text"
						name="searchModel.likeNomCaisse" value="">
					</TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
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
					<TD>Uniquement actives</TD>
					<TD>
						<!-- <INPUT type="hidden" name="searchModel.forActifs" value="0" /> -->
						<input type="checkbox" name="searchModel.forActifs" />
					</TD>			
					<TD>Trier par</TD>
					<%
					Vector orderList=new Vector(2);	
					orderList.add(new String[]{"orderRCListeNomCaisse","Nom de la caisse"});
					orderList.add(new String[]{"orderRCListeNumCaisse","Numéro de la caisse"});
					%>
					<TD><ct:FWListSelectTag data="<%=orderList%>" defaut=""
						name="searchModel.orderBy" /></TD>
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
