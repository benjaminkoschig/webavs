<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits"%>
<%@page import="globaz.globall.db.BSession"%>
<%@ include file="/theme/capage/header.jspf" %>
<%
// Les labels de cette page commence par la préfix "JSP_PC_PARAM_ZONE_LOCALITE_R"
	idEcran="PPC0108";
	IFrameDetailHeight = "420";
	
	String dateDebut = PCCommonHandler.getStringDefault(request.getParameter("zoneLocalite.simpleLienZoneLocalite.anneeDebut"));
	String idZone = PCCommonHandler.getStringDefault(request.getParameter("zoneLocalite.simpleLienZoneLocalite.idZoneForfait"));
	String useValiderContinuer = PCCommonHandler.getStringDefault(request.getParameter("useValiderContinuer"));
	
	actionNew = actionNew+"&zoneLocalite.simpleLienZoneLocalite.anneeDebut="+dateDebut+
	            "&zoneLocalite.simpleLienZoneLocalite.idZoneForfait="+idZone+
	            "&useValiderContinuer="+useValiderContinuer;
	BSession objSession=((BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION));
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>




<%@page import="globaz.pegasus.utils.PCParametreHandler"%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<script language="JavaScript">
	var bFind = true;
	var MAIN_URL="/webavs/pegasus";
	var detailLink = "<%=actionNew%>";
	var usrAction = "<%=IPCActions.ACTION_PARAMETRES_ZONE_LOCALITE + ".lister" %>";
</script>


<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<%-- /tpl:insert --%>
				
<%@ include file="/theme/capage/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td>
			<div>
				<label for="simpleZoneForfaits.csCanton">
					<ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_D_ZONE_FOFAITS" />
				</label>   
				<ct:select name="zoneLocaliteSearch.forIdZoneForfait" wantBlank="true" defaultValue="<%=idZone%>">
				    <%for( JadeAbstractModel model:PCParametreHandler.getListZoneFofaits()){ 
				     	SimpleZoneForfaits zone = (SimpleZoneForfaits)model;
				     %>
						<ct:option value="<%=zone.getId()%>" label="<%=PCParametreHandler.getDescriptionZone(zone,objSession) %>"/>
					<%} %>
				</ct:select>
				
				<label for="localiteSearch">
					<ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_R_LOCALITE"/>
				</label>
				<input type="text" 
				       id="zoneLocaliteSearch.likeNumPostal" 
				       name = "zoneLocaliteSearch.likeNumPostal"
				       value="" />

				<label for="zoneLocaliteSearch.forDateDebut">
					<ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_R_DATE_DEBUT"/>
				</label>
				<input data-g-calendar=" " type="text" name="zoneLocaliteSearch.forDateValable" value ="<%=dateDebut%>"/>
				<input type="text" name="" value="" style="display:none"/>
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
