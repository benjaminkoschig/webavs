<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.pavo.db.bta.CIDecisionBtaViewBean"%>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
 		CIDecisionBtaViewBean viewBean = (CIDecisionBtaViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.bta.decisionBta.executer";
		idEcran = "CCI2014";
		//out.print("id dossier "+viewBean.getIdDossierBta());
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="options" menuId="dossierBta-detail" showTab="options">
		<ct:menuSetAllParams key="idDossierBta" value="<%=viewBean.getId()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der BGS Verfügungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						
						<TR>
							<TD>
								Jahr
							</TD>
							<TD>
								<input type="text" name="annee" value="<%=viewBean.getAnnee()%>" size="3" maxlength="4">
							</TD>
						</TR>
						<TR>
							<TD>
								E-Mail Adresse
							</TD>
							<TD>
								<input type="text" name="eMailAddress" value="<%=emailAdresse%>" size = "40">
							</TD>
						</TR>
						<%if(!viewBean.isDossierOuvert()){%>
						<TR>
							<td nowrap colspan="7">&nbsp;</td>
							<td nowrap colspan="7">&nbsp;</td>
						</TR>
						<TR>
							<TD nowrap colspan="7">
								<font color="red"><b>ACHTUNG, das Dossier muss im Status Offen sein</b></font>
							</TD>
							<TD nowrap colspan="7">
								&nbsp;
							</TD>
						</TR>
						<%showProcessButton = false;}%>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>