<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix ""
	idEcran="PPC0116";
	IFrameDetailHeight = "420";
	String annee = PCCommonHandler.getStringDefault(request.getParameter("anneeValeur"));
	String age = PCCommonHandler.getStringDefault(request.getParameter("ageValeur"));
	String useAnneAge = PCCommonHandler.getStringDefault(request.getParameter("useAnneAge"));
	actionNew = actionNew+"&anneeValeur="+annee+"&ageValeur="+age+"&useAnneAge="+useAnneAge;
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>




<%@page import="globaz.pegasus.utils.PCCommonHandler"%><ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "<%=IPCActions.ACTION_PARAMETRES_CONVERSION_RENTE + ".lister" %>";
	 
	/*$(function(){
	$("#forAnnee").change(function(){
		var frameDetail =$('[name="fr_detail"]').contents();
		$("[name=annee]",frameDetail).val($(this).val());
	});
});
  */
	  

	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
	<%-- tpl:put name="zoneTitle" --%>
	<ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_R_TITRE"/>
	<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
		<tr>
		<td>
			<table border="0" cellspacing="0" cellpadding="0" width="900">
				<tr>
					<TD><label for="forAge"><ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_R_AGE"/></label></TD>
					<TD><input type="text" name="conversionRenteSearch.forAge" /></TD>
					<TD><label for="forAnnee"><ct:FWLabel key="JSP_PC_PARAM_CONVERSION_RENTE_R_ANNEE"/></label></TD>
					<TD><input type="text" data-g-calendar="type:month"  name="conversionRenteSearch.forAnnee" id="forAnnee" value="<%=annee%>"/> </TD>
				</tr>
			</table>
		</td>
	</tr>
	<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>