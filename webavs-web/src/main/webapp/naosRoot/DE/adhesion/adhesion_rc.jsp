<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@page import="globaz.naos.application.AFApplication"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0004";
	globaz.naos.db.adhesion.AFAdhesionViewBean viewBean = (globaz.naos.db.adhesion.AFAdhesionViewBean)session.getAttribute ("viewBean");
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
	usrAction = "naos.adhesion.adhesion.lister";
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
				Verwaltung der Beitritte
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<%
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAffiliationId())) {
								bButtonNew = false;
							} else {
								actionNew +=  "&affiliationId=" + viewBean.getAffiliationId();
							}
						%>
	        			<naos:AFInfoAffiliation name="forAffiliationId" affiliation="<%=viewBean.getAffiliation()%>"/>
          				<%-- /tpl:put --%>
<!-- ***************** ANCIENNEMENT import bodyButtons.jspf, ATTENTION SI MODIF LES REPERCUTER EGALEMENT ICI *******************-->          				
<TR>
							<TD height="20">
								<INPUT type="hidden" name="userAction" value="">
								<INPUT type="hidden" name="_sl" value="">
								<INPUT type="hidden" name="_method" value="">
								<INPUT type="hidden" name="_valid" value="">
								<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
								
							</TD>
							<td>&nbsp</td>
							<td>&nbsp</td>
							<TD valign="top" align="right" width="100">
								<%
				             		String gedAffilieNumero = viewBean.getAffiliation().getAffilieNumero();
				             		String gedNumAvs = viewBean.getAffiliation().getTiers().getNumAvsActuel();
				             		String gedIdTiers = viewBean.getAffiliation().getIdTiers();
				             		String gedIdRole = "";
				             		
				             	%>
								
								<%@ include file="/theme/gedCall.jspf" %>
							</td>
						</TR>
					</TBODY>
				</TABLE>
				
			</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="2" align="right">
				<%if (bShowExportButton) {%>
					<INPUT type="button" name="btnExport" value="<%=btnExportLabel%>" onClick="onExport();">
				<%}%>
				<%if (bButtonFind) {%>
					<INPUT type="submit" name="btnFind" value="<%=btnFindLabel%>">
				<%} if (bButtonNew) {%>
					<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="onClickNew();btnNew.onclick='';document.location.href='<%=actionNew%>'">
				<%}%>
<!-- ***************** FIN import bodyButtons.jspf *******************-->   
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>