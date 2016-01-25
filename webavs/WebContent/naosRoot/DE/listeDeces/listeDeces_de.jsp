<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 

<%@page import="globaz.naos.db.listeDeces.AFListeDecesViewBean"%>

<%
 	idEcran = "CAF0071";
  	AFListeDecesViewBean viewBean = (AFListeDecesViewBean)session.getAttribute("viewBean");
  	userActionValue = "naos.listeDeces.listeDeces.executer";
 %>
 
<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="CAF0071_TITRE_ECRAN_IMPRESSION_DECES"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 
						
	<TR> 
		<TD><ct:FWLabel key="CAF0071_DATE_DECES"/></TD>
		<TD><ct:FWCalendarTag name="dateDeces" doClientValidation="CALENDAR" value="<%=viewBean.getDateDeces()%>" /></TD>
		<TD>&nbsp;</TD>
	</TR>
	<TR> 
		<TD><ct:FWLabel key="EMAIL"/></TD>
		<TD><INPUT type="text" size="40" name="email" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>" /></TD>
		<TD>&nbsp;</TD>
	</TR>
	
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 

	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
	
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>