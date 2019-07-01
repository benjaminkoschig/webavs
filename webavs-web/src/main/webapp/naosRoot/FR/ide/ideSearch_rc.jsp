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
<script type="text/javascript"
        src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="CAF0074";	
	bButtonNew=false;
    String nss = request.getParameter("forNumeroAVS");
%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function prepareCreationEntiteIde(){
	setUserAction("naos.ide.ideAnnonce.prepareAjoutAnnonceIdeCreation");
	document.forms[0].target= "fr_main";
	document.forms[0].submit();	
}

var fieldsIde = ["#idePrefixe", "#forNumeroIDE", "#libSearchNumeroIDE"];
var fieldsOther = ["#forRaisonSociale", "#forNpa", "#forLocalite", "#forRue", "#forNumeroRue", "#forNaissance", "#forNumeroAVS", "#helpForRaisonSociale", "#libSearchRaisonSociale", "#libSearchNpa", "#libSearchLocalite", "#libSearchRue", "#libSearchNumeroRue", "#libSearchNaissance", "#divForNaissance", "#libSearchNumeroAVS"];
var fieldsAvs = ["#forNumeroAVS","#navsligne", "#libSearchNumeroAVS"];

function showHideChamRecherche() {
	if (eval(document.getElementById("modeSearch1").checked == true)) {
        showFields(fieldsIde);
        hideFields(fieldsOther);
        hideFields(fieldsAvs);
		
	} 
	if (eval(document.getElementById("modeSearch2").checked == true)) {
        hideFields(fieldsIde);
        showFields(fieldsOther);
        hideFields(fieldsAvs);
	}

    if (eval(document.getElementById("modeSearch3").checked == true)) {
        hideFields(fieldsIde);
        hideFields(fieldsOther);
        showFields(fieldsAvs);
    }

}

function hideFields(fields) {
    for(var i= 0; i < fields.length; i++) {
        $(fields[i]).hide();
    }
}

function showFields (fields) {
    for(var i= 0; i < fields.length; i++) {
        $(fields[i]).show();
    }
}


function postInit(){

	document.getElementById("modeSearch2").checked = true;

	showHideChamRecherche();
	
	$("#btnAddAnnonceCreationEntiteIde").click(function(){prepareCreationEntiteIde();});

    $('#partialdossierSearchlikeNss').change(function(){
        $('#forNumeroAVS').val($('[name=dossierSearchlikeNss]').val());
    });
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
           <INPUT type="hidden" id="raisonSociale" name="raisonSociale" value="<%=(request.getParameter("forRaisonSociale")==null)?"":new String(Base64.decodeBase64(request.getParameter("forRaisonSociale").getBytes())).replaceAll("\"","&quot;")%>">
           
            <TD colspan="10">
            	<label id="libRadioSearchIDE"><ct:FWLabel key="NAOS_JSP_IDE_RADIO_IDE"/></label>
            	<INPUT TYPE="radio" name="forTypeRecherche" id="modeSearch1" value="<%=AFIDEUtil.TYPE_RECHERCHE_NUM_IDE %>" onclick="showHideChamRecherche()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            	<label id="libRadioSearchAutreChamp"><ct:FWLabel key="NAOS_JSP_IDE_RADIO_AUTRECHAMP"/></label>
            	<INPUT TYPE="radio" name="forTypeRecherche" id="modeSearch2" value="<%=AFIDEUtil.TYPE_RECHERCHE_RAISON_SOCIALE %>" onclick="showHideChamRecherche()">
                <label id="libRadioSearchAutreChamp"><ct:FWLabel key="NAOS_JSP_IDE_RADIO_AVS"/></label>
                <INPUT TYPE="radio" name="forTypeRecherche" id="modeSearch3" value="<%=AFIDEUtil.TYPE_RECHERCHE_AVS %>" onclick="showHideChamRecherche()">
            </TD>
           
      	</TR>
      	
      	<TR>
      		<TD colspan="10">&nbsp;</TD>
      	</TR>

        <TR id="navsligne">
            <TD><label id="libSearchNumeroAVS"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NUMERO_AVS"/></label></TD>
            <TD><nss:nssPopup avsMinNbrDigit="99"
                              nssMinNbrDigit="99"
                              newnss=""
                              cssclass="nssPrefixe"
                              value="<%=nss%>"
                              name="dossierSearchlikeNss" />
                <input type="hidden" id="forNumeroAVS" name="forNumeroAVS"></TD>
            <TD colspan="9"></TD>
        </TR>

	  	<TR>
            <TD><label id="libSearchNumeroIDE"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NUMERO_IDE"/></label></TD>
            <TD><INPUT type="text" id="idePrefixe" name="idePrefixe" disabled="disabled" value="CHE" maxlength="3" size="3" style="text-align:center;" /> <INPUT type="text" id="forNumeroIDE" name="forNumeroIDE" value='<%=(request.getParameter("forNumeroIDE")==null)?"":request.getParameter("forNumeroIDE") %>'/></TD>
            <TD colspan="8"></TD>
      	</TR>
      	
            	
      	<TR>
            <TD><label id="libSearchRaisonSociale"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_RAISON_SOCIALE"/></label></TD>
            <TD>
            	<INPUT type="text" id="forRaisonSociale" name="forRaisonSociale" value="<%=(request.getParameter("forRaisonSociale")==null)?"":new String(Base64.decodeBase64(request.getParameter("forRaisonSociale").getBytes())).replaceAll("\"","&quot;")%>"/>
            	<div id="helpForRaisonSociale"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_RAISON_SOCIALE_HELP"/></div>
           	
            </TD>
          	<TD colspan="8"></TD>
      	</TR>
      	
      	<TR>
      		<TD colspan="10">&nbsp;</TD>
      	</TR>
      	
      	<TR>
           <TD><label id="libSearchNpa"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NPA"/></label></TD>
           <TD colspan="9"><INPUT type="text" id="forNpa" name="forNpa">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <label id="libSearchLocalite"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_LOCALITE"/></label>&nbsp;&nbsp;&nbsp;
           <INPUT type="text" id="forLocalite" name="forLocalite">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <label id="libSearchRue"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_RUE"/></label>&nbsp;&nbsp;&nbsp;
           <INPUT type="text" id="forRue" name="forRue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <label id="libSearchNumeroRue"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NUMERO_RUE"/></label>&nbsp;&nbsp;&nbsp;
           <INPUT type="text" id="forNumeroRue" name="forNumeroRue" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <span id="divForNaissance">
           <label id="libSearchNaissance"><ct:FWLabel key="NAOS_JSP_IDE_SEARCH_NAISSANCE"/></label>&nbsp;&nbsp;&nbsp;
           <ct:inputText id="forNaissance" name="forNaissance" notation="data-g-calendar=''" />
           </span>
           </TD>
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