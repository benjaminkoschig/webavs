<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0062"; %>
<%@ page import="globaz.globall.util.JANumberFormatter" %>
<%@ page import="globaz.osiris.db.suiviprocedure.CAFailliteViewBean" %>
<%@ page import="globaz.osiris.db.comptes.CACompteAnnexeViewBean" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.osiris.translation.CACodeSystem"%>
<%
	subTableWidth = "";

	CAFailliteViewBean viewBean = (CAFailliteViewBean) session.getAttribute("viewBean");
	
	if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdCompteAnnexe())) {
		viewBean.setIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
	}
	CACompteAnnexeViewBean compteAnnexeViewBean = viewBean.getCompteAnnexe();
	
	selectedIdValue = viewBean.getIdFaillite();
	userActionValue = "osiris.suiviprocedure.faillite.modifier";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="javascript">

function add() {
    document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.ajouter"
}

function upd() {
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.ajouter";
    } else {
	    document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.modifier";
    }

    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="back";
	} else {
		document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.afficher";
	}
}

function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="osiris.suiviprocedure.faillite.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

top.document.title = "Détail Suivi de la procédure - Faillite - " + top.location.href;

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>D&eacute;tail Suivi de la procédure - Faillite<%-- /tpl:put --%>
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
	<td class="label"  width="10%" >
		<input type="hidden" name="forIdCompteAnnexe" value="<%=compteAnnexeViewBean.getIdCompteAnnexe()%>">
		<input type="hidden" name="idCompteAnnexe" value="<%=compteAnnexeViewBean.getIdCompteAnnexe()%>">
		Compte
	</td>
	<td nowrap></td>
	<td class="control" width="10%" rowspan="2"><textarea rows="4" class="disabled" readonly><%=compteAnnexeTitulaireEntete%></textarea></td>
	<td class="label" width="10%" >&nbsp;Affiliation</td>
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
	<td width="125" class="label"><b>Date faillite</b></td>
	<td width="30">&nbsp;<input type="hidden" name="idFaillite" value="<%=viewBean.getIdFaillite()%>"/></td>
	<td nowrap><ct:FWCalendarTag name="dateFaillite" doClientValidation="CALENDAR" value="<%=viewBean.getDateFaillite()%>"/></td>
	<td nowrap></td>
	<td nowrap></td>
	<td nowrap></td>
</tr>

<tr>
	<td colspan="6">&nbsp;</td>
</tr>

<tr>
	<td class="label" width="125">Production</td>
	<td width="30">&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="dateProduction" doClientValidation="CALENDAR" value="<%=viewBean.getDateProduction()%>"/></td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
	<td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Production d&eacute;finitive</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateProductionDefinitive" doClientValidation="CALENDAR" value="<%=viewBean.getDateProductionDefinitive()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Annulation production</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateAnnulationProduction" doClientValidation="CALENDAR" value="<%=viewBean.getDateAnnulationProduction()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
 </tr>

<tr>
   <td class="label">R&eacute;vocation &#47; r&eacute;tractation</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateRevocation" doClientValidation="CALENDAR" value="<%=viewBean.getDateRevocation()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Suspension faillite</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateSuspensionFaillite" doClientValidation="CALENDAR" value="<%=viewBean.getDateSuspensionFaillite()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Etat de collocation</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateEtatCollocation" doClientValidation="CALENDAR" value="<%=viewBean.getDateEtatCollocation()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Modification &eacute;tat collocation</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateModificationEtatCollocation" doClientValidation="CALENDAR" value="<%=viewBean.getDateModificationEtatCollocation()%>"/></td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
   <td nowrap>&nbsp;</td>
</tr>

<tr>
   <td class="label">Clôture faillite</td>
   <td width="30">&nbsp;</td>
   <td class="control"><ct:FWCalendarTag name="dateClotureFaillite" doClientValidation="CALENDAR" value="<%=viewBean.getDateClotureFaillite()%>"/></td>
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

<tr>
	<td class="label" >Commentaire</td>
	<td  style="min-width:30px;">&nbsp;</td>
	<td  colspan="4" nowrap style="max-width:600px;"><input type="text" name="commentaire" value="<%=viewBean.getCommentaire()%>" class="commentaire" maxlength="4000" style="width:100%;"</td>
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