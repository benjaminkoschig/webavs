<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	String numAffilie = request.getParameter("likeNumAffilie");
	idEcran="CAF0075";
	bButtonFind = true;
	bButtonNew = false;
	rememberSearchCriterias=true;
	String initEtat = CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE;
	
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js">

function postInit() {
	<% if( numAffilie != null) {
	 	initEtat = "";
	 } %>
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
	top.document.title = "Web@AVS - <ct:FWLabel key='NAOS_JSP_IDE_ANNONCE_TITRE'/>";
	usrAction = "naos.ide.ideAnnonce.lister";
	bFind=true;
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	  	<TR>
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_NUMERO_IDE"/></TD>
            <TD><INPUT name="prefixeNumeroIDE" size="4" maxlength="4" type="text" value="CHE-" readOnly tabindex="-1" class="prefixeIDEDisable" disabled="disabled"><INPUT type="text" id="likeNumeroIde" name="likeNumeroIde">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_NUMERO_AFFILIE"/></TD>
            <TD><INPUT type="text" id="likeNumeroAffilie" name="likeNumeroAffilie" value="<%=request.getParameter("likeNumAffilie")==null?"":request.getParameter("likeNumAffilie")%>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
          
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_RAISON_SOCIALE"/></TD>
            <TD><INPUT type="text" id="likeRaisonSociale" name="likeRaisonSociale"></TD>
      	</TR>
      	
      	<TR>
      		<TD colspan="6">&nbsp;</TD> 
      	</TR>
      	
      	<TR>
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_STATUT_IDE"/></TD>
            <TD><ct:FWCodeSelectTag name="forStatut" defaut="" codeType="VEIDESTATU" wantBlank="true"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
           
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_CREATION"/></TD>
            <TD><ct:FWCalendarTag name="fromDateCreation" doClientValidation="CALENDAR" value="" />&nbsp;<ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_A"/>&nbsp;<ct:FWCalendarTag name="untilDateCreation" doClientValidation="CALENDAR" value="" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
         
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_TRAITEMENT"/></TD>
            <TD><ct:FWCalendarTag name="fromDateTraitement" doClientValidation="CALENDAR" value="" />&nbsp;<ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_A"/>&nbsp;<ct:FWCalendarTag name="untilDateTraitement" doClientValidation="CALENDAR" value="" /> </TD>
      	</TR>
      	
      	<TR>
      		<TD colspan="6">&nbsp;</TD> 
      	</TR>
      	
      	<TR>
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_CATEGORIE"/></TD>
            <TD><ct:FWCodeSelectTag name="forCategorie" defaut="" codeType="VEIDECATAN" wantBlank="true"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
           
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_TYPE"/></TD>
            <TD><ct:FWCodeSelectTag name="forType" defaut="" codeType="VEIDETYPAN" wantBlank="true"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
         
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_ETAT"/></TD>
            <TD><ct:FWCodeSelectTag name="forEtat" defaut="<%=initEtat%>" codeType="VEIDEETAAN" wantBlank="true"  /></TD>
      	</TR>
      	
      	<TR>
      		<TD colspan="6">&nbsp;</TD> 
      	</TR>
      	
      	<TR>
      		<TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_AFFICHER_ANNONCE_PASSIVE"/></TD>
      		<TD><input type="checkbox" id="wantAnnoncePassive" name="wantAnnoncePassive"/></TD>
      		<TD colspan="4">&nbsp;</TD> 
      	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>