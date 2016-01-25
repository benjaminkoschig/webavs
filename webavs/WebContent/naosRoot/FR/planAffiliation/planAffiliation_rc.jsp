<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0006";
	globaz.naos.db.planAffiliation.AFPlanAffiliationViewBean viewBean = (globaz.naos.db.planAffiliation.AFPlanAffiliationViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.planAffiliation.planAffiliation.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Gestion des Plans d'affiliation
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<%
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAffiliationId())) {
								bButtonNew = false;
							} else {
								actionNew += "&affiliationId=" + viewBean.getAffiliationId();
							}
						%>
						<naos:AFInfoAffiliation name="forAffiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="100" /> 

						<TR>
		                    <TD nowrap width="128">Libelle</TD>
                        	<TD nowrap>
                        		<INPUT type="text" name="fromLibelle" size="25" maxlength="25" tabindex="1">
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