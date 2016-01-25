
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3022"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
globaz.osiris.db.process.CAInteretMoratoireManuelViewBean viewBean = (globaz.osiris.db.process.CAInteretMoratoireManuelViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.interetMoratoireManuel.simuler";
selectedIdValue=viewBean.getIdSection();

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

okButtonLabel = "Simulieren";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function SetTimer() {
		document.forms[0].lancement.value = "Lancé...";
		vSubmit();
}
function fLancement() {
	document.forms[0].lancer.value = "lancer";
	SetTimer();

}
function vSubmit() {
	document.forms[0].submit();
}

<%
	String tmpEtape = "Etappe 1/3";
	if (processLaunched) {
		tmpEtape = "Etappe 3/3";
	}
%>

top.document.title = "Verzugszinsen berechnen - <%=tmpEtape%> - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Verzugszinsen berechnen - <%=tmpEtape%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

<%
	CACompteAnnexe compte = (CACompteAnnexe) viewBean.getCompteAnnexeInformation();
	CASection section = (CASection) viewBean.getSectionInformation();
	if (compte != null && section != null) {
%>

		<ct:menuChange displayId="options" menuId="CA-DetailSectionGauche" showTab="options">
			<ct:menuSetAllParams key="id" value="<%=section.getIdSection()%>"/>
			<ct:menuSetAllParams key="idSection" value="<%=section.getIdSection()%>"/>
			<ct:menuSetAllParams key="selectedId" value="<%=section.getIdSection()%>"/>
			<ct:menuSetAllParams key="noAffiliationId" value="<%=compte.getIdExterneRole()%>"/>
			<ct:menuSetAllParams key="idCompteAnnexe" value="<%=section.getIdCompteAnnexe()%>"/>
			<ct:menuSetAllParams key="idPlanRecouvrement" value="<%=section.getIdPlanRecouvrement()%>"/>
			<ct:menuSetAllParams key="forIdSection" value="<%=section.getIdSection()%>"/>

			<% if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(section.getIdPlanRecouvrement())) {%>
			<ct:menuActivateNode active="no" nodeId="echeances_plan"/>
			<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="echeances_plan"/>
			<% } %>
		</ct:menuChange>

		<tr>
		<td valign="top"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuComptes.afficher&id=<%=compte.getIdCompteAnnexe()%>">Konto</a></td>
		<td colspan="3"><TEXTAREA cols="40" rows="4" class="libelleLongDisabled" readonly><%=compte.getTitulaireEnteteForCompteAnnexeParSection()%></TEXTAREA></td>
		</tr>
		<tr>
		<td><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=section.getIdSection()%>">Sektion</a>&nbsp;</td>
		<td colspan="3"><input type="text" name="section" class="libelleSpecialLongDisabled" readonly value="<%=section.getFullDescription()%>"/></td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
<%
	} else {
		showProcessButton = false;
	}
%>

	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">E-Mail</td>
		<td colspan="3">
        <input type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
        </td>
	</tr>

	<tr>
	    <td>Rechnungsnummer (optional)</td>
	    <td align="left" colspan="3">

	    <%
	    	String tmp = viewBean.getNumeroFactureGroupe();
	    	if (tmp == null || tmp.equals("")) {
	    		tmp = viewBean.getDefaultNumeroFactureGroupe();
	    	}
	    %>

		<input type="text" name="numeroFactureGroupe" class="libelleLong" value="<%=tmp%>"/>
		</td>
	</tr>

	<tr>
	    <td>Enddatum</td>
	    <td align="left" colspan="3">

	    <%
	    	tmp = viewBean.getDateFin();
	    	if (tmp == null || tmp.equals("")) {
	    		tmp = globaz.globall.util.JACalendar.todayJJsMMsAAAA();
	    	}
	    %>

		<ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value="<%=tmp%>"/>
		</td>
	</tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>