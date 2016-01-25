<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
idEcran ="GTI6008";
bButtonFind = false;
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<ct:menuChange displayId="options" menuId="TIMenuVide"/>
<SCRIPT>
usrAction = "pyxis.fusiontiers.fusionTiersEtat.lister";
bFind = true;
reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="FUSION_TIERS_ETAT"/><%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain"  --%>
	<tr>
		<td><ct:FWLabel key="DATE_FUSION"/>&nbsp;</td>
		<td>
			<input type="hidden" name="forIdFusionsTiers" value="<%=request.getParameter("idFusionsTiers")%>"/>
			<input type="text" value="<%=request.getParameter("dateFusion")%>" class="dateDisabled" readonly="readonly"/>
		</td>
	</tr>
	<tr>
	    <td><ct:FWLabel key="TIERS_DEFINITIF"/>&nbsp;</td>
        <td>
			<input type="text" value="<%=request.getParameter("masterNSS")%>" class="libelleLongDisabled" readonly="readonly"/>
			&nbsp;
			<input type="text" value="<%=request.getParameter("masterNom")%>" class="libelleLongDisabled" readonly="readonly"/>
		</td>
	</tr>
	<tr>
	    <td><ct:FWLabel key="TIERS_DESACTIVER"/>&nbsp;</td>
        <td>
			<input type="text" value="<%=request.getParameter("slaveNSS")%>" class="libelleLongDisabled" readonly="readonly"/>
			&nbsp;
			<input type="text" value="<%=request.getParameter("slaveNom")%>" class="libelleLongDisabled" readonly="readonly"/>
		</td>
	</tr>
	<tr>
		<td><ct:FWLabel key="STATUT_FUSION_TIERS"/>&nbsp;</td>
		<td>
			<input type="text" value="<%=request.getParameter("statutFusion")%>" class="libelleDisabled" readonly="readonly"/>
		</td>
	</tr>
	 <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>