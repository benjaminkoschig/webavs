<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.pegasus.utils.PCParametreHandler"%>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@ include file="/theme/capage/header.jspf" %>
<%
// Les labels de cette page commence par la préfix ""
	idEcran="PPC0109";
	IFrameDetailHeight = "420";
	String rootPath = servletContext+(mainServletPath+"Root");
	String idZone = PCCommonHandler.getStringDefault(request.getParameter("zoneLocalite.simpleLienZoneLocalite.idZoneForfait"));
	BSession objSession = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION));
	//actionNew = actionNew+"&anneeValeur="+annee+"&ageValeur="+age+"&useAnneAge="+useAnneAge;
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>



<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/formTableLess.css">
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<script language="JavaScript">
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "<%=IPCActions.ACTION_PARAMETRES_FORFAIT_PRIME_ASSURANCE_MALADIE + ".lister" %>";
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
				
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td>
			<div class="formTableLess">
				<label for="forfaitPrimesAssuranceMaladieSearch.forDateValable">
					<ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_R_ANNEE_VALIDITE"/>
				</label>
				
				<ct:inputText notation="data-g-calendar=' '" name="forfaitPrimesAssuranceMaladieSearch.forDateValable" />
				
				<label for="forfaitPrimesAssuranceMaladieSearch.forCsTypePrime">
					<ct:FWLabel key="JSP_PC_PARAM_FORFAIT_PRIME_AM_R_TYPE_DE_PRIME"/>
				</label>
				<ct:select name="forfaitPrimesAssuranceMaladieSearch.forCsTypePrime" 
			           wantBlank="true" notation="data-g-select='mandatory:true'">
					<ct:optionsCodesSystems csFamille="PCTYPEPRI"/>
				</ct:select>
				<label for="forfaitPrimesAssuranceMaladieSearch.forIdZoneForfaits">
					<ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_D_ZONE_FOFAITS" />
				</label>   
				<ct:select name="forfaitPrimesAssuranceMaladieSearch.forIdZoneForfaits" wantBlank="true" defaultValue="<%=idZone%>">
				    <%for( JadeAbstractModel model:PCParametreHandler.getListZoneFofaits()){ 
				     	SimpleZoneForfaits zone = (SimpleZoneForfaits)model;
				     %>
						<ct:option value="<%=zone.getId()%>" label="<%=PCParametreHandler.getDescriptionZone(zone,objSession) %>"/>
					<%} %>
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
