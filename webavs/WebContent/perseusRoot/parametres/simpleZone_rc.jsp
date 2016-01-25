<%@page import="globaz.globall.db.BSession"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<%
// Les labels de cette page commence par la préfix ""
	idEcran="PPF0932";
	IFrameDetailHeight = "420";
	//actionNew = actionNew+"&anneeValeur="+annee+"&ageValeur="+age+"&useAnneAge="+useAnneAge;
	
	BSession objSession = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION));

	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		bButtonNew = true;
	}else{
		bButtonNew = false;
	}
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="perseus-optionsempty"/>

<script language="JavaScript">
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "perseus.parametres.simpleZone.lister";
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_ZONES_TITRE"/><%-- /tpl:insert --%>
				
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td>
			<label for="likeDesignation"><ct:FWLabel key="JSP_PF_PARAM_ZONE_D_DESIGNATION"/></label>
			<ct:inputText name="simpleZoneSearchModel.likeDesignation" id="likeDesignation"/>
		</td>
	</tr>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
