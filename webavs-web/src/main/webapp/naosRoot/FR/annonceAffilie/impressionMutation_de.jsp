<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@page import="globaz.naos.db.annonceAffilie.AFImpressionMutationViewBean"%>
<%
globaz.naos.db.annonceAffilie.AFImpressionMutationViewBean viewBean = (globaz.naos.db.annonceAffilie.AFImpressionMutationViewBean) session.getAttribute("viewBean");
idEcran = "CAF3001";
userActionValue = "naos.annonceAffilie.impressionMutation.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Générer avis de mutations<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>Date d'impression</TD>
							<TD><ct:FWCalendarTag name="dateAnnonce" value="<%=viewBean.getDateAnnonce()%>"/></TD>
						</TR>
						<TR>
							<TD>E-Mail</TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>"></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>