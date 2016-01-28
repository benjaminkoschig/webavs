<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@page import="globaz.naos.util.AFIDEUtil"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.lang.String"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="CAF0074";	
	bButtonNew=false;
%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function prepareCreationEntiteIde(){
	setUserAction("naos.ide.ideAnnonce.prepareAjoutAnnonceIdeCreation");
	document.forms[0].target= "fr_main";
	document.forms[0].submit();	
}

function showHideChamRecherche() {
	if (eval(document.getElementById("modeSearch1").checked == true)) {
		jscss("remove", document.forms[0].idePrefixe, "hidden");
		jscss("remove", document.forms[0].forNumeroIDE, "hidden");
		jscss("add", document.forms[0].forRaisonSociale, "hidden");
		$("#helpForRaisonSociale").hide();
		jscss("add", document.forms[0].forNpa, "hidden");
		jscss("add", document.forms[0].forLocalite, "hidden");
		jscss("add", document.forms[0].forRue, "hidden");
		jscss("add", document.forms[0].forNumeroRue, "hidden");
		/* jscss("add", document.forms[0].wantSemblable, "hidden"); */
		$("#libSearchNumeroIDE").show();
		$("#libSearchRaisonSociale").hide();
		$("#libSearchNpa").hide();
		$("#libSearchLocalite").hide();
		$("#libSearchRue").hide();
		$("#libSearchNumeroRue").hide();
		/* $("#libSearchSemblable").hide(); */
		
	} 
	if (eval(document.getElementById("modeSearch2").checked == true)) {
		jscss("add", document.forms[0].idePrefixe, "hidden");
		jscss("add", document.forms[0].forNumeroIDE, "hidden");
		jscss("remove", document.forms[0].forRaisonSociale, "hidden");
		$("#helpForRaisonSociale").show();
		jscss("remove", document.forms[0].forNpa, "hidden");
		jscss("remove", document.forms[0].forLocalite, "hidden");
		jscss("remove", document.forms[0].forRue, "hidden");
		jscss("remove", document.forms[0].forNumeroRue, "hidden");
		/* jscss("remove", document.forms[0].wantSemblable, "hidden"); */
		$("#libSearchNumeroIDE").hide();
		$("#libSearchRaisonSociale").show();
		$("#libSearchNpa").show();
		$("#libSearchLocalite").show();
		$("#libSearchRue").show();
		$("#libSearchNumeroRue").show();
		/* $("#libSearchSemblable").show(); */
	}

}

function postInit(){

	document.getElementById("modeSearch2").checked = true;

	showHideChamRecherche();
	
	$("#btnAddAnnonceCreationEntiteIde").click(function(){prepareCreationEntiteIde();});
	

}



</SCRIPT>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<SCRIPT>
	top.document.title = "Web@AVS - <ct:FWLabel key='NAOS_JSP_IDE_SEARCH_TITRE'/>";
	usrAction = "naos.ide.ideSearch.lister";
	bFind = true;
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

		<TR>
           <INPUT type="hidden" id="ideAnnonceIdAffiliation" name="ideAnnonceIdAffiliation" value='<%=(request.getParameter("ideAnnonceIdAffiliation")==null)?"":request.getParameter("ideAnnonceIdAffiliation") %>'/>
           <INPUT type="hidden" id="idTiers" name="idTiers" value='<%=(request.getParameter("idTiers")==null)?"":request.getParameter("idTiers") %>'/>
           <INPUT type="hidden" id="ideAnnonceCategorie" name="ideAnnonceCategorie" value="<%=CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI%>">
           <INPUT type="hidden" id="ideAnnonceEtat" name="ideAnnonceEtat" value="<%=CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE%>">
           <INPUT type="hidden" id="ideAnnonceType" name="ideAnnonceType" value="<%=CodeSystem.TYPE_ANNONCE_IDE_CREATION%>">
           <INPUT type="hidden" id="raisonSociale" name="raisonSociale" value="<%=(request.getParameter("forRaisonSociale")==null)?"":new String(Base64.decodeBase64(request.getParameter("forRaisonSociale").getBytes())) %>">
           
            <TD colspan="8">
            	<label id="libRadioSearchIDE"><ct:FWLabel key="NAOS_JSP_IDE_RADIO_IDE"/></label>
            	<INPUT TYPE="radio" name="forTypeRecherche" id="modeSearch1" value="<%=AFIDEUtil.TYPE_RECHERCHE_NUM_IDE %>" onclick="showHideChamRecherche()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            	<label id="libRadioSearchAutreChamp"><ct:FWLabel key="NAOS_JSP_IDE_RADIO_AUTRECHAMP"/></label>
            	<INPUT TYPE="radio" name="forTypeRecherche" id="modeSearch2" value="<%=AFIDEUtil.TYPE_RECHERCHE_RAISON_SOCIALE %>" onclick="showHideChamRecherche()">
            </TD>
           
      	</TR>
      	
      	<TR>
      		<TD colspan="8">&nbsp;</TD>
      	</TR>

	  	<TR>
            <TD><label id="libSearchNumeroIDE"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NUMERO_IDE"/></label></TD>
            <TD><INPUT type="text" id="idePrefixe" name="idePrefixe" disabled="disabled" value="CHE" maxlength="3" size="3" style="text-align:center;" /> <INPUT type="text" id="forNumeroIDE" name="forNumeroIDE" value='<%=(request.getParameter("forNumeroIDE")==null)?"":request.getParameter("forNumeroIDE") %>'/></TD>
            <TD colspan="6"></TD>
      	</TR>
      	
            	
      	<TR>
            <TD><label id="libSearchRaisonSociale"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_RAISON_SOCIALE"/></label></TD>
            <TD>
            	<INPUT type="text" id="forRaisonSociale" name="forRaisonSociale" value="<%=(request.getParameter("forRaisonSociale")==null)?"":new String(Base64.decodeBase64(request.getParameter("forRaisonSociale").getBytes())) %>"/>
            	<div id="helpForRaisonSociale"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_RAISON_SOCIALE_HELP"/></div>
           	
            </TD>
          	<TD colspan="6"></TD>
      	</TR>
      	
      	<TR>
      		<TD colspan="8">&nbsp;</TD>
      	</TR>
      	
      	<TR>
           <TD><label id="libSearchNpa"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NPA"/></label></TD>
           <TD colspan="7"><INPUT type="text" id="forNpa" name="forNpa">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <label id="libSearchLocalite"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_LOCALITE"/></label>&nbsp;&nbsp;&nbsp;
           <INPUT type="text" id="forLocalite" name="forLocalite">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <label id="libSearchRue"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_RUE"/></label>&nbsp;&nbsp;&nbsp;
           <INPUT type="text" id="forRue" name="forRue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <label id="libSearchNumeroRue"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NUMERO_RUE"/></label>&nbsp;&nbsp;&nbsp;
           <INPUT type="text" id="forNumeroRue" name="forNumeroRue" ></TD>
      	</TR>
      	
      <TR>
      		<TD colspan="8">&nbsp;</TD>
      </TR>
      	
      <%-- <TR>
            <TD><label  id="libSearchSemblable"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_SEMBLABLE"/></label></TD>
           <TD><INPUT type="checkbox" id="wantSemblable" name="wantSemblable"></TD>
          <TD colspan="6"></TD>
      </TR> --%>
      	

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<INPUT type="button" name="btnAddAnnonceCreationEntiteIde" id="btnAddAnnonceCreationEntiteIde" value="<ct:FWLabel key='NAOS_JSP_IDE_SEARCH_BOUTON_CREATION_IDE'/>" />
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>