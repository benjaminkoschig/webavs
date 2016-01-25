<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF3008";
	globaz.naos.db.affiliation.AFSoldesAffiliesRadiesViewBean viewBean = (globaz.naos.db.affiliation.AFSoldesAffiliesRadiesViewBean) session.getAttribute("viewBean");
	userActionValue = "naos.affiliation.soldesAffiliesRadies.executer";
%>
<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_CAF3008"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD><ct:FWLabel key="EMAIL"/> : </TD>
		<TD><INPUT type="text" size="40" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="CAF3008_A_PARTIR_DE"/> : </TD>
		<TD><ct:FWCalendarTag name="fromDate" value="<%=viewBean.getFromDate()%>"/></TD>
	</TR>
	<td><ct:FWLabel key="TYPE_IMPRESSION"/> : </td>
      	<TD>
      		<input type="radio" name="typeImpression" value="pdf" <%="pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/>PDF&nbsp;
      		<input type="radio" name="typeImpression" value="xls" <%="xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/>Excel
      	</TD>
  	</TR>
  	
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>