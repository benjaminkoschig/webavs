<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_TDR_D"

	idEcran="PRE2016";

	RETerminerDemandeRentePrevBilViewBean viewBean = (RETerminerDemandeRentePrevBilViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");

	userActionValue=globaz.corvus.servlet.IREActions.ACTION_TERMINER_DEMANDERENTEPREVBIL+ ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.vb.process.RETerminerDemandeRentePrevBilViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.corvus.vb.process.RETerminerDemandeRentePrevBilViewBean"%><ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TDR_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						 	<TD style="width: 300px;"><ct:FWLabel key="JSP_TDR_D_DATEENVOI"/></TD>	
							<TD>
								<input	id="DateEnvoiCalcul"
										name="DateEnvoiCalcul"
										data-g-calendar="type:default"
										value="<%=JadeStringUtil.isEmpty(viewBean.getDateEnvoiCalcul())?JACalendar.todayJJsMMsAAAA():viewBean.getDateEnvoiCalcul()%>" />
							</TD>
						</TR>				
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>