<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GCA0063";
	rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.translation.CACodeSystem"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%
	actionNew += "&idCompteAnnexe=" + request.getParameter("selectedId");

	globaz.osiris.db.suiviprocedure.CAFailliteListViewBean manager = new globaz.osiris.db.suiviprocedure.CAFailliteListViewBean();
	manager.setSession((globaz.globall.db.BSession)controller.getSession());
	manager.setForIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.osiris.db.comptes.CACompteAnnexeViewBean viewBean = manager.getCompteAnnexe();
	String idContentieuxSrc = request.getParameter("idContentieuxSrc");
	String libSequence = request.getParameter("libSequence");
	String idAdministrateurSrc = request.getParameter("idAdministrateurSrc");

	if (globaz.jade.client.util.JadeStringUtil.isNull(idContentieuxSrc)) {
		idContentieuxSrc = "";
	}
	if (globaz.jade.client.util.JadeStringUtil.isNull(libSequence)) {
		libSequence = "";
	}
	if (globaz.jade.client.util.JadeStringUtil.isNull(idAdministrateurSrc)) {
		idAdministrateurSrc = "";
	}
%>
<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier" showTab="options">
	<ct:menuSetAllParams key="idContentieuxSrc" value="<%=idContentieuxSrc%>"/>
	<ct:menuSetAllParams key="libSequence" value="<%=libSequence%>"/>
	<ct:menuSetAllParams key="idAdministrateurSrc" value="<%=idAdministrateurSrc%>"/>
	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
	<ct:menuSetAllParams key="forIdExterneRoleLike"  value="<%=viewBean.getIdExterneRole()%>"/>
	<ct:menuSetAllParams key="forIdRole"  value="<%=viewBean.getIdRole()%>"/>
	<ct:menuSetAllParams key="idCompteAnnexe"  value="<%=viewBean.getIdCompteAnnexe()%>"/>
	<ct:menuSetAllParams key="id"  value="<%=viewBean.getIdCompteAnnexe()%>"/>
</ct:menuChange>

<script language="JavaScript">
<!--//hide this script from non-javascript-enabled browsers
usrAction = "osiris.suiviprocedure.faillite.lister";
bFind = true;

top.document.title = "Suivi de la procédure - Faillite - " + top.location.href;
// stop hiding -->
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
			Suivi de la procédure - Faillites<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<%
	String idCompteAnnexe = "";
	String compteAnnexeTitulaireEntete = "";
	String compteAnnexeRoleDateDebutDateFin = "";
	String compteAnnexeSoldeFormate = "";
	String compteAnnexeInformation = "";

	try {
		idCompteAnnexe = viewBean.getIdCompteAnnexe();
		compteAnnexeTitulaireEntete = viewBean.getTitulaireEntete();
		compteAnnexeRoleDateDebutDateFin = viewBean.getRole().getDateDebutDateFin(viewBean.getIdExterneRole());
		compteAnnexeSoldeFormate = viewBean.getSoldeFormate();
		compteAnnexeInformation = viewBean.getInformation();

	} catch (Exception e) {
	}
%>
<tr>
	<td class="label">Compte</td>
	<td class="control" rowspan="2"><textarea rows="4" class="disabled" readonly><%=compteAnnexeTitulaireEntete%></textarea></td>
	<td>&nbsp;<input type="hidden" name="forIdCompteAnnexe" value="<%=idCompteAnnexe%>"></td>
	<td class="label">Affiliation&nbsp;</td>
	<td nowrap class="control"><input type="text" value="<%=compteAnnexeRoleDateDebutDateFin%>" class="libelleLongDisabled" readonly></td>
</tr>

<tr>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
	<td class="label">Solde compte&nbsp;</td>
	<td class="control"><input type="text" value="<%=compteAnnexeSoldeFormate%>" class="montantDisabled" readonly></td>
	<td>&nbsp;</td>
</tr>

<tr>
	<% if (!JadeStringUtil.isDecimalEmpty(compteAnnexeInformation)) { %>
           	<td class="label">Information&nbsp;</td>
           	<td class="control" colspan="4">
            	<input style="color:#FF0000" type="text" name="" value="<%=CACodeSystem.getLibelle(session, compteAnnexeInformation)%>" class="inputDisabled" tabindex="-1" readonly>
           	</td>
	<% } else { %>
           	<td nowrap></td>
           	<td nowrap></td>
           	<td nowrap></td>
           	<td nowrap></td>
           	<td nowrap></td>
	<% } %>
</tr>

<tr>
	<td colspan="5"><br/><hr noshade size="3"/><br/></td>
</tr>

<tr>
	<td class="label">Date faillite&nbsp;</td>
	<td class="control"><ct:FWCalendarTag name="forDateFaillite" doClientValidation="CALENDAR" value=""/></td>
	<td colspan="3">&nbsp;</td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>