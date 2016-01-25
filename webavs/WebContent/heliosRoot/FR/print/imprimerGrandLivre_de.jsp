
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.print.*,globaz.helios.db.interfaces.*,globaz.globall.db.*,globaz.helios.translation.*" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF2005";
	//Récupération du viewBean
	CGImprimerGrandLivreViewBean viewBean = (CGImprimerGrandLivreViewBean) session.getAttribute ("viewBean");

	//Récupération de l'exercice comptable
	CGExerciceComptable exerciceComptable = (CGExerciceComptable) session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	//Label utilisé pour spécifier à l'utilisateur qu'aucune option n'est sélectionnée.
	String labelAucun = "Aucun";
	if (languePage.equalsIgnoreCase("de")) {
		labelAucun = "Kein";
	}

	userActionValue = "helios.print.imprimerGrandLivre.executer";

String toutLexercice = "Tout l'exercice";
if (languePage.equalsIgnoreCase("de")) {
	toutLexercice = "Ganze Rechnungsjahr";
}

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.helios.itext.list.CGProcessImpressionGrandLivre"%>
<ct:menuChange displayId="menu" menuId="CG-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CG-OnlyDetail"/>

<%
	globaz.framework.menu.FWMenuBlackBox bb = (globaz.framework.menu.FWMenuBlackBox) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
	bb.setNodeOpen(false, "parameters", "CG-MenuPrincipal");
%>

<script>
function init() { }
function onOk() {
	document.forms[0].submit();
}
function onCancel() {
	document.forms[0].elements('userAction').value="back";
//	document.forms[0].submit();
}

function clearDateFields() {
	document.getElementById('fromPeriodeDate').value = "";
	document.getElementById('untilPeriodeDate').value = "";

	document.getElementById('fromPeriodeDate').disabled = true;
	document.getElementById('untilPeriodeDate').disabled = true;
}

function openDateFields() {
	document.getElementById('fromPeriodeDate').disabled = false;
	document.getElementById('untilPeriodeDate').disabled = false;
}

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le compte n'existe pas.");
	}
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>


	Imprimer le Grand Livre
      <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<tr>
			<td>Mandat</td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'> <input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"></td>
		</tr>
		<tr>
			<td>Exercice</td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getFullDescription()%>'>
			     <input name='idMandat' type="hidden" value='<%=exerciceComptable.getIdMandat()%>'></td>
		</tr>

		<tr>
			<td>Adresse E-Mail</td>
			<td> <input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>'> * </td>
		</tr>

		<tr>
			<td>Comptabilité </td>
			<td nowrap><ct:FWCodeSelectTag name="idComptabilite" defaut="<%=CodeSystem.CS_DEFINITIF%>" codeType="CGPRODEF" />
		</tr>

		<%
			globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
			globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

			int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();

			String jspLocation = servletContext + "/heliosRoot/compte_select.jsp";
			String params = "idExerciceComptable=" + exerciceComptable.getIdExerciceComptable() + "&isMandatAVS=" + exerciceComptable.getMandat().isEstComptabiliteAVS();
		%>

		<tr>
			<td>Compte de...</td>
			<td>
			<ct:FWPopupList name="compteDe"
				onFailure="onCompteFailure(window.event);"
				validateOnChange="true"
				params="<%=params%>"
				value=""
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				autoNbrDigit="<%=autoCompleteStart%>"
				forceSelection="true"/>

			&nbsp;à ...&nbsp;

			<ct:FWPopupList name="compteA"
				onFailure="onCompteFailure(window.event);"
				validateOnChange="true"
				params="<%=params%>"
				value=""
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				autoNbrDigit="<%=autoCompleteStart%>"
				forceSelection="true"/>
			</td>
		</tr>

		<tr>
			<td>Période</td>
			<td>
			<input type="radio" name="searchPeriode" value="<%=CGProcessImpressionGrandLivre.SEARCH_TOUT_EXERCICE%>" onClick="javascript:clearDateFields();" checked/>Tout l'exercice
			<br/>
			<input type="radio" name="searchPeriode" value="<%=CGProcessImpressionGrandLivre.SEARCH_PAR_DATE%>" onClick="javascript:openDateFields();"/>Du <ct:FWCalendarTag name="fromPeriodeDate" value=""/>&nbsp;au&nbsp;<ct:FWCalendarTag name="untilPeriodeDate" value=""/>
			</td>
		</tr>

	<script>
		javascript:clearDateFields();
	</script>
			  <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
