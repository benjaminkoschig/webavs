<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP3007";

userActionValue="apg.process.genererLot.executer";
	globaz.apg.vb.process.APGenererLotViewBean viewBean = (globaz.apg.vb.process.APGenererLotViewBean)(session.getAttribute("viewBean"));
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GENERER_LOT"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<TR>
								<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/></TD>
								<TD><INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>"></TD>
							</TR>
							<TR>
								<%if(globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION).equals(globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE)){%>
									<TD><ct:FWLabel key="JUSQU_A_DATE"/></TD>
									<TD><ct:FWCalendarTag name="prestationDateFin" value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>"/></TD>
								<%} else if(globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION).equals(globaz.prestation.api.IPRDemande.CS_TYPE_PANDEMIE)){%>
									<TD><ct:FWLabel key="JUSQU_A_DATE"/></TD>
									<TD><ct:FWCalendarTag name="prestationDateFin" value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>"/></TD>
								<%} else{%>
									<TD>&nbsp;</TD>
									<TD><INPUT type="hidden" name="prestationDateFin" value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>"/></TD>
								<%}%>
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_DESCRIPTION"/></TD>
								<TD><INPUT type="text" name="description"> </TD>
							</TR>
							<TR>
								<TD><INPUT type="hidden" name="typePrestation" value="<%=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)%>"></TD>
								<TD>&nbsp;</TD>
							</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>