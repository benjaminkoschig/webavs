<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP3008";

	userActionValue="apg.process.genererStatsOFAS.executer";
	globaz.apg.vb.process.APGenererStatsOFASViewBean viewBean = (globaz.apg.vb.process.APGenererStatsOFASViewBean)(session.getAttribute("viewBean"));
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GENERER_STATS_OFAS"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_ADRESSE_EMAIL"/>&nbsp;
							</TD>
							<TD>
								<INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>">
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_ANNEE"/>&nbsp;
							</TD>
							<TD>
							    <INPUT type="text" name="forAnnee" value="<%=viewBean.getForAnnee()%>">&nbsp;
							    <ct:FWLabel key="JSP_FORMAT_ANNEE"/>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>