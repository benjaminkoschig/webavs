<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0064"; %>
<%@ page import="globaz.globall.util.JANumberFormatter" %>
<%@ page import="globaz.osiris.db.suiviprocedure.CASursisConcordataireViewBean" %>
<%@ page import="globaz.osiris.db.comptes.CACompteAnnexeViewBean" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.osiris.translation.CACodeSystem"%>
<%
	subTableWidth = "";

	CASursisConcordataireViewBean viewBean = (CASursisConcordataireViewBean) session.getAttribute("viewBean");
	
	if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdCompteAnnexe())) {
		viewBean.setIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
	}
	CACompteAnnexeViewBean compteAnnexeViewBean = viewBean.getCompteAnnexe();
	
	selectedIdValue = viewBean.getIdSursisConcordataire();
	userActionValue = "osiris.suiviprocedure.sursisConcordataire.modifier";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="javascript">

function add() {
    document.forms[0].elements('userAction').value="osiris.suiviprocedure.sursisConcordataire.ajouter"
}

function upd() {
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="osiris.suiviprocedure.sursisConcordataire.ajouter";
    } else {
	    document.forms[0].elements('userAction').value="osiris.suiviprocedure.sursisConcordataire.modifier";
    }

    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="back";
	} else {
		document.forms[0].elements('userAction').value="osiris.suiviprocedure.sursisConcordataire.afficher";
	}
}

function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="osiris.suiviprocedure.sursisConcordataire.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

top.document.title = "Détail Suivi de la procédure - Sursis concordataire - " + top.location.href;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>D&eacute;tail Suivi de la procédure - Sursis concordataire<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>


<%
	String idCompteAnnexe = "";
	String compteAnnexeTitulaireEntete = "";
	String compteAnnexeRoleDateDebutDateFin = "";
	String compteAnnexeSoldeFormate = "";
	String compteAnnexeInformation = "";

	try {
		idCompteAnnexe = compteAnnexeViewBean.getIdCompteAnnexe();
		compteAnnexeTitulaireEntete = compteAnnexeViewBean.getTitulaireEntete();
		compteAnnexeRoleDateDebutDateFin = compteAnnexeViewBean.getRole().getDateDebutDateFin(compteAnnexeViewBean.getIdExterneRole());
		compteAnnexeSoldeFormate = compteAnnexeViewBean.getSoldeFormate();
		compteAnnexeInformation = compteAnnexeViewBean.getInformation();

	} catch (Exception e) {
	}
%>

<tr>
	<td class="label">
		<input type="hidden" name="forIdCompteAnnexe" value="<%=compteAnnexeViewBean.getIdCompteAnnexe()%>">
		<input type="hidden" name="idCompteAnnexe" value="<%=compteAnnexeViewBean.getIdCompteAnnexe()%>">
		Compte
	</td>
	<td nowrap></td>
	<td class="control" rowspan="2"><textarea rows="4" class="disabled" readonly><%=compteAnnexeTitulaireEntete%></textarea></td>
	<td class="label">&nbsp;Affiliation</td>
	<td nowrap class="control">&nbsp;<input type="text" value="<%=compteAnnexeRoleDateDebutDateFin%>" class="libelleLongDisabled" readonly></td>
	<td>&nbsp;</td>
</tr>

<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td class="label">&nbsp;Solde compte</td>
	<td class="control">&nbsp;<input type="text" value="<%=compteAnnexeSoldeFormate%>" class="montantDisabled" readonly></td>
	<td>&nbsp;</td>
</tr>

<tr>
	<% if (!JadeStringUtil.isDecimalEmpty(compteAnnexeInformation)) { %>
           	<td class="label">Information</td>
           	<td nowrap></td>
           	<td class="control" colspan="2">
            	<input style="color:#FF0000" type="text" name="" value="<%=CACodeSystem.getLibelle(session, compteAnnexeInformation)%>" class="inputDisabled" tabindex="-1" readonly>
           	</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
	<% } else { %>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
           	<td nowrap>&nbsp;</td>
	<% } %>
</tr>


<tr>
	<td colspan="6"><br/><hr noshade size="3"><br/></td>
</tr>

<tr>
	<td width="125" class="label"><b>Date du sursis concordataire</b></td>
	<td width="30">&nbsp;
		<input type="hidden" name="idSursisConcordataire" value="<%=viewBean.getIdSursisConcordataire()%>"/>
		<input type="hidden" name="idCompteAnnxe" value="<%=idCompteAnnexe%>"/>
	</td>
	<td nowrap><ct:FWCalendarTag name="dateSursisConcordataire" doClientValidation="CALENDAR" value="<%=viewBean.getDateSursisConcordataire()%>"/></td>
	<td nowrap></td>
	<td nowrap></td>
	<td nowrap></td>
</tr>

<tr>
	<td colspan="6">&nbsp;</td>
</tr>

<tr>
	<td class="label">Ech&eacute;ance du sursis</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateEcheanceSursis" doClientValidation="CALENDAR" value="<%=viewBean.getDateEcheanceSursis()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">Production</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateProduction" doClientValidation="CALENDAR" value="<%=viewBean.getDateProduction()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">Rectification production</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateRectificationProduction" doClientValidation="CALENDAR" value="<%=viewBean.getDateRectificationProduction()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">Ech&eacute;ance prolongation</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateEcheanceProlongation" doClientValidation="CALENDAR" value="<%=viewBean.getDateEcheanceProlongation()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">R&eacute;vocation du sursis</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateRevocationSursis" doClientValidation="CALENDAR" value="<%=viewBean.getDateRevocationSursis()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">Assembl&eacute;e cr&eacute;anciers</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateAssembleeCreanciers" doClientValidation="CALENDAR" value="<%=viewBean.getDateAssembleeCreanciers()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">Homologation du sursis</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateHomologationSursis" doClientValidation="CALENDAR" value="<%=viewBean.getDateHomologationSursis()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">Fin du sursis</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateFinSursis" doClientValidation="CALENDAR" value="<%=viewBean.getDateFinSursis()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
	<td class="label">Montant de production</td>
	<td width="30">&nbsp;</td>
	<td class="control"><input type="text" name="montantProduction" value="<%=JANumberFormatter.formatNoRound(viewBean.getMontantProduction(), 2)%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>