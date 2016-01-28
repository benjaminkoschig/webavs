<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/find/header.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- tpl:insert attribute="zoneInit" --%>
<% 
idEcran = "AL0001";
rememberSearchCriterias = true;
//actionNew = servletContext + mainServletPath + "?userAction=al.dossier.dossierMain.afficher&_method=add";
bButtonNew = false;
%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<SCRIPT language="JavaScript">
usrAction = "al.personne.personneAF.lister";


function resetSearchFields(){
	
	document.getElementById("searchModel.likeNom").value="";

	document.getElementById("searchModel.likePrenom").value="";
	document.getElementById("searchModel.likeNss").value="";
	document.getElementById("likeNss").value="";

	document.getElementById("forDate").value="";
	document.getElementById("searchModel.forDate").value="";

	document.getElementById("searchModel.whereKey").value="";

	document.forms[0].submit();	
}

function initCheckBox() {
	

	//surcharge du comportement du nss taglib pour être en accord avec l'écran AL0004
	// commenter cette ligne pour gérer aussi le n°avs
	$('input[name="partiallikeNss"]').keypress(function(e){	
	});
	$('input[name="partiallikeNss"]').keyup(function(e){
		nssOnKeyUp('likeNss');
		// pour formater a chaque passage
		nssAction('likeNss');
		var nssToSearch = $('input[name="likeNssNssPrefixe"]').val()+this.value;
		$('input[name="searchModel.likeNss"]').val(nssToSearch);
	});

}
</SCRIPT>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
		<%-- tpl:insert attribute="zoneTitle" --%>
		<ct:FWLabel key="AL0001_TITRE"/>
		<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	<div id="AL0001">
    	<table>
        	<tr>
              	<td><ct:FWLabel key="AL0001_RECHERCHE_NOM"/></td>
				<td>
					<input name="searchModel.likeNom" tabindex="1" type="text"/>
				</td>
                <td><ct:FWLabel key="AL0001_RECHERCHE_NAISSANCE"/></td>
				<td>
					
						<input id="forDateValue" name="searchModel.forDate" tabindex="4" 
							   type="text" data-g-calendar=" ">
					
					
					
				</td>
				<td><ct:FWLabel key="AL0001_RECHERCHE_ROLE"/></td>
				<td><select name="searchModel.whereKey" tabindex="5">
 						<option value="default"><ct:FWLabel key="AL0001_RECHERCHE_ROLE_TOUS"/></option>
 						<option value="roleAlloc"><ct:FWLabel key="AL0001_RECHERCHE_ROLE_ALLOC"/></option>
 						<option value="roleEnfant"><ct:FWLabel key="AL0001_RECHERCHE_ROLE_ENFANT"/></option>
 					</select>
 				</td>
				         
            </tr> 
           
            <tr>               
                <td><ct:FWLabel key="AL0001_RECHERCHE_PRENOM"/></td>
				<td>
					<input name="searchModel.likePrenom" tabindex="2" type="text"/>
				</td>           
 				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td> 
 				
                
             </tr>
             <tr>
                <td><ct:FWLabel key="AL0001_RECHERCHE_NUM_NSS"/></td>
				<td>
					<ct:inputHidden name="searchModel.likeNss"/>
					<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNss" newnss="true" tabindex="3"/>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><input type="button" value="Clear" style="width:100px;" onclick="resetSearchFields();" accesskey="C" tabindex="6"/>&nbsp;<ct:FWLabel key="AL0002_RECHERCHE_CLEAR"/></td> 
             </tr>
             
   
            
        </table>  
	</div>
	</td>
</tr>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
