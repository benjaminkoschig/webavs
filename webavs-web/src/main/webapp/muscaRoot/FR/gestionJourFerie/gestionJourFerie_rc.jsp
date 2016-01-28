<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@ page import="globaz.musca.db.gestionJourFerie.FAGestionJourFerieUtil" %>
<%@ page import="globaz.musca.db.gestionJourFerie.FAModifierSupprimerPeriodeViewBean" %>
<%
	rememberSearchCriterias = true;
	idEcran ="?";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="java.util.ArrayList"%><SCRIPT>
	usrAction = "musca.gestionJourFerie.gestionJourFerie.lister";
	bFind = true;

	function periodeWeekend(){
		setUserAction("musca.gestionJourFerie.periodeWeekend.afficher");
		document.forms[0].target= "fr_main";
		document.forms[0].submit();
	}

	function modifierSupprimerSelection(operationARealiser) {
		var listIdFerieAModifierSupprimer = "";
		var listCheckBox = $('[name=chkJourAModifierSupprimer]:checked',$('[name=fr_list]').contents()) ;

		listCheckBox.each(function(checkBox){
			listIdFerieAModifierSupprimer = listIdFerieAModifierSupprimer + $(this).val() + ",";
		});
		listIdFerieAModifierSupprimer = listIdFerieAModifierSupprimer.substring(0, listIdFerieAModifierSupprimer.length - 1);

		$("#listIdFerieAModifierSupprimer").val(listIdFerieAModifierSupprimer);
		$("#operationARealiser").val(operationARealiser);

		setUserAction("musca.gestionJourFerie.modifierSupprimerPeriode.afficher");
		document.forms[0].target= "fr_main";
		document.forms[0].submit();
	}

	$(function(){
		$("#btnPeriodeWeekend").click(function(){periodeWeekend();});
		$("#btnModifierSelection").click(function(){modifierSupprimerSelection('<%=FAModifierSupprimerPeriodeViewBean.OPERATION_MODIFIER %>');});
		$("#btnSupprimerSelection").click(function(){modifierSupprimerSelection('<%=FAModifierSupprimerPeriodeViewBean.OPERATION_SUPPRIMER %>');});
	});
		 
</SCRIPT>

<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_JOUR_FERIE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	
	<input type="hidden" id="listIdFerieAModifierSupprimer" name="listIdFerieAModifierSupprimer" value=""/>
	<input type="hidden" id="operationARealiser" name="operationARealiser" value=""/>

	<TR>
		<TD nowrap width="140" height="31"><ct:FWLabel key="DE_DATE" /></TD>	
		<TD nowrap width="300"><ct:FWCalendarTag name="fromDateJour" value=""/></TD>
	</TR>
	
	<TR>
		<TD nowrap width="140" height="31"><ct:FWLabel key="A_DATE" /></TD>	
		<TD nowrap width="300"><ct:FWCalendarTag name="toDateJour" value=""/></TD>
	</TR>
	
	<TR>
		<TD><ct:FWLabel key="DOMAINE"/></TD>
		<TD><%=FAGestionJourFerieUtil.getHtmlCheckBoxDomaineFerie()%></TD>
	</TR>
	
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
	<INPUT type="button" name="btnPeriodeWeekend" id="btnPeriodeWeekend" value="Ajouter une période" />
	<INPUT type="button" name="btnModifierSelection" id="btnModifierSelection" value="Modifier la sélection" />
	<INPUT type="button" name="btnSupprimerSelection" id="btnSupprimerSelection" value="Supprimer la sélection" />
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>