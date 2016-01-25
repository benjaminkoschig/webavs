<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
 		DSImprimerDeclarationViewBean viewBean = (DSImprimerDeclarationViewBean)session.getAttribute("viewBean");
		userActionValue = "draco.declaration.imprimerDeclaration.executer";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
		selectedIdValue = viewBean.getId();
		idEcran = "CDS3005";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.draco.db.declaration.DSImprimerDeclarationViewBean"%>
<ct:menuChange displayId="options" menuId="DS-OptionsDeclaration" showTab="options">
		<ct:menuSetAllParams key="idDeclaration" value="<%=viewBean.getId()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Lohnbescheinigung ausdrucken<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<TD>
								E-Mail Adresse
							</TD>
							<TD>
								<input type="text" name="eMailAddress" value="<%=!globaz.jade.client.util.JadeStringUtil.isBlank(emailAdresse)?emailAdresse:""%>" size = "40">
							</TD>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>