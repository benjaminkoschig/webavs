<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<% idEcran="IEN0150"; %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--<%@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" %> --%>

<SCRIPT language="javaScript">
	usrAction = "amal.formule.csgroupchamps.lister";
	bFind=true;
</SCRIPT>

<%
	bButtonNew=false;
		
	btnFindLabel = objSession.getLabel("RECHERCHER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	btnExportLabel = objSession.getLabel("EXPORT");
%>

<SCRIPT>
	reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ai:AIProperty key="LISTE_GROUPE_CHAMPS"/><ct:FWLabel key="LIBELLE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- Groupe --%>
						<TR>
							<TD width="20%" style="width:180px;"><ai:AIProperty key="GROUP"/><ct:FWLabel key="LIBELLE"/></TD>
							<TD width="25%" style="width:320px;"><INPUT type="text" name="fromLibelle" value=""></TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>									
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>