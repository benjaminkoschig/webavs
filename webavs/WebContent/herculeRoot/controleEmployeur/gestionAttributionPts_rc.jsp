<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hercule.db.controleEmployeur.CEGestionAttributionPtsListViewBean"%>

<%
	idEcran="CCE0001";

	String forNumAffilie = "";
	if(request.getParameter("numAffilie") != null) {
		forNumAffilie = (String) request.getParameter("numAffilie");
	}else if (request.getParameter("selectedId") != null) {
		forNumAffilie = (String) request.getParameter("selectedId");
	}
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";

	usrAction = "hercule.controleEmployeur.gestionAttributionPts.lister";
	bFind = true;
</SCRIPT>

<ct:menuChange displayId="menu" menuId="CE-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CE-OptionsDefaut"/>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_HISTORIQUE_EVALUATION"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	  	<TR>
			<td nowrap width="100"><ct:FWLabel key="NUMERO_AFFILIE"/></td>
			<td><INPUT type="text" name="likeNumAffilie" id="likeNumAffilie" class="libelle" value="<%=forNumAffilie%>" size="17" maxlength="15" /></TD>
			<TD width="30">&nbsp;</TD>
        	<TD nowrap width="200"><ct:FWLabel key="DATE_MODIFICATION"/></TD>
            <TD nowrap><ct:FWCalendarTag name="forLastModification" value="" errorMessage="la date de modification est incorrecte" doClientValidation="CALENDAR"/></TD>
			<TD width="30">&nbsp;</TD>
			<td nowrap width="100"><ct:FWLabel key="TRIER_PAR"/></td>
			<td>			
				<select name="orderBy" size="1">
  					<option value="ATT.MALNAF ASC" selected="selected"><ct:FWLabel key="NUMERO_AFFILIE"/>&nbsp;<ct:FWLabel key="CROISSANT"/></option>
   					<option value="ATT.MALNAF DESC"><ct:FWLabel key="NUMERO_AFFILIE"/>&nbsp;<ct:FWLabel key="DECROISSANT"/></option>
  					<option value="PSPYORDER asc"><ct:FWLabel key="DATE_MODIFICATION"/>&nbsp;<ct:FWLabel key="CROISSANT"/></option>
  					<option value="PSPYORDER desc"><ct:FWLabel key="DATE_MODIFICATION"/>&nbsp;<ct:FWLabel key="DECROISSANT"/></option>
				</select> 
			</TD>
		</TR>
        <TR>
        	<TD nowrap width="100"><ct:FWLabel key="UTILISATEUR"/></TD>
            <TD nowrap><INPUT type="text" name="forLastUser" class="libelle" size="13"/></TD>
        	<TD width="30">&nbsp;</TD>
        	<TD nowrap width="100"><ct:FWLabel key="ONLY_ACTIVE_ATTRIBUTION"/></TD>
            <TD nowrap><INPUT type="checkbox" name="forActif" checked="checked"/></TD>
        </TR>	

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>