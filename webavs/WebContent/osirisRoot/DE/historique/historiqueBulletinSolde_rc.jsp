<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GCA0067";
	rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.translation.CACodeSystem"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%
	globaz.osiris.db.historique.CAHistoriqueBulletinSoldeListViewBean manager = new globaz.osiris.db.historique.CAHistoriqueBulletinSoldeListViewBean();
	manager.setSession((globaz.globall.db.BSession)controller.getSession());
	manager.setForIdSection(request.getParameter("forIdSection"));
	
	globaz.osiris.db.comptes.CACompteAnnexeViewBean compteAnnexe = manager.getCompteAnnexe();
	globaz.osiris.db.comptes.CASectionViewBean section = manager.getSection();
	
	bButtonNew = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	if (section != null && compteAnnexe != null) {
%>
	<ct:menuChange displayId="options" menuId="CA-DetailSectionGauche" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=section.getIdSection()%>"/>
		<ct:menuSetAllParams key="idSection" value="<%=section.getIdSection()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=section.getIdSection()%>"/>
		<ct:menuSetAllParams key="noAffiliationId" value="<%=compteAnnexe.getIdExterneRole()%>"/>
		<ct:menuSetAllParams key="osiris.section.idExterne" value="<%=section.getIdExterne()%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=section.getIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="idPlanRecouvrement" value="<%=section.getIdPlanRecouvrement()%>"/>
		<ct:menuSetAllParams key="idRole" value="<%=compteAnnexe.getIdRole()%>"/>
		<ct:menuSetAllParams key="forIdSection" value="<%=section.getIdSection()%>"/>

		<% if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(section.getIdPlanRecouvrement())) {%>
		<ct:menuActivateNode active="no" nodeId="echeances_plan"/>
		<% } else { %>
		<ct:menuActivateNode active="yes" nodeId="echeances_plan"/>
		<% } %>
	</ct:menuChange>
	
<%
	}
%>

<script language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "osiris.historique.historiqueBulletinSolde.lister";
bFind = true;

top.document.title = "Saldo-ESR Verlauf - " + top.location.href;
// stop hiding -->
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Saldo-ESR Verlauf<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<%
	String idCompteAnnexe = "";
	String idSection = "";
	
	String compteAnnexeTitulaireEntete = "";
	String fullDescription = "";
	
	if (compteAnnexe != null) {
		idCompteAnnexe = compteAnnexe.getIdCompteAnnexe();
		idSection = section.getIdSection();
		
		compteAnnexeTitulaireEntete = compteAnnexe.getTitulaireEnteteForCompteAnnexeParSection();
		fullDescription = section.getFullDescription();
	}
%>

<tr>
		<td valign="top"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuComptes.afficher&id=<%=idCompteAnnexe%>">Konto</a>&nbsp;</td>
		<td colspan="4"><TEXTAREA cols="40" rows="4" class="libelleLongDisabled" readonly><%=compteAnnexeTitulaireEntete%></TEXTAREA></td>
</tr>
<tr>
		<td><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=idSection%>">Sektion</a>&nbsp;</td>
		<td colspan="4">
		<input type="text" name="section" class="libelleSpecialLongDisabled" readonly value="<%=fullDescription%>"/>
		<input type="hidden" name="forIdSection" value="<%=request.getParameter("forIdSection")%>">
		</td>
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