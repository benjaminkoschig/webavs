<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix ""
	idEcran="PPC0100";
	IFrameDetailHeight = "420";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%><ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=IPCActions.ACTION_PARAMETRES_VARIABLE_METIER + ".lister" %>";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
	<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_PARAM_VARMET_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
		<TR>
		<TD>
			<TABLE border="0" cellspacing="0" cellpadding="0" width="900">
				<TR>
					<TD><label for="du"><ct:FWLabel key="JSP_PC_PARAM_VARMET_R_DATE_VALABLE"/></label></TD>
					<TD><input type="text" name="variableMetierSearch.forDateValable" value="" data-g-calendar="mandatory:false,type:month"/></TD>
					<TD><label for="du"><ct:FWLabel key="JSP_PC_PARAM_VARMET_R_TYPE_VARIABLE"/></label></TD>
					<TD><ct:FWCodeSelectTag notation="data-g-selectautocomplete=' '" codeType="PCTYPVMET" name="variableMetierSearch.forCsTypeVariableMetier" wantBlank="true" defaut="blank"/></TD>
				</TR>
			</TABLE>
		</TD>
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