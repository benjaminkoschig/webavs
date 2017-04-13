<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_LOT_R"

idEcran="PRE0030";
rememberSearchCriterias = true;
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="globaz.corvus.api.lots.IRELot"%>
<%@page import="globaz.corvus.db.lots.RELotManager"%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionslot"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "corvus.lots.lot.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LOT_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_LOT_R_A_PARTIR_DE"/>
								&nbsp; 
								<input	id="fromDateCreation"
										name="fromDateCreation"
										data-g-calendar="type:default"
										value="" />
							</TD>
							<TD><ct:FWLabel key="JSP_LOT_R_TYPE"/>&nbsp;
								
								<input type="hidden" name="isTriParDateEnvoi" value="true">
								<input type="hidden" name="forCsLotOwner" value="<%=IRELot.CS_LOT_OWNER_RENTES%>">
								<ct:select name="forCsType">
									<OPTION value="<%=RELotManager.FOR_CS_TYPE_LOT_IN_DECISION_ALL%>" selected></OPTION>
									<OPTION value="<%=IRELot.CS_TYP_LOT_MENSUEL%>"><%=objSession.getCodeLibelle(IRELot.CS_TYP_LOT_MENSUEL)%></OPTION>
									<OPTION value="<%=IRELot.CS_TYP_LOT_DECISION%>"><%=objSession.getCodeLibelle(IRELot.CS_TYP_LOT_DECISION)%></OPTION>
									<OPTION value="<%=IRELot.CS_TYP_LOT_DEBLOCAGE_RA%>"><%=objSession.getCodeLibelle(IRELot.CS_TYP_LOT_DEBLOCAGE_RA)%></OPTION>
								</ct:select>								
							</TD>
							<TD><ct:FWLabel key="JSP_LOT_R_ETAT"/>&nbsp;
								<ct:select name="forCsEtat" wantBlank="true">
									<ct:optionsCodesSystems csFamille="<%=globaz.corvus.api.lots.IRELot.CS_GROUPE_ETAT_LOT%>"/>
								</ct:select>
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