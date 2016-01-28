<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF0015";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.couverture.couverture.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Verwaltung der Versicherungsdeckungen
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<%
							globaz.naos.db.couverture.AFCouvertureViewBean viewBean = (globaz.naos.db.couverture.AFCouvertureViewBean)session.getAttribute ("viewBean");
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getPlanCaisseId())) {
								bButtonNew = false;
							} else {
								actionNew += "&planCaisseId=" + viewBean.getPlanCaisseId();
							}
						%>
						<TR>
		            		<TD nowrap colspan="3"> 
			                	<input type="hidden" name="forPlanCaisseId" value="<%=viewBean.getPlanCaisseId()%>">
			                </TD>
		                    <TD nowrap width="128">Für das Datum vom</TD>
							<TD nowrap width="30" height="31"></TD>
                        	<TD nowrap width="269"> 
                        		<ct:FWCalendarTag name="forDate" value="" />
                        	</TD>              
 		 				</TR>
          				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>