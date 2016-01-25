<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp"
	import="globaz.globall.http.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/capage/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page
	import="globaz.cygnus.vb.motifsDeRefus.RFRechercheMotifsDeRefusViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=servletContext%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=servletContext%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<script language="JavaScript" src="<%=servletContext%>/scripts/menu.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js">	
</script>
<%
	idEcran="PRF0033";

	RFRechercheMotifsDeRefusViewBean viewBean = (RFRechercheMotifsDeRefusViewBean) request
			.getAttribute("viewBean");

	IFrameListHeight = "220";
	IFrameDetailHeight = "1000";

	bButtonNew = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"
	showTab="menu" />
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" />

<SCRIPT language="JavaScript">	
	
	bFind = true;
		
	usrAction = "<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.lister";
	actionNew = "<%=IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS%>.afficher&_method=add";// sur de ça?
	detailLink = "<%=actionNew%>";

	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.lyra.servlet.ILYActions.ACTION_ECHEANCES%>.chercher";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
	

	function loadFrames() {
		// prevenir les cursor state not valid exception
		if(bFind) {
			document.fr_detail.location.href = detailLink + "&_valid=new";
		}
	}
	

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_RF_RECHERCHE_MOTIF_REFUS_TITRE" />
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD>
	<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
		<TR>
			<TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION" />&nbsp;</TD>
			<TD><INPUT type="text" name="forDescriptionFR"
				value="" />&nbsp;&nbsp;</TD>
			<!-- <TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION_IT" />&nbsp;</TD> -->
			<!--<TD><INPUT type="text" name="forDescriptionIT"
				value="" />&nbsp;&nbsp;</TD> -->
			<!--<TD><ct:FWLabel key="JSP_RF_SAISIE_MOTIF_REFUS_DESCRIPTION_DE" />&nbsp;</TD> -->
			<!--<TD><INPUT type="text" name="forDescriptionDE"
				value="" /></TD>								 -->
		</TR>
	</TABLE>
	</TD>
</TR>
<input type="hidden" value="2" name="forIsMotifRefusSysteme"/>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf"%>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf"%>
<%-- /tpl:insert --%>