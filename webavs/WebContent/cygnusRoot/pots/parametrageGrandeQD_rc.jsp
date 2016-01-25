<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>

<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<script language="JavaScript" src="<%=servletContext%>/scripts/menu.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js">	
</script>
<%
	idEcran="PRF0031";
	
	
	IFrameListHeight = "220";
	IFrameDetailHeight = "1000";
	
	bButtonNew = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty"/>

<SCRIPT language="JavaScript">
	
	bFind = true;
	usrAction = "<%=IRFActions.ACTION_PARAMETRAGE_GRANDE_QD%>.lister";
	actionNew = "<%=IRFActions.ACTION_PARAMETRAGE_GRANDE_QD%>.afficher&_method=add";
	detailLink = "<%=actionNew%>";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_PARAMETRAGE_QD_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD  width="100%">
		<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
			<TR>			
				<TD><ct:FWLabel key="JSP_RF_PARAMETRAGE_QD_DATE_DEBUT"/>&nbsp;</TD>
				<TD><input data-g-calendar=" "  name="forDateDebut" value="" />&nbsp;</TD>
				<TD><ct:FWLabel key="JSP_RF_PARAMETRAGE_QD_DATE_FIN"/>&nbsp;</TD>
				<TD><input data-g-calendar=" "  name="forDateFin" value="" /></TD>																		
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