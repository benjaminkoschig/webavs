<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF3006";
	globaz.naos.db.releve.AFGestionReleveViewBean viewBean = (globaz.naos.db.releve.AFGestionReleveViewBean)session.getAttribute("viewBean");
	userActionValue="naos.releve.gestionReleve.executer";
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Suivi des relevés<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						<TD width="150">Adresse e-mail</TD>
						<TD><INPUT name="email" size="20" type="text" style="text-align : left;" value="<%=viewBean.getEmail()%>"></TD>
						</TR>
						<TR>
						<TD width="150">Période</TD>
						<TD><INPUT name="periode" type="text" value="<%=viewBean.getPeriode()%>"></TD>
						</TR>
						<TR>
						<TD width="150"><input type="hidden" name="genererRappel" value="true"></TD>
						</TR>
						<!--TD width="131">Générer rappel</TD>
						<TD><input name="genererRappel" size="5" type="checkbox" style="text-align : right; "></TD>
						</TR>
						<TR>
						<TD width="131">Générer sommation</TD>
						<TD><input name="genererSommation" size="5" type="checkbox" style="text-align : right"></TD>
						</TR>
						<TR>
						<TD width="131">Générer taxation</TD>
						<TD><input name="genererTaxation" size="5" type="checkbox" style="text-align : right"></TD>
						</TR-->
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>