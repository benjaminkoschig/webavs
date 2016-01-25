<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%  
	idEcran = "CAF3003";
	globaz.naos.db.rentier.AFExportViewBean viewBean = (globaz.naos.db.rentier.AFExportViewBean)session.getAttribute("viewBean");
	userActionValue = "naos.rentier.export.executer";
	subTableWidth="";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Streichung der Rentner
      				<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD class="text">Beginndatum</TD>
							<TD class="text">
								&nbsp;<input type="text" name="dateDebut" value="<%=globaz.naos.db.rentier.AFExportViewBean.getDefaultDateDebut()%>">
							</TD>
						</TR>
						<TR> 
							<TD class="text">Enddatum</TD>
							<TD class="text">
								&nbsp;<input type="text" name="dateFin" value="<%=globaz.naos.db.rentier.AFExportViewBean.getDefaultDateFin()%>">
							</TD>
						</TR>
						<TR> 
							<TD class="text">E-Mail</TD>
							<TD class="text">
								&nbsp;<input type="text" name="eMailAddress" value="<%=((viewBean.getSession().getUserEMail()==null)?"":viewBean.getSession().getUserEMail())%>">
							</TD>
						</TR>
						<TR> 
							<TD class="text">Periodizitätsänderung</TD>
							<TD class="text">
								&nbsp;<INPUT type="checkbox" name="changementPeriodicite" <%=(viewBean.getChangementPeriodicite())?  "unchecked": ""%> >
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