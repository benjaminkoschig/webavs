<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
	
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@page import="java.util.Vector"%>
<%
	idEcran="AM0004";
	bButtonNew = false;
	rememberSearchCriterias = true;
	
	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_AM_CON_R_TRI_NOMPRENOM")});
%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu"/>


<script type="text/javascript">
<!--
bFind = false;
actionNew =  "<%=servletContext + mainServletPath + "?userAction=amal.contribuable.contribuable.afficher&_method=add"%>";

usrAction = "amal.contribuable.contribuableHistorique.lister";
$(document).ready(function() {
	// -------------------------------------------------------------------------------
	// INITIALISATION
	// -------------------------------------------------------------------------------
	changeUserAction($("#forRechercheHisto"));
	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	// Changement de la taille de la police pour les éléments options
	$('#contribuableHistoriqueRCListeSearch\\.orderBy').css('font-size','0.9em');
	$('#clearButton').css('font-size','0.9em');
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
	
	$("#contribuableHistoriqueRCListeSearch\\.forNoContribuable").change(function() {
		var val = $(this).val();
		if (val.indexOf('.')<0 && val.length==11) {
			$(this).val(val.substr(0,3)+"."+val.substr(3,3)+"."+val.substr(6,3)+"."+val.substr(9,2)); 
		}
	});
	
});


function clearFields() {
	$('.clearable, #contribuableHistoriqueRCListeSearch\\.forCsSexe, #partialcontribuableSearch\\.likeNss').val("");
}

function changeUserAction(elem) {
}
//-->
</script>
<ct:menuChange displayId="options" menuId="amal-optionsempty"></ct:menuChange>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_AM_CON_R_HISTORIQUE_TITRE"/><%-- /tpl:insert --%>
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
					<td>&nbsp;&nbsp;&nbsp;</td>
					<TD style="width:160px"><LABEL for="contribuableHistoriqueRCListeSearch.likeNss"><ct:FWLabel
						key="JSP_AM_CON_R_NSS" /></LABEL>&nbsp;<INPUT type="hidden"
						name="hasPostitField" value="<%=true%>"></TD>
					<TD><nss:nssPopup avsMinNbrDigit="99" nssMinNbrDigit="99"
						newnss="" name="contribuableHistoriqueRCListeSearch.likeNss" /> <input type="hidden"
						id="hiddenContribuableSearchlikeNss"
						name="contribuableHistoriqueRCListSearch.likeNss"></TD>
					<TD><LABEL for="likeNom"><ct:FWLabel
						key="JSP_AM_CON_R_NOM" /></LABEL>&nbsp;</TD>
					<TD><INPUT class="clearable" type="text"
						name="contribuableHistoriqueRCListeSearch.likeNom" value="" onchange=""></TD>
					<TD><LABEL for="likePrenom"><ct:FWLabel
						key="JSP_AM_CON_R_PRENOM" /></LABEL>&nbsp;</TD>
					<TD><INPUT class="clearable" ype="text"
						name="contribuableHistoriqueRCListeSearch.likePrenom" onchange="" value=""></TD>
					<td>&nbsp;&nbsp;&nbsp;</td>
				</TR>
				<TR>
					<td>&nbsp;&nbsp;&nbsp;</td>
					<TD><LABEL for="forNoContribuable"><ct:FWLabel
						key="JSP_AM_CON_R_NO_CONTRIBUABLE" /></LABEL>&nbsp;</TD>
					<TD><INPUT class="clearable" type="text"
						name="contribuableHistoriqueRCListeSearch.forNoContribuable" value=""
						onchange=""></TD>
					<TD><LABEL for="forDateNaissance"><ct:FWLabel
						key="JSP_AM_CON_R_DATE_NAISSANCE" /></LABEL>&nbsp;</TD>
					<TD><input class="clearable" type="text"
						name="contribuableHistoriqueRCListeSearch.forDateNaissance" value=""
						data-g-calendar="mandatory:false" /></TD>
					<TD>&nbsp;</TD>
					<TD>&nbsp;</TD>
					<td>&nbsp;&nbsp;&nbsp;</td>
				</TR>
				<TR style="height:16px">
					<td>&nbsp;</td>
					<TD colspan="6">&nbsp;</TD>
					<td>&nbsp;</td>
				</TR>
				<tr style="font-size: 0.9em;font-style:italic">
					<td>&nbsp;</td>
					<td colspan="6">
						<!-- <h5>Options</h5> -->
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
				<TR class="optionLine"  style="font-size: 0.9em;font-style:italic">
					<td colspan="3">&nbsp;&nbsp;&nbsp;</td>
					<TD><ct:FWLabel key="JSP_TRIER_PAR" /></TD>
					<TD><ct:FWListSelectTag data="<%=orderList%>" defaut=""
						name="contribuableHistoriqueRCListeSearch.orderBy" /></TD>
					<TD>&nbsp;</TD>
					<TD><input type="hidden" name="contribuableHistoriqueRCListeSearch.forRechercheHistorique" value="1"/>
						<input type="button" id="clearButton" onclick="clearFields()" accesskey="C" value="Effacer" />[ALT+C]</TD>
					<td>&nbsp;&nbsp;&nbsp;</td>
				</TR>		
				<TR>
					<td>&nbsp;</td>
					<TD colspan="6">&nbsp;</TD>
					<td>&nbsp;</td>
				</TR>
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
