<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@page import="globaz.naos.db.affiliation.AFLettreBienvenueViewBean"%>
<%
globaz.naos.db.affiliation.AFLettreBienvenueViewBean viewBean = (globaz.naos.db.affiliation.AFLettreBienvenueViewBean) session.getAttribute("viewBean");
idEcran = "CAF3013";
userActionValue = "naos.affiliation.lettreBienvenue.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Unterstellungsbrief<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% 
						if (viewBean.getIdAffiliation() != null && viewBean.getIdAffiliation().length() != 0) { %>
						<naos:AFInfoAffiliation name="idAffiliation" affiliation="<%=viewBean.getAff()%>"/>
						<% } %>
						<TR>
							<TD>E-Mail</TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>"></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>