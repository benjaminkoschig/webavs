<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran = "CAF3002";
	globaz.naos.db.tent.AFExportViewBean viewBean = (globaz.naos.db.tent.AFExportViewBean)session.getAttribute("viewBean");
	userActionValue = "naos.tent.export.executer";
	subTableWidth="";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Nouvelles affiliations personnelles
					<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD class="text">Date d&eacute;but</TD>
							<TD class="text">
								<INPUT type="text" name="dateDebut" value="<%=globaz.naos.db.tent.AFExportViewBean.getDefaultDateDebut()%>">
							</TD>
						</TR>
						<TR> 
							<TD class="text">Date fin</TD>
							<TD class="text">
								<INPUT type="text" name="dateFin" value="<%=globaz.naos.db.tent.AFExportViewBean.getDefaultDateFin()%>">
							</TD>
						</TR>
						<TR> 
							<TD class="text">Email</TD>
							<TD class="text">
								<INPUT type="text" name="eMailAddress" value="<%=((viewBean.getSession().getUserEMail()==null)?"":viewBean.getSession().getUserEMail())%>">
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<% } %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>