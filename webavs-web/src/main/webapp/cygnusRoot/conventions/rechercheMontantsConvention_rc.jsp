<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.conventions.RFConventionViewBean "%>
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
	idEcran="PRF0024";

	RFConventionViewBean viewBean = (RFConventionViewBean) request.getAttribute("viewBean");
	
	IFrameListHeight = "220";
	IFrameDetailHeight = "290";
	
	bButtonNew = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty"/>

<SCRIPT language="JavaScript">

	//afficher un message d'erreur si le viewBean est en erreur
	
	bFind = true;
		
	usrAction = "<%=IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION%>.lister";
	actionNew = "<%=IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION%>.afficher&_method=add";
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
<%@ include file="/theme/capage/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_RECHERCHE_MNT_CONV_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD>
		<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
			<TR>
				<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_NUMERO"/></TD>											
				<TD style="font-weight: bold ;">&nbsp;<%=viewBean.getIdConvention() %></TD>				
				<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_LIBELLE"/></TD>
				<TD style=" font-weight: bold ;">&nbsp;<%=viewBean.getLibelle() %></TD>
				<INPUT type="hidden" name="libelle" value="<%=viewBean.getLibelle() %>"/>			
				<INPUT type="hidden" name="forIdConvention" value="<%=viewBean.getIdConvention() %>"/>	
			</TR>
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<TR>			
				<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_FROM"/></TD>
				<TD><input data-g-calendar="type:month"  name="forFromDate" value="<%=viewBean.getFromDate()%>"/>&nbsp;[mm.aaaa]</TD>																		
			</TR>			
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_BENEFICIAIRE"/></TD>
				<TD colspan="5"><ct:FWListSelectTag data="<%=viewBean.getCsTypeBeneficiairePCData()%>" defaut="" name="forCsTypeBeneficiairePc" /></TD>		
			</TR>			
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_EN_COURS"/></TD>
				<TD colspan="5"><INPUT type="checkbox" name="forEnCours" <%=viewBean.getEnCours().booleanValue()?"CHECKED":""%>/></TD>				
			</TR>			
			<TR><TD colspan="6">&nbsp;</TD></TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_TRI"/></TD>
				<TD colspan="5"><ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="forOrderBy" /></TD>		
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