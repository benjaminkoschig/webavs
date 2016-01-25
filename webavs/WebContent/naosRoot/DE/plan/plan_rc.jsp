<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0037";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.plan.plan.lister";
	bFind = true;
	detailLink = servlet+"?userAction=naos.plan.plan.afficher&selectedId="; 
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Verwaltung der Versicherungspläne
					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          				<%
							//globaz.naos.db.plan.AFPlanViewBean viewBean = (globaz.naos.db.plan.AFPlanViewBean)session.getAttribute ("viewBean");
							IFrameListHeight = "100";
							IFrameDetailHeight ="370";	
						%>
			          	<TR> 
			            	<TD nowrap width="128">&nbsp;</TD>
			            	<TD height="20">
								<INPUT type="hidden" name="selectedId" value='<%=request.getParameter("selectedId")%>'>
							</TD>
			            	<TD nowrap colspan="2" width="298">&nbsp;</TD>
			          	</TR>
          				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>