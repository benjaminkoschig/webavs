<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.osiris.db.comptes.CAReferenceRubriqueViewBean"%>
<%@page import="globaz.osiris.parser.CASelectBlockParser"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%

	idEcran = "GCA0060";

	CAReferenceRubriqueViewBean viewBean = (CAReferenceRubriqueViewBean)session.getAttribute("viewBean");
	userActionValue = "osiris.comptes.referenceRubrique.afficher";
	if(!JAUtil.isStringNull(request.getParameter("idRefRubrique"))){
		viewBean.setIdRefRubrique(request.getParameter("idRefRubrique"));
	}
	if(!JAUtil.isStringNull(request.getParameter("idRubrique"))){
		viewBean.setIdRubrique(request.getParameter("idRubrique"));
	}

	bButtonValidate = objSession.hasRight(userActionNew, globaz.framework.secure.FWSecureConstants.ADD);

	selectedIdValue = viewBean.getIdRefRubrique();
%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">

function add() {
}

function upd() {
	document.forms[0].elements('userAction').value="osiris.comptes.referenceRubrique.modifier";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer la référence de la rubrique sélectionnée! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.referenceRubrique.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add"){
    	document.forms[0].elements('userAction').value="osiris.comptes.referenceRubrique.ajouter";
        document.forms[0].elements('selectedId').value = document.forms[0].elements('idRefRubrique').value;
    } else {
        document.forms[0].elements('userAction').value="osiris.comptes.referenceRubrique.modifier";
    }
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="back";
	} else {

		document.forms[0].elements('userAction').value="osiris.comptes.referenceRubrique.chercher";
	}
}


function init(){
//	 On met à jour les infos
	updatePage();
//	 On raffraichit le _rcListe du parent (CAPage)
<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	if (parent.document.forms[0])
		parent.document.forms[0].submit();
<%}%>
}
function updatePage() {
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCA0060_TITRE_SOUS_ECRAN_REFERENCE_RUBRIQUE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD nowrap width="50"><ct:FWLabel key="GCA0060_RUBRIQUE"/></TD>
		<TD width="1">&nbsp;</TD>
		<TD width="1"><input type="text" name="" value="<%if (viewBean.getRubrique() != null) {%><%=viewBean.getRubrique().getIdExterne()%><%}%>" class="libelleLongDisabled"  readonly="readonly" tabindex="-1"/></TD>
		<TD width="1">
			<input type="text" name="rubriqueDescription" size="30" value="<%if (viewBean.getRubrique() != null) {%><%=viewBean.getRubrique().getDescription()%><%}%>" class="libelleLongDisabled" readonly="readonly" tabindex="-1"/>
			<input type="hidden" name="idRubrique" value="<%=viewBean.getIdRubrique()%>"/>
			<input type="hidden" name="idRefRubrique" value="<%=viewBean.getIdRefRubrique()%>"/>
		</TD>
	</TR>
	<TR>
		<TD colspan="4"><HR />&nbsp;</TD>
	</TR>
	<%
		String selectBlock = CASelectBlockParser.getForIdCodeReferenceSelectBlock(objSession, viewBean.getIdCodeReference());
		
		if (!JadeStringUtil.isBlank(selectBlock)) {
			out.print("<tr>");
			out.print("<td nowrap width=\"50\" align=\"left\">" + objSession.getLabel("GCA0060_REFERENCE") + "</td>");
			out.print("<TD width=\"1\">&nbsp;</TD>");
			out.print("<td nowrap colspan=\"2\" align=\"left\">");
			out.print(selectBlock);
			out.print("</td>");
			out.print("</tr>");
		}
	%>
			
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>