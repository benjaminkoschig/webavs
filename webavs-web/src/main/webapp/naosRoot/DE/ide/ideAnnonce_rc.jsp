<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.naos.db.ide.AFIdeAnnonceListViewBean"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
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
	Object viewbean = session.getAttribute ("listViewBean");
	AFIdeAnnonceListViewBean viewBeanFind = null;
	if(viewbean != null && viewbean instanceof AFIdeAnnonceListViewBean) {
	    viewBeanFind = (AFIdeAnnonceListViewBean)viewbean;
	}
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js">

function postInit() {
	<%
	 
	String likeNumeroIde = "";
	String likeNumAffilie = "";
	String likeRaisonSociale = "";
	String forStatut = "";
	String fromDateCreation = "";
	String untilDateCreation = "";
	String fromDateTraitement = "";
	String untilDateTraitement = "";
	String forCategorie = "";
	String forType = "";
	String forEtat = "";
	Boolean wantAnnoncePassive = false;
	
	if(viewBeanFind != null) {
		likeNumeroIde = viewBeanFind.getLikeNumeroIde();
		likeNumAffilie = viewBeanFind.getLikeNumeroAffilie();
		likeRaisonSociale = viewBeanFind.getLikeRaisonSociale();
		forStatut = viewBeanFind.getForStatut();
		fromDateCreation = viewBeanFind.getFromDateCreation();
		untilDateCreation = viewBeanFind.getUntilDateCreation();
		fromDateTraitement = viewBeanFind.getFromDateTraitement();
		untilDateTraitement = viewBeanFind.getUntilDateTraitement();
		forCategorie = viewBeanFind.getForCategorie();
		forType = viewBeanFind.getForType();
		forEtat = viewBeanFind.getForEtat();
		wantAnnoncePassive = viewBeanFind.isWantAnnoncePassive();
	}
	
	 if(numAffilie != null && forEtat == "") {
	 	initEtat = "";
	 } else if(forEtat != "") {
	     initEtat = forEtat;
	 }
	 
	 %>

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
	top.document.title = "Web@AVS - <ct:FWLabel key='NAOS_JSP_IDE_ANNONCE_TITRE'/>";
	usrAction = "naos.ide.ideAnnonce.lister";
	bFind=true;
	
	function clearFields() {
		document.getElementsByName("likeNumeroIde")[0].value= "";
		document.getElementsByName("likeNumeroAffilie")[0].value= "";
		document.getElementsByName("likeRaisonSociale")[0].value="";
		document.getElementsByName("forStatut")[0].value="";
		document.getElementsByName("fromDateCreation")[0].value="";
		document.getElementsByName("untilDateCreation")[0].value="";
		document.getElementsByName("fromDateTraitement")[0].value="";
		document.getElementsByName("untilDateTraitement")[0].value="";
		document.getElementsByName("forCategorie")[0].value="";
		document.getElementsByName("forType")[0].value="";
		document.getElementsByName("forEtat")[0].value="";
		document.getElementsByName("wantAnnoncePassive")[0].checked = false;
		
	};
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	  	<TR>
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_NUMERO_IDE"/></TD>
            <TD><INPUT name="prefixeNumeroIDE" size="4" maxlength="4" type="text" value="CHE-" readOnly tabindex="-1" class="prefixeIDEDisable" disabled="disabled"><INPUT type="text" id="likeNumeroIde" name="likeNumeroIde" value="<%=likeNumeroIde%>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
            
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_NUMERO_AFFILIE"/></TD>
            <TD><INPUT type="text" id="likeNumeroAffilie" name="likeNumeroAffilie" value="<%=likeNumAffilie%>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
          
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_RAISON_SOCIALE"/></TD>
            <TD><INPUT type="text" id="likeRaisonSociale" name="likeRaisonSociale" value="<%=likeRaisonSociale%>"></TD>
      	</TR>
      	
      	<TR>
      		<TD colspan="6">&nbsp;</TD> 
      	</TR>
      	
      	<TR>
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_STATUT_IDE"/></TD>
            <TD><ct:FWCodeSelectTag name="forStatut" defaut="<%=forStatut%>" codeType="VEIDESTATU" wantBlank="true"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
           
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_CREATION"/></TD>
            <TD><ct:FWCalendarTag name="fromDateCreation" doClientValidation="CALENDAR" value="<%=fromDateCreation%>" />&nbsp;<ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_A"/>&nbsp;<ct:FWCalendarTag name="untilDateCreation" doClientValidation="CALENDAR" value="<%=untilDateCreation%>" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
         
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_TRAITEMENT"/></TD>
            <TD><ct:FWCalendarTag name="fromDateTraitement" doClientValidation="CALENDAR" value="<%=fromDateTraitement%>" />&nbsp;<ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_A"/>&nbsp;<ct:FWCalendarTag name="untilDateTraitement" doClientValidation="CALENDAR" value="<%=untilDateTraitement%>" /> </TD>
      	</TR>
      	
      	<TR>
      		<TD colspan="6">&nbsp;</TD> 
      	</TR>
      	
      	<TR>
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_CATEGORIE"/></TD>
            <TD><ct:FWCodeSelectTag name="forCategorie" defaut="<%=forCategorie%>"  codeType="VEIDECATAN" wantBlank="true"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
           
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_TYPE"/></TD>
            <TD><ct:FWCodeSelectTag name="forType" defaut="<%=forType%>" codeType="VEIDETYPAN" wantBlank="true"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
         
            <TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_ETAT"/></TD>
            <TD><ct:FWCodeSelectTag name="forEtat" defaut="<%=initEtat%>" codeType="VEIDEETAAN" wantBlank="true"  /></TD>
      	</TR>
      	
      	<TR>
      		<TD colspan="6">&nbsp;</TD> 
      	</TR>
      	
      	<TR>
      		<TD><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_AFFICHER_ANNONCE_PASSIVE"/></TD>
      		<TD><input type="checkbox" id="wantAnnoncePassive" name="wantAnnoncePassive" <%=wantAnnoncePassive ? "checked" : ""%>/></TD>
      		<TD colspan="4">&nbsp;</TD> 
      	</TR>
      	
      	<TR>
      		<TD colspan="6">&nbsp;</TD> 
      	</TR>
      	      	
		<TR>
			<TD>
				<input 	type="button" 
						onclick="clearFields()" 
						accesskey="<ct:FWLabel key="AK_EFFACER"/>" 
						value="<ct:FWLabel key="JSP_EFFACER"/>">
				<label>
					[ALT+<ct:FWLabel key="AK_EFFACER"/>]
				</label>
			</TD>
		</TR>      	

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>