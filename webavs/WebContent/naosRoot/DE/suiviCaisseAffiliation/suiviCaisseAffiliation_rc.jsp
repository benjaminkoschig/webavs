<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@page import="globaz.naos.application.AFApplication"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0058";
	globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationViewBean viewBean = 
		(globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationViewBean)session.getAttribute("viewBean");
	String gedFolderType = "";
	String gedServiceName = "";
	try {
		globaz.globall.api.BIApplication naosApp = globaz.globall.api.GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS);
		gedFolderType =  naosApp.getProperty("ged.folder.type", "");
		gedServiceName =  naosApp.getProperty("ged.servicename.id", "");
	} catch (Exception e){
		// Le reste de la page doit tout de même fonctionner
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.suiviCaisseAffiliation.suiviCaisseAffiliation.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" checkAdd="no"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options" checkAdd="no">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>" checkAdd="no"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>" checkAdd="no"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Verfolgung der Versicherungen
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAffiliationId())) {
								bButtonNew = false;
							} else {
								actionNew += "&affiliationId="+ viewBean.getAffiliationId();
							}
						%>
						<naos:AFInfoAffiliation name="forAffiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="100" /> 

						<TR>
							<TD nowrap width="128" height="31">Kassenart</TD>
	 						<TD nowrap colspan="3">
								<ct:FWCodeSelectTag 
									name="forGenreCaisse" 
									defaut=""
									codeType="VEGENCAISS"
									wantBlank="true"/>
							</TD>
							<TD valign="top" align="right" width="100">
								<%
				             		String gedAffilieNumero = viewBean.getAffiliation().getAffilieNumero();
				             		String gedNumAvs = viewBean.getAffiliation().getTiers().getNumAvsActuel();
				             		String gedIdTiers = viewBean.getAffiliation().getIdTiers();
				             		String gedIdRole = "";
				             		
				             	%>
								
								<%@ include file="/theme/gedCall.jspf" %></td>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>