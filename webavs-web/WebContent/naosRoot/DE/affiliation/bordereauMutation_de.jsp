<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@page import="globaz.naos.db.affiliation.AFBordereauMutationViewBean"%>
<%
AFBordereauMutationViewBean viewBean = (AFBordereauMutationViewBean) session.getAttribute("viewBean");
idEcran = "CAF3010";
userActionValue = "naos.affiliation.bordereauMutation.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Mutationsauszug<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
						<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>"/>
						<% } %>
						<TR>
							<TD>E-Mail</TD>
							<TD><INPUT type="text" name="email" value="<%=viewBean.getEmail()%>"></TD>
						</TR>
						<TR>
							<TD>Datum der Situation</TD>
							<TD><ct:FWCalendarTag name="dateSituation" value="<%=viewBean.getDateSituation()%>"/></TD>
						</TR>
						<TR>
							<TD>Beobachtungen</TD>
							<TD><TEXTAREA name="observations" cols="80" rows="5"><%=viewBean.getObservations()%></TEXTAREA></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>