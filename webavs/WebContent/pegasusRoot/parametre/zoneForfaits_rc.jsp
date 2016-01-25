<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ include file="/theme/capage/header.jspf" %>
<%
// Les labels de cette page commence par la préfix ""
	idEcran="PPC0107";
	IFrameDetailHeight = "420";
	//actionNew = actionNew+"&anneeValeur="+annee+"&ageValeur="+age+"&useAnneAge="+useAnneAge;
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<script language="JavaScript">
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "<%=IPCActions.ACTION_PARAMETRES_ZONE_FORFAIT + ".lister" %>";
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
				
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td>
			<div>
				<label for="forAge"><ct:FWLabel key="JSP_PC_PARAM_ZONE_FORFAIT_D_CANTON"/></label>
				<ct:select name="simpleZoneForfaitsSearch.forCsCanton" wantBlank="true">
					<ct:optionsCodesSystems csFamille="PYCANTON"/>
				</ct:select>
			</div>
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
