<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
 		globaz.draco.db.inscriptions.DSPrerempliDeclarationViewBean viewBean = (globaz.draco.db.inscriptions.DSPrerempliDeclarationViewBean)session.getAttribute("viewBean");
		userActionValue = "draco.inscriptions.prerempliDeclaration.executer";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEMailAddress())?viewBean.getEMailAddress():viewBean.getEmailAdressEcran();
		idEcran = "CDS3001";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Pré-remplir la déclaration<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<TD>
								E-Mail
							</TD>
							<TD>
								<input type="text" name="eMailAddress" value=<%=!globaz.jade.client.util.JadeStringUtil.isBlank(emailAdresse)?emailAdresse:""%> size = "40">
							</TD>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>