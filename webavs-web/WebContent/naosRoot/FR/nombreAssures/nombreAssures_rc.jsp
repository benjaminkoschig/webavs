<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0023";
	globaz.naos.db.nombreAssures.AFNombreAssuresViewBean viewBean = (globaz.naos.db.nombreAssures.AFNombreAssuresViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	bFind = true;
	usrAction = "naos.nombreAssures.nombreAssures.lister";
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Gestion du nombre d'assur&eacute;s
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<%
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAffiliationId())) {
								bButtonNew = false;
							} else {
								actionNew += "&affiliationId="+viewBean.getAffiliationId();
							}
						%>
						<naos:AFInfoAffiliation name="forAffiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="100"/>
						
						<TR>
		                    <TD nowrap width="128">Depuis l'ann&eacute;e</TD>
                        	<TD nowrap width="100"> 
                        		<INPUT type="text" name="fromAnnee" value="" size="5" maxlength="4">
                        		<INPUT type="hidden" name="forAssuranceId" value="<%=viewBean.getAssuranceId()%>">
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