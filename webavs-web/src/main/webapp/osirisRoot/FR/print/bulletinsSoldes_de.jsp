
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA2013"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%
globaz.osiris.db.print.CABulletinsSoldesViewBean viewBean
  = (globaz.osiris.db.print.CABulletinsSoldesViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.bulletinsSoldes.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Génération des bulletins de soldes - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>G&eacute;n&eacute;rer les bulletins de soldes<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap>Section</TD>
            <TD nowrap>
              <input type="text" name="sectionLibelle" value="<%=(!JadeStringUtil.isBlank(viewBean._getSectionIdExterne())?viewBean._getSectionIdExterne()+"-"+viewBean._getSectionLibelle():"")%>" size="60" disabled>
            </TD>
            <TD>
              <input type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>">
            </TD>
            <TD nowrap>&nbsp; </TD>
            <TD nowrap>&nbsp;</TD>
          </TR>
          <TR>
            <td nowrap>E-mail</td>
            <td nowrap>
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
            <TD>&nbsp;</TD>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap>&nbsp;</TD>
          </TR>
          <tr>
			<td nowrap>Forcer le bulletin de solde</td>
            <td nowrap>
              <input type="checkbox" name="forcerBV" id="forcerBV"> <label for="forcerBV"> Ne pas tenir compte des montants minimes.</label>
            </td>
            <td>&nbsp;</td>
            <td nowrap>&nbsp;</td>
            <td nowrap>&nbsp;</td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%
	try {
		globaz.osiris.db.comptes.CASection section = (globaz.osiris.db.comptes.CASection) viewBean.loadSection();
		globaz.osiris.db.comptes.CACompteAnnexe compteAnnexe = (globaz.osiris.db.comptes.CACompteAnnexe) viewBean.loadSectionCompteAnnexe();

%>
		<ct:menuChange displayId="options" menuId="CA-DetailSectionGauche" showTab="options">
			<ct:menuSetAllParams key="id" value="<%=section.getIdSection()%>"/>
			<ct:menuSetAllParams key="idSection" value="<%=section.getIdSection()%>"/>
			<ct:menuSetAllParams key="selectedId" value="<%=section.getIdSection()%>"/>
			<ct:menuSetAllParams key="noAffiliationId" value="<%=compteAnnexe.getIdExterneRole()%>"/>
			<ct:menuSetAllParams key="idCompteAnnexe" value="<%=section.getIdCompteAnnexe()%>"/>
			<ct:menuSetAllParams key="idPlanRecouvrement" value="<%=section.getIdPlanRecouvrement()%>"/>
			<ct:menuSetAllParams key="forIdSection" value="<%=section.getIdSection()%>"/>

			<% if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(section.getIdPlanRecouvrement())) {%>
			<ct:menuActivateNode active="no" nodeId="echeances_plan"/>
			<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="echeances_plan"/>
			<% } %>
		</ct:menuChange>
<%
	} catch (Exception e) {
		// Do nothin
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>