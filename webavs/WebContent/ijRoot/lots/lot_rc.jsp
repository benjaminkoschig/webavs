<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ0020";

rememberSearchCriterias = true;

bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.servlet.IIJActions"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "ij.lots.lot.lister";
	
	function nouveauLot(){
		document.forms[0].elements('userAction').value = "ij.process.genererLot.afficher";
  		document.location.href="ij?userAction=ij.process.genererLot.afficher";
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LOTS"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_A_PARTIR_DE"/>&nbsp; <ct:FWCalendarTag name="fromDateCreation" value=""/></TD>
							<TD><ct:FWLabel key="JSP_ETAT"/>&nbsp;
									<SELECT name="forCsEtat">
										<OPTION value="<%=globaz.ij.api.lots.IIJLot.CS_OUVERT%>"><ct:FWLabel key="JSP_OUVERT"/></OPTION>
										<OPTION value="<%=globaz.ij.api.lots.IIJLot.CS_COMPENSE%>"><ct:FWLabel key="JSP_COMPENSE"/></OPTION>
										<OPTION value="<%=globaz.ij.api.lots.IIJLot.CS_VALIDE%>"><ct:FWLabel key="JSP_VALIDE"/></OPTION>
										<OPTION value="<%=globaz.ij.db.lots.IJLotManager.FOR_TOUS%>"><ct:FWLabel key="JSP_TOUS"/></OPTION>
										<OPTION value="<%=globaz.ij.db.lots.IJLotManager.FOR_NON_VALIDE %>" selected="selected"/><ct:FWLabel key="JSP_NON_VALIDE"/></OPTION>								
									</SELECT>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<ct:ifhasright element="<%=IIJActions.ACTION_GENERER_LOT%>" crud="c">
					<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="nouveauLot()">
				</ct:ifhasright>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>