<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
idEcran ="GTI6006";
bButtonNew = bButtonNew && ConfigReader.isAllMergerClassesOk();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<%@page import="globaz.pyxis.process.fusiontiers.util.ConfigReader"%><ct:menuChange displayId="options" menuId="TIMenuVide"/>
<SCRIPT>
usrAction = "pyxis.fusiontiers.fusionTiers.lister";
reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="FUSION_TIERS"/><%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain"  --%>
	<tr>
		<td><ct:FWLabel key="STATUT_FUSION_TIERS"/>&nbsp;</td>
		<td>
			<ct:FWCodeSelectTag name="forCsResultatStatut" defaut="" wantBlank="true" codeType="PYFUSRES"/>
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