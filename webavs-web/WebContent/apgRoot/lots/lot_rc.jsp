<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0028";

rememberSearchCriterias = true;

bButtonNew = false;%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%

String typeLot="";

if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {
	typeLot = globaz.prestation.api.IPRDemande.CS_TYPE_APG;
%>

	<%@page import="globaz.apg.servlet.IAPActions"%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {
	typeLot = globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE;
%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "apg.lots.lot.lister";
	
	function nouveauLot(){
		document.forms[0].elements('userAction').value = "apg.process.genererLot.afficher";
  		document.location.href="apg?userAction=apg.process.genererLot.afficher";
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LOTS"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<input type="hidden" name="forTypeLot" value="<%=typeLot%>">
							<TD><ct:FWLabel key="JSP_A_PARTIR_DE"/>&nbsp; <ct:FWCalendarTag name="fromDateCreation" value=""/></TD>
							<TD><ct:FWLabel key="JSP_ETAT"/>&nbsp;
									<SELECT name="forEtat">
										<OPTION value="<%=globaz.apg.api.lots.IAPLot.CS_OUVERT%>"><ct:FWLabel key="JSP_OUVERT"/></OPTION>
										<OPTION value="<%=globaz.apg.api.lots.IAPLot.CS_COMPENSE%>"><ct:FWLabel key="JSP_COMPENSE"/></OPTION>
										<OPTION value="<%=globaz.apg.api.lots.IAPLot.CS_VALIDE%>"><ct:FWLabel key="JSP_VALIDE"/></OPTION>
										<OPTION value="<%=globaz.apg.db.lots.APLotManager.FOR_TOUS%>"><ct:FWLabel key="JSP_TOUS"/></OPTION>
										<OPTION value="<%=globaz.apg.db.lots.APLotManager.FOR_NON_VALIDE %>" selected="selected"/><ct:FWLabel key="JSP_NON_VALIDE"/></OPTION>								
									</SELECT>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<ct:ifhasright element="<%=IAPActions.ACTION_LOTS%>" crud="u">
				<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="nouveauLot()">
				</ct:ifhasright>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>